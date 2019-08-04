package com.eden.orchid.netlify.publication

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.makeMillisReadable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.apache.commons.io.FileUtils
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Named
import javax.validation.ValidationException
import javax.validation.constraints.NotBlank

@Description(value = "Upload your site directly to Netlify, while using your favorite CI platform.", name = "Netlify")
class NetlifyPublisher @Inject
constructor(
    private val client: OkHttpClient,

    @Named("dest")
    private val destinationDir: String,

    @Named("netlifyToken")
    @NotBlank(message = "A Netlify Personal Access Token is required for deploys, set as \'netlifyToken\' flag")
    val netlifyToken: String
) : OrchidPublisher("netlify") {

    @Option
    @Description("Your Netlify site ID or domain (ie. orchid.netlify.com). If not provided, your site's baseUrl will be used.")
    lateinit var siteId: String

    override fun validate(context: OrchidContext): Boolean {
        var valid = super.validate(context)

        if(siteId.isBlank()) {
            // siteId not provided, use the baseUrl instead
            try {
                siteId = URL(context.baseUrl).host
            }
            catch (e: Exception) { }
        }

        if(siteId.isBlank()) {
            throw ValidationException("A valid siteId must be provided.")
        }

        // make sure the site exists
        val site = getFrom("sites/$siteId").call(client)
        if (!site.first) {
            Clog.e("A Netlify site at {} does not exist or it cannot be accessed.", siteId)
            valid = false
        }
        return valid
    }

    override fun publish(context: OrchidContext) {
        doNetlifyDeploy()
    }

    private fun doNetlifyDeploy() {
        val file = File(destinationDir)
        val fileMap = mutableMapOf<String, MutableList<Pair<String, File>>>()

        // create digest of files to be uploaded
        if (file.exists() && file.isDirectory) {
            FileUtils.listFiles(file, null, true)
                .filter { it.isFile }
                .forEach {
                    val path = OrchidUtils.getRelativeFilename(it.absolutePath, destinationDir)
                    val sha1 = OrchidUtils.sha1(it)
                    fileMap.computeIfAbsent(sha1) { ArrayList() }.add(path to it)
                }
        }

        // post to Netlify to determine which files need to be uploaded still
        val asyncDeployResponse = startDeploySite(fileMap)

        // poll Netlify until it has finished determining which files need to be uploaded, or until a timeout is reached
        val deployResponse = pollUntilDeployIsReady(asyncDeployResponse)

        if (deployResponse.toUploadTotalCount == 0) {
            Clog.i("All files up-to-date on Netlify.")
        } else {
            uploadRequiredFiles(deployResponse)
        }
    }

    /**
     * This method will start the Netlify deploy using their async method, which is needed to deploy really large sites.
     * It will make the initial request, but must then poll for a while until either the site is ready or a timeout is
     * reached. That timeout is proportional to the number of files being uploaded.
     */
    private fun startDeploySite(
        files: Map<String, MutableList<Pair<String, File>>>
    ) : CreateSiteResponse {
        val body = JSONObject()
        body.put("async", true)
        body.put("files", JSONObject().apply {
            for((sha1, filePairs) in files) {
                for(filePair in filePairs) {
                    this.put(filePair.first,  sha1)
                }
            }
        })

        val requiredFilesResponse = body.postTo("sites/$siteId/deploys").call(client)
        if (!requiredFilesResponse.first) {
            throw RuntimeException("something went wrong attempting to deploy to Netlify: " + requiredFilesResponse.second)
        }

        val required = JSONObject(requiredFilesResponse.second)

        val deployId = required.getString("id")

        return CreateSiteResponse(
            deployId,

            files,

            emptyList()
        )
    }

    /**
     * Poll for a while until either the site is ready or a timeout is reached. That timeout is proportional to the number of files being uploaded.
     */
    private fun pollUntilDeployIsReady(response: CreateSiteResponse): CreateSiteResponse {
        val timeout = (30 * 1000) + (response.originalFilesCount * 50) // give timeout of 30s + 50ms * (number of files)
        val startTime = System.currentTimeMillis()

        while(true) {
            val now = System.currentTimeMillis()
            if((now - startTime) > timeout) break

            val requiredFilesResponse = getFrom("sites/$siteId/deploys/${response.deployId}").call(client)

            val required = JSONObject(requiredFilesResponse.second)

            val deployState = required.getString("state")

            if(deployState == "prepared" && (required.has("required") || required.has("required_functions"))) {
                val requiredFiles = required.optJSONArray("required")?.filterNotNull()?.map { it.toString() } ?: emptyList()

                return response.copy(requiredFiles = requiredFiles)
            }
            else {
                Clog.d("        Deploy still processing, trying again in {}", 5000.makeMillisReadable())
                Thread.sleep(5000)
            }
        }

        throw IOException("NetlifyPublisher timed out waiting for site to be ready to upload files")
    }

    /**
     * Upload the required files to Netlify as site files as requested from the initial deploy call.
     */
    private fun uploadRequiredFiles(response: CreateSiteResponse) {
        response.uploadedFilesCount = 0
        // upload all required files
        Clog.i("Uploading {} files to Netlify.", response.toUploadFilesCount)

        response
            .requiredFiles
            .flatMap { sha1ToUpload -> response.getFiles(sha1ToUpload) }
            .parallelStream()
            .forEach { fileToUpload ->
                val path = OrchidUtils.getRelativeFilename(fileToUpload.absolutePath, destinationDir)
                Clog.d("Netlify FILE UPLOAD {}/{}: {}", response.uploadedFilesCount + 1, response.toUploadFilesCount, path)
                fileToUpload.uploadTo("deploys/${response.deployId}/files/$path").call(client)
                response.uploadedFilesCount++
            }
    }

    private fun getFrom(url: String, vararg args: Any): Request {
        val fullURL = Clog.format("$netlifyUrl/$url", *args)
        Clog.d("Netlify GET: {}", fullURL)
        return newRequest(fullURL)
            .get()
            .build()
    }

    private fun JSONObject.postTo(url: String, vararg args: Any): Request {
        val fullURL = Clog.format("$netlifyUrl/$url", *args)
        Clog.d("Netlify POST: {}", fullURL)
        return newRequest(fullURL)
            .post(this.toString().toRequestBody(JSON))
            .build()
    }

    private fun File.uploadTo(url: String, vararg args: Any): Request {
        val fullURL = Clog.format("$netlifyUrl/$url", *args)

        return newRequest(fullURL)
            .put(this.asRequestBody(BINARY))
            .build()
    }

    private fun newRequest(url: String): Request.Builder {
        return Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $netlifyToken")
    }

    private fun Request.call(client: OkHttpClient) : Pair<Boolean, String> {
        val response = client
            .newCall(this)
            .execute()
            .timeoutRateLimit()

        return try {
            val bodyString = response.body!!.string()
            if (!response.isSuccessful) {
                Clog.e("{}", bodyString)
            }

            response.isSuccessful to bodyString
        } catch (e: Exception) {
            e.printStackTrace()
            false to ""
        }
    }

    private fun Response.timeoutRateLimit(): Response {
        try {
            val rateLimitLimit = header("X-Ratelimit-Limit")?.toIntOrNull() ?: header("X-RateLimit-Limit")?.toIntOrNull() ?: 1
            val rateLimitRemaining = header("X-Ratelimit-Remaining")?.toIntOrNull() ?: header("X-RateLimit-Remaining")?.toIntOrNull() ?: 1
            val rateLimitReset = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(header("X-Ratelimit-Reset")!!).toInstant()
            val current = Instant.now()
            val d = Duration.between(rateLimitReset, current)

            // if we are nearing the rate limit, pause down a bit until it resets
            if ((rateLimitRemaining * 1.0 / rateLimitLimit * 1.0) < 0.1) {
                val totalMillis = Math.abs(d.toMillis())
                Clog.d("        Rate limit running low, sleeping for {}", totalMillis.makeMillisReadable())
                Thread.sleep(totalMillis)
            }
        } catch (e: Exception) { }

        return this
    }

    companion object {
        private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        private val BINARY = "application/octet-stream".toMediaTypeOrNull()
        private const val netlifyUrl = "https://api.netlify.com/api/v1"
    }
}

private data class CreateSiteResponse(
    val deployId: String,

    val originalFiles: Map<String, MutableList<Pair<String, File>>>,

    val requiredFiles: List<String>
) {

    val originalFilesCount: Int by lazy {
        originalFiles.map { it.value.size }.sum()
    }
    val originaltotalCount: Int = originalFilesCount

    val toUploadFilesCount: Int = requiredFiles.size
    val toUploadTotalCount: Int = toUploadFilesCount

    var uploadedFilesCount = 0

    fun getFiles(sha1: String) : List<File> {
        return originalFiles.getOrDefault(sha1, ArrayList()).map { it.second }
    }
}
