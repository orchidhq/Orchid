package com.eden.orchid.copper

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.GlobalCollection
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidExternalPage
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Provider

class CopperGithubProjectGlobalCollection
@Inject
constructor(
    val context: Provider<OrchidContext>,
    val client: OkHttpClient
) : GlobalCollection<OrchidPage>("githubProject") {

    val latestPostsRegex = "^:githubProject\\((.*)\\)$".toRegex()

    val cachedProjectPages = mutableMapOf<String, Pair<Boolean, OrchidPage?>>()

    val githubToken: String?
        get() {
            return OrchidFlags.getInstance().getFlagValue("githubToken")
        }

    override fun loadItems(): List<OrchidPage> = emptyList()

    override fun find(id: String?): Stream<OrchidPage> {
        if(id != null) {
            if(id.matches(latestPostsRegex)) {
                val (githubProject) = latestPostsRegex.matchEntire(id)!!.destructured

                val projectPage = cachedProjectPages.getOrPut(githubProject) { loadGithubPage(githubProject) }
                if(projectPage.first) {
                    return Stream.of(projectPage.second)
                }
            }
        }
        return emptyList<OrchidPage>().stream()
    }

    private fun loadGithubPage(githubProject: String): Pair<Boolean, OrchidPage?> {
        try {
            val request = Request.Builder().url("https://api.github.com/repos/$githubProject").get()

            if (!EdenUtils.isEmpty(githubToken)) {
                request.header("Authorization", "token $githubToken")
            }

            val response = client.newCall(request.build()).execute()
            val bodyString = response.body()!!.string()

            if (response.isSuccessful) {
                val jsonBody = JSONObject(bodyString)

                val name = jsonBody.getString("name")
                val url = jsonBody.getString("html_url")
                val description = jsonBody.getString("description")
                val starCount = jsonBody.getInt("stargazers_count")

                val pageRef = OrchidReference.fromUrl(context.get(), name, "https://$url")
                val pageRes = StringResource(description, pageRef)
                val page = OrchidExternalPage(pageRes, "githubProject", "")
                page.description = """
                    |<span class="stars">
                    |    <span class="icon"><i class="fas fa-star"></i></span>
                    |    <span>$starCount</span>
                    |</span>
                """.trimMargin()

                return Pair(true, page)
            }
            else {
                Clog.e("{}", bodyString)
            }
        } catch (e: Exception) {
        }


        return Pair(false, null)
    }


}

