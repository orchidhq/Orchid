package com.eden.orchid.netlifycms

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.FolderCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.ResourceCollection
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.tasks.TaskService
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.netlifycms.pages.NetlifyCmsAdminPage
import com.eden.orchid.netlifycms.util.getNetlifyCmsFields
import com.eden.orchid.utilities.OrchidUtils
import org.json.JSONArray
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@JvmSuppressWildcards
class NetlifyCmsGenerator @Inject
constructor(
        context: OrchidContext,
        val templateTags: Set<TemplateTag>,
        val components: Set<OrchidComponent>,
        val menuItems: Set<OrchidMenuItem>,
        val extractor: OptionsExtractor
) : OrchidGenerator(context, "netlifyCms", OrchidGenerator.PRIORITY_DEFAULT) {

    override fun setTheme(theme: Any?) {

    }

    override fun getTheme(): Any {
        return "Default"
    }

    @Option
    @Description("A config object to pass to the Netlify CMS to configure the backend.")
    lateinit var backend: JSONObject

    @Option @StringDefault("src/orchid/resources")
    @Description("The resource directory, relative to the git repo root, where all resources are located.")
    lateinit var resourceRoot: String

    @Option @StringDefault("assets/media")
    @Description("The resource directory, relative to the resourceRoot, where 'media' resources are located.")
    lateinit var mediaFolder: String

    override fun startIndexing(): List<OrchidPage>? {
        return null
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        val includeCms: Boolean

        if(context.taskType == TaskService.TaskType.SERVE) {
            backend.put("name", "orchid-server")
            resourceRoot = ""
            includeCms = true
        }
        else {
            includeCms = backend.length() > 0
        }

        if(includeCms) {
            val adminResource = context.getResourceEntry("netlifyCms/admin.peb")
            adminResource.reference.stripFromPath("netlifyCms")
            val adminPage = NetlifyCmsAdminPage(adminResource, templateTags, components, menuItems)
            context.renderRaw(adminPage)

            val adminConfig = OrchidPage(StringResource(context, "admin/config.yml", getYamlConfig()), "admin")
            adminConfig.reference.isUsePrettyUrl = false
            context.renderRaw(adminConfig)
        }
    }

    private fun getYamlConfig(): String {
        val jsonConfig = JSONObject()

        jsonConfig.put("backend", backend)
//        jsonConfig.put("publish_mode", "editorial_workflow")
        jsonConfig.put("media_folder", OrchidUtils.normalizePath("$resourceRoot/${mediaFolder}"))
        jsonConfig.put("public_folder", OrchidUtils.normalizePath("$resourceRoot/${mediaFolder}"))
        jsonConfig.put("collections", JSONArray())

        context.collections
                .forEach { collection ->
                    val collectionData = JSONObject()
                    collectionData.put("label", if (!EdenUtils.isEmpty(collection.label)) {collection.label} else { collection.collectionId.capitalize() })
                    collectionData.put("name", "${collection.collectionType}_${collection.collectionId}")

                    when (collection) {
                        is FolderCollection -> setupFolderCollection(collectionData, collection)
                        is FileCollection -> setupFileCollection(collectionData, collection)
                        is ResourceCollection -> setupResourceCollection(collectionData, collection)
                    }

                    jsonConfig.getJSONArray("collections").put(collectionData)
                }

        val yaml = Yaml()
        return yaml.dump(jsonConfig.toMap())
    }

    private fun setupFolderCollection(collectionData: JSONObject, collection: FolderCollection) {
        collectionData.put("folder", OrchidUtils.normalizePath("$resourceRoot/${collection.resourceRoot}"))
        collectionData.put("create", collection.isCanCreate)
        collectionData.put("fields", extractor.describeAllOptions(collection.pageClass).getNetlifyCmsFields())
        collectionData.getJSONArray("fields").put(getBodyField())
    }

    private fun setupFileCollection(collectionData: JSONObject, collection: FileCollection) {
        collectionData.put("files", JSONArray())

        collection.items.forEach { page ->
            val pageData = JSONObject()
            pageData.put("label", page.title)
            pageData.put("name", page.resource.reference.originalFileName)
            pageData.put("file", OrchidUtils.normalizePath("$resourceRoot/${page.resource.reference.originalFullFileName}"))
            pageData.put("fields", extractor.describeAllOptions(page.javaClass).getNetlifyCmsFields())
            pageData.getJSONArray("fields").put(getBodyField())

            collectionData.getJSONArray("files").put(pageData)
        }
    }

    private fun setupResourceCollection(collectionData: JSONObject, collection: ResourceCollection<Any>) {
        collectionData.put("files", JSONArray())

        collection.resources.forEach { res ->
            val pageData = JSONObject()
            pageData.put("label", res.reference.originalFileName)
            pageData.put("name", res.reference.originalFileName)
            pageData.put("file", OrchidUtils.normalizePath("$resourceRoot/${res.reference.originalFullFileName}"))
            pageData.put("fields", extractor.describeAllOptions(collection.itemClass).getNetlifyCmsFields())
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

    override fun getCollections(): List<OrchidCollection<*>>? {
        return null
    }
}
