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
import com.eden.orchid.api.options.annotations.BooleanDefault
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
import com.eden.orchid.netlifycms.util.toNetlifyCmsSlug
import com.eden.orchid.utilities.OrchidUtils
import org.json.JSONArray
import org.json.JSONObject
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

    @Option
    @Description("Arbitrary config options to add to the main CMS config file.")
    lateinit var config: JSONObject

    @Option @StringDefault("src/orchid/resources")
    @Description("The resource directory, relative to the git repo root, where all resources are located.")
    lateinit var resourceRoot: String

    @Option @StringDefault("assets/media")
    @Description("The resource directory, relative to the resourceRoot, where 'media' resources are located.")
    lateinit var mediaFolder: String

    @Option @BooleanDefault(false)
    @Description("Whether options of parent classes are included.")
    var includeInheritedOptions: Boolean = false

    @Option @BooleanDefault(true)
    @Description("Whether options of its own class are included.")
    var includeOwnOptions: Boolean = true

    @Option @BooleanDefault(false)
    @Description("Whether to use the Netlify Identity Widget for managing user accounts. Should be paired with the `git-gateway` backend.")
    var useNetlifyIdentityWidget: Boolean = false

    override fun startIndexing(): List<OrchidPage>? {
        return null
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        val includeCms: Boolean

        if(isRunningLocally(context)) {
            config.put("backend", JSONObject())
            config.getJSONObject("backend").put("name", "orchid-server")
            resourceRoot = ""
            includeCms = true
        }
        else {
            includeCms = config.has("backend")
        }

        if(includeCms) {
            val adminResource = context.getResourceEntry("netlifyCms/admin.peb")
            adminResource.reference.stripFromPath("netlifyCms")
            val adminPage = NetlifyCmsAdminPage(adminResource, templateTags, components, menuItems, useNetlifyIdentityWidget)
            context.renderRaw(adminPage)

            val adminConfig = OrchidPage(StringResource(context, "admin/config.yml", getYamlConfig()), "admin", null)
            adminConfig.reference.isUsePrettyUrl = false
            context.renderRaw(adminConfig)
        }
    }

    private fun getYamlConfig(): String {
        var jsonConfig = JSONObject()

        jsonConfig.put("media_folder", OrchidUtils.normalizePath("$resourceRoot/$mediaFolder"))
        jsonConfig.put("public_folder", "/" + OrchidUtils.normalizePath(mediaFolder))
        jsonConfig.put("collections", JSONArray())

        context.collections
                .forEach { collection ->
                    var collectionData = JSONObject()
                    val shouldContinue: Boolean = when (collection) {
                        is FolderCollection -> setupFolderCollection(collectionData, collection)
                        is FileCollection -> setupFileCollection(collectionData, collection)
                        is ResourceCollection -> setupResourceCollection(collectionData, collection)
                        else -> false
                    }
                    if(shouldContinue) {
                        collectionData.put("label", collection.title)
                        collectionData.put("name", "${collection.collectionType}_${collection.collectionId}")

                        // merge in collection-specific options
                        var queryStrings = arrayOf(
                                "${collection.collectionType}.collections",
                                "${collection.collectionType}.collections.${collection.collectionId}"
                        )

                        queryStrings.forEach { queryString ->
                            var configOptions = context.query(queryString)
                            if (EdenUtils.elementIsObject(configOptions)) {
                                val queryResult = configOptions.element as JSONObject
                                collectionData = EdenUtils.merge(collectionData, queryResult)
                            }
                        }

                        jsonConfig.getJSONArray("collections").put(collectionData)
                    }
                }

        jsonConfig = EdenUtils.merge(jsonConfig, config)

        return context.serialize("yaml", jsonConfig.toMap())
    }

    private fun setupFolderCollection(collectionData: JSONObject, collection: FolderCollection): Boolean {
        collectionData.put("folder", OrchidUtils.normalizePath("$resourceRoot/${collection.resourceRoot}"))
        collectionData.put("create", collection.isCanCreate)
        collectionData.put("delete", collection.isCanDelete)
        collectionData.put("slug", collection.slugFormat.toNetlifyCmsSlug())
        collectionData.put("fields", extractor.describeOptions(collection.pageClass, includeOwnOptions, includeInheritedOptions).getNetlifyCmsFields())

        return true
    }

    private fun setupFileCollection(collectionData: JSONObject, collection: FileCollection): Boolean {
        collectionData.put("files", JSONArray())

        collection.items.forEach { page ->
            val pageData = JSONObject()
            pageData.put("label", page.title)
            pageData.put("name", page.resource.reference.originalFileName)
            pageData.put("file", OrchidUtils.normalizePath("$resourceRoot/${page.resource.reference.originalFullFileName}"))
            pageData.put("fields", extractor.describeOptions(page.javaClass, includeOwnOptions, includeInheritedOptions).getNetlifyCmsFields())

            collectionData.getJSONArray("files").put(pageData)
        }
        return true
    }

    private fun setupResourceCollection(collectionData: JSONObject, collection: ResourceCollection<Any>): Boolean {
        collectionData.put("files", JSONArray())

        collection.resources.forEach { res ->
            val pageData = JSONObject()
            pageData.put("label", res.reference.originalFileName)
            pageData.put("name", res.reference.originalFileName)
            pageData.put("file", OrchidUtils.normalizePath("$resourceRoot/${res.reference.originalFullFileName}"))
            pageData.put("fields", extractor.describeOptions(collection.itemClass, includeOwnOptions, includeInheritedOptions).getNetlifyCmsFields())

            collectionData.getJSONArray("files").put(pageData)
        }
        return true
    }

    override fun getCollections(): List<OrchidCollection<*>>? {
        return null
    }

}

fun isRunningLocally(context: OrchidContext): Boolean {
    return context.taskType == TaskService.TaskType.SERVE
}