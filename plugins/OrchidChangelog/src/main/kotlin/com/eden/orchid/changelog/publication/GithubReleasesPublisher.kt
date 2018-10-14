package com.eden.orchid.changelog.publication

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.changelog.model.ChangelogModel
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Description("Create releases directly on Github.", name = "GithubReleases")
class GithubReleasesPublisher
@Inject
constructor(
        context: OrchidContext,
        val model: ChangelogModel,
        val client: OkHttpClient
) : OrchidPublisher(context, "githubReleases", 100) {

    @Description("A GitHub Personal Access Token.")
    lateinit var githubToken: String

    @Description("The repository to release to, as [username/repo].")
    lateinit var repo: String

    override fun validate(): Boolean {
        val valid = super.validate()
        return valid && hasVersion()
    }

    private fun hasVersion(): Boolean {
        // make sure the current site version has a matching changelog version
        if (model.getVersion(context.site.version) == null) {
            Clog.e("Required changelog entry for version '{}' is missing.", context.site.version)
            return false
        }
        return true
    }

    override fun publish() {
        val version = model.getVersion(context.site.version)
        val url = Clog.format("https://api.github.com/repos/{}/releases", repo)
        val request = Request.Builder().url(url)
                .header("Authorization", "token $githubToken")
                .post(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        JSONObject(mapOf(
                                "tag_name" to version?.version,
                                "name" to version?.version,
                                "body" to version?.content))
                                .toString()))
                .build()
        Clog.d("Github POST: {}", url)
        try {
            val response = client.newCall(request).execute()
            timeoutRateLimit(response)

            if (!response.isSuccessful) {
                val bodyString = response.body()!!.string()
                Clog.e("{}", bodyString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun timeoutRateLimit(response: Response) {
        try {
            val rlLimit = Integer.parseInt(response.header("X-RateLimit-Limit")!!)
            val rlRemaining = Integer.parseInt(response.header("X-RateLimit-Remaining")!!)
            val rlReset = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(response.header("X-RateLimit-Reset")).toInstant()
            val current = Instant.now()
            val d = Duration.between(rlReset, current)

            // if we are nearing the rate limit, pause down a bit until it resets
            if (rlRemaining * 1.0 / rlLimit * 1.0 < 0.1) {
                Thread.sleep(Math.abs(d.toMillis()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}