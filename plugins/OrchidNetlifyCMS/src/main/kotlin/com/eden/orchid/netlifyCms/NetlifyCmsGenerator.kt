package com.eden.orchid.netlifyCms

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.netlifyCms.pages.NetlifyCmsAdminPage
import com.eden.orchid.netlifyCms.util.toNetlifyCmsField
import com.eden.orchid.utilities.OrchidUtils
import org.json.JSONArray
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject

@JvmSuppressWildcards
class NetlifyCmsGenerator @Inject
constructor(
        context: OrchidContext,
        val templateTags: Set<TemplateTag>,
        val extractor: OptionsExtractor
) : OrchidGenerator(context, "netlifyCms", 1) {

    @Option
    lateinit var backend: JSONObject

    @Option @StringDefault("src/orchid/resources")
    lateinit var resourceRoot: String

    override fun startIndexing(): List<OrchidPage>? {
        if(backend.length() > 0) {
            val pages = ArrayList<OrchidPage>()

            val adminResource = context.getResourceEntry("netlifyCms/admin.peb")
            adminResource.reference.stripFromPath("netlifyCms")
            val adminPage = NetlifyCmsAdminPage(adminResource, templateTags)
            pages.add(adminPage)

            val adminConfig = OrchidPage(StringResource(context, "admin/config.yml", getYamlConfig()), "admin")
            adminConfig.reference.isUsePrettyUrl = false
            pages.add(adminConfig)

            return pages
        }

        return null
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderRaw(it) }
    }

    private fun getYamlConfig(): String {
        val jsonConfig = JSONObject()

        jsonConfig.put("backend", backend)
        jsonConfig.put("publish_mode", "editorial_workflow")
        jsonConfig.put("media_folder", "src/orchid/resources/assets/media")
        jsonConfig.put("public_folder", "assets/images/media")
        jsonConfig.put("collections", JSONArray())

        context
                .getFilteredGenerators(false)
                .map { it.collections }
                .filter { !EdenUtils.isEmpty(it) }
                .flatMap { it.stream() }
                .forEach { collection ->
                    val collectionData = JSONObject()
                    collectionData.put("label", collection.label)
                    collectionData.put("name", collection.name)

                    if(collection.collectionType == OrchidCollection.Type.Folder) {
                        setupFolderCollection(collectionData, collection)
                    }
                    if(collection.collectionType == OrchidCollection.Type.Files) {
                        setupFileCollection(collectionData, collection)
                    }

                    jsonConfig.getJSONArray("collections").put(collectionData)
                }

        val yaml = Yaml()
        return yaml.dump(jsonConfig.toMap())
    }

    private fun setupFolderCollection(collectionData: JSONObject, collection: OrchidCollection) {
        collectionData.put("folder", "${OrchidUtils.normalizePath(resourceRoot)}/${collection.resourceRoot}")
        collectionData.put("create", collection.isCanCreate)
        collectionData.put("fields", JSONArray())
        extractor.describeOptions(collection.collectionPageType).forEach {
            collectionData.getJSONArray("fields").put(it.toNetlifyCmsField())
        }
        collectionData.getJSONArray("fields").put(getBodyField())
    }

    private fun setupFileCollection(collectionData: JSONObject, collection: OrchidCollection) {
        collectionData.put("files", JSONArray())

        collection.resources.forEach { page ->
            val pageData = JSONObject()
            pageData.put("label", page.title)
            pageData.put("name", page.resource.reference.originalFileName)
            pageData.put("file", "${OrchidUtils.normalizePath(resourceRoot)}/${page.resource.reference.originalFullFileName}")

            pageData.put("fields", JSONArray())
            page.describeOptions(context).forEach {
                pageData.getJSONArray("fields").put(it.toNetlifyCmsField())
            }
            pageData.getJSONArray("fields").put(getBodyField())

            collectionData.getJSONArray("files").put(pageData)
        }
    }

    private fun getBodyField(): JSONObject {
        val bodyField = JSONObject()
        bodyField.put("label", "Page Content")
        bodyField.put("name", "body")
        bodyField.put("widget", "markdown")
        return bodyField
    }

    override fun getCollections(): List<OrchidCollection>? {
        return null
    }
}