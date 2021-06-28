package com.eden.orchid.plugindocs.tags

import clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.options.ArchetypeDescription
import com.eden.orchid.api.options.OptionsDescription
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.resolve
import org.apache.commons.lang3.ClassUtils

@Description("Show all options for one of your plugin's classes.", name = "Plugin Documentation")
class PluginDocsTag : TemplateTag("docs", Type.Simple, true) {

    enum class Tabs {
        overview, all_options, own_options, inherited_options, archetypes
    }

    @Option
    @Description("A fully-qualified class name to render options for.")
    lateinit var className: String

    @Option
    @Description("A custom template to use the for tabs tag used internally.")
    lateinit var tabsTemplate: String

    @Option @BooleanDefault(false)
    @Description("A custom template to use the for tabs tag used internally.")
    var admin: Boolean = false

    @Option
    @StringDefault("overview", "own_options", "inherited_options", "archetypes")
    @JvmSuppressWildcards
    lateinit var enabledTabs: List<String>

    val enabledTabsEnums: List<Tabs> get() = enabledTabs
        .mapNotNull { kotlin.runCatching { Tabs.valueOf(it) }.getOrNull() }

    private val optionsExtractor: OptionsExtractor by lazy { context.resolve<OptionsExtractor>() }

    override fun parameters() = arrayOf(::className.name)

    val classType: Class<*> by lazy {
        Class.forName(className)
    }

    fun getClassTypeOverviewTemplate(): String {
        val o = classType
        val classes = ArrayList<String>()

        // first add the class and its superclasses, to make sure that the concrete types get precedence over interfaces
        classes.add("server/description/" + o.name.replace(".", "/"))
        ClassUtils.getAllSuperclasses(o).forEach {
            classes.add("server/description/" + it.name.replace(".", "/"))
        }

        // fall back to interface class templates
        ClassUtils.getAllInterfaces(o).forEach {
            classes.add("server/description/" + it.name.replace(".", "/"))
        }

        return "?" + classes.joinToString(",")
    }

    fun getClassDescription(): String {
        return optionsExtractor.describeOptions(classType, false, false).classDescription
    }

    fun getRelatedCollections(collectionType: String, collectionId: String): List<OrchidCollection<*>> {
        var stream = this.context.collections.filter { it != null }

        if (!EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(collectionId)) {
            stream = stream.filter { collection -> collectionType == collection.collectionType }
        }
        else if (!EdenUtils.isEmpty(collectionType)) {
            stream = stream.filter { collection -> collectionType == collection.collectionType }
        }
        else if (!EdenUtils.isEmpty(collectionId)) {
            val estimatedCollection = stream.firstOrNull()
            if (estimatedCollection != null) {
                stream = stream.filter { collection -> estimatedCollection.collectionType == collection.collectionType }
            }
            else {
                stream = this.context.collections.filter { it != null }
            }
        }

        return stream.sortedBy { it.collectionType }
    }

    fun isTabEnabled(tabName: String): Boolean {
        return enabledTabsEnums.any { it.name == tabName }
    }

    fun hasOptions(): Boolean {
        return hasOwnOptions() || hasInheritedOptions()
    }

    fun getAllOptions(): List<OptionsDescription> {
        return getOwnOptions() + getInheritedOptions()
    }

    fun hasOwnOptions(): Boolean {
        return optionsExtractor.hasOptions(classType, true, false)
    }

    fun getOwnOptions(): List<OptionsDescription> {
        return optionsExtractor.describeOwnOptions(classType).optionsDescriptions
    }

    fun hasInheritedOptions(): Boolean {
        return optionsExtractor.hasOptions(classType, false, true)
    }

    fun getInheritedOptions(): List<OptionsDescription> {
        return optionsExtractor.describeInheritedOptions(classType).optionsDescriptions
    }

    fun hasArchetypes(): Boolean {
        return true
    }

    fun getArchetypes(): List<ArchetypeDescription> {
        return optionsExtractor.describeOptions(classType, false, false).archetypeDescriptions
    }

    private fun getDescriptionLink(o: Any, title: String): String {
        val className = o as? Class<*> ?: o.javaClass

        if(admin) {
            val link = "${context.baseUrl}/admin/describe?className=${className.name}"
            return "<a href=\"${link}\">${title}</a>"
        }
        else {
            val page = context.findPage(null, null, className.name)

            if (page != null) {
                val link = page.link
                return "<a href=\"${link}\">${title}</a>"
            }
        }

        return title
    }

    fun toAnchor(archetype: ArchetypeDescription): String {
        return getDescriptionLink(archetype.archetypeType, archetype.displayName)
    }

    fun toAnchor(option: OptionsDescription): String {
        val baseLink = getDescriptionLink(option.optionType, option.optionType.simpleName)
        val typeParamLinks: String

        if (!EdenUtils.isEmpty(option.optionTypeParameters)) {
            typeParamLinks = option.optionTypeParameters
                    .filterNotNull()
                    .map { getDescriptionLink(it, it.simpleName) }
                    .joinToString(separator = ", ", prefix = "&lt;", postfix = "&gt;")
        }
        else {
            typeParamLinks = ""
        }

        return baseLink + typeParamLinks
    }

    @Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
    fun <T> provide(): T? {
        try {
            return context.resolve(classType) as? T
        }
        catch (e: Exception) {

        }

        return null
    }
}



