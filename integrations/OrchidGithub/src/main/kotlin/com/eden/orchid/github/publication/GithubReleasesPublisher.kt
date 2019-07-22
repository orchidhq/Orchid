package com.eden.orchid.github.publication

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.changelog.model.ChangelogModel
import com.eden.orchid.utilities.resolve
import com.google.inject.name.Named
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

@Description("Create releases directly on Github.", name = "Github Releases")
class GithubReleasesPublisher
@Inject
constructor(
    private val client: OkHttpClient,
    @Named("githubToken")
    private val githubToken: String
) : OrchidPublisher("githubReleases") {

    @Option
    @Description("The repository to release to, as [username/repo].")
    lateinit var repo: String

    @Option
    @Description("The `target_commitish` value to create the release tag on.")
    lateinit var commitish: String

    @Option
    @Description("Whether this is a prerelease.")
    var prerelease: Boolean = false

    override fun validate(context: OrchidContext): Boolean {
        val valid = super.validate(context)
        return valid && hasVersion(context, context.resolve())
    }

    private fun hasVersion(context: OrchidContext, model: ChangelogModel): Boolean {
        // make sure the current site version has a matching changelog version
        if (model.getVersion(context.site.version) == null) {
            Clog.e("Required changelog entry for version '{}' is missing.", context.site.version)
            return false
        }
        return true
    }

    override fun publish(context: OrchidContext) {
        val model: ChangelogModel = context.resolve()

        val version = model.getVersion(context.site.version)
        val url = Clog.format("https://api.github.com/repos/{}/releases", repo)
        val request = Request.Builder().url(url)
            .header("Authorization", "token $githubToken")
            .post(
                RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    JSONObject(
                        mutableMapOf(
                            "tag_name" to version?.version,
                            "name" to version?.version,
                            "body" to version?.content,
                            "prerelease" to prerelease
                        ).also {
                            if(commitish.isNotBlank()) {
                                it["target_commitish"] = commitish
                            }
                        }
                    ).toString()
                )
            )
            .build()
        Clog.d("Github POST: {}", url)
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            Clog.e("{}", response.body()!!.string())
        }
    }
}
