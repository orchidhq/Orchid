package com.eden.orchid.github.menu

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidExternalPage
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class GithubMenuItem : OrchidMenuFactory("github") {

    @Option
    lateinit var githubProject: String

    @Option
    lateinit var title: String

    val githubToken: String?
        get() {
            return OrchidFlags.getInstance().getFlagValue("githubToken")
        }

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        val githubPage = loadGithubPage(context, context.resolve(OkHttpClient::class.java))

        return if (githubPage.first && githubPage.second != null) {
            listOf(MenuItem.Builder(context) {
                page(githubPage.second)

                if(this@GithubMenuItem.title.isNotBlank()) {
                    title(this@GithubMenuItem.title)
                }
            }.build())
        } else {
            emptyList()
        }
    }

    private fun loadGithubPage(context: OrchidContext, client: OkHttpClient): Pair<Boolean, OrchidPage?> {
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

                val pageRef = OrchidReference.fromUrl(context, name, url)
                val pageRes = StringResource(description, pageRef)
                val page = OrchidExternalPage(pageRes, "githubProject", "")
                page.description = """
                    |<span class="stars">
                    |    <span class="icon"><i class="fas fa-star"></i></span>
                    |    <span>$starCount</span>
                    |</span>
                """.trimMargin()

                return Pair(true, page)
            } else {
                Clog.e("{}", bodyString)
            }
        } catch (e: Exception) {
        }


        return Pair(false, null)
    }
}
