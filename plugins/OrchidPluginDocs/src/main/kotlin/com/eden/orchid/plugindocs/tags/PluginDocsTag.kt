package com.eden.orchid.plugindocs.tags

import com.caseyjbrooks.clog.Clog
import com.copperleaf.krow.KrowTable
import com.copperleaf.krow.formatters.html.DefaultHtmlAttributes
import com.copperleaf.krow.formatters.html.HtmlAttributes
import com.copperleaf.krow.formatters.html.HtmlTableFormatter
import com.copperleaf.krow.krow
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.options.OptionHolderDescription
import com.eden.orchid.api.options.OptionsDescription
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.utilities.SuppressedWarnings
import org.apache.commons.lang3.ClassUtils
import javax.inject.Inject

@Description("Show all options for one of your plugin's classes.", name = "Plugin Documentation")
class PluginDocsTag
@Inject
constructor(
        val optionsExtractor: OptionsExtractor
) : TemplateTag("docs", TemplateTag.Type.Simple, true) {

    @Option
    @Description("A fully-qualified class name to render options for.")
    lateinit var className: String

    @Option
    @Description("A fully-qualified class name to render options for.")
    lateinit var tableClass: String

    @Option
    @Description("A fully-qualified class name to render options for.")
    lateinit var tableHeaderClass: String

    @Option
    @Description("A fully-qualified class name to render options for.")
    lateinit var tableLeaderClass: String

    @Option
    @Description("A custom template to use the for tabs tag used internally.")
    lateinit var tabsTemplate: String

    @Option @BooleanDefault(false)
    @Description("A custom template to use the for tabs tag used internally.")
    var admin: Boolean = false

    override fun parameters(): Array<String> {
        return arrayOf("className")
    }

    fun findClass(): Class<*> {
        return Class.forName(className)
    }

    val classType: Class<*> get() = findClass()

    fun getClassTypeOverviewTemplate(): String {
        val o = findClass()
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
        val classDescripton = optionsExtractor.describeOptions(findClass(), false, false)
        return context.compile("md", classDescripton.classDescription)
    }

    fun getOwnOptionsDescription(): String {
        if(optionsExtractor.hasOptions(findClass(), true, false)) {
            val classDescripton = optionsExtractor.describeOptions(findClass(), true, false)
            val table = getDescriptionTable(classDescripton)
            return table.print(HtmlTableFormatter(tableAttrs))
        }
        else {
            return "No options"
        }
    }

    fun getInheritedOptionsDescription(): String {
        if(optionsExtractor.hasOptions(findClass(), false, true)) {
            val classDescripton = optionsExtractor.describeOptions(findClass(), false, true)
            val table = getDescriptionTable(classDescripton)
            return table.print(HtmlTableFormatter(tableAttrs))
        }
        else {
            return "No options"
        }
    }

    fun getArchetypesDescription(): String {
        val classDescription = optionsExtractor.describeOptions(findClass(), false, false)
        if(classDescription.archetypeDescriptions.isNotEmpty()) {
            val table = krow {
                classDescription.archetypeDescriptions.forEachIndexed { i, option ->
                    cell("Key", "$i") {
                        content = "<code>${option.key}</code>"
                    }
                    cell("Type", "$i") {
                        content = getDescriptionLink(option.archetypeType, option.displayName)
                    }
                    cell("Description", "$i") {
                        content = context.compile("md", option.description)
                    }
                }
            }
            return table.print(HtmlTableFormatter(tableAttrs))
        }
        else {
            return "No archetypes"
        }
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

    private fun getDescriptionTable(optionsHolderDescription: OptionHolderDescription): KrowTable {
        return krow {
            optionsHolderDescription.optionsDescriptions.forEach { option ->
                cell("Key", option.key) {
                    content = "<code>${option.key}</code>"
                }
                cell("Type", option.key) {
                    content = toAnchor(option)
                }
                cell("Default Value", option.key) {
                    content = option.defaultValue
                }
                cell("Description", option.key) {
                    content = context.compile("md", option.description)
                }
            }
        }
    }

    private fun getDescriptionLink(o: Any, title: String): String {
        val className = o as? Class<*> ?: o.javaClass

        if(admin) {
            val link = "${context.baseUrl}/admin/describe?className=${className.name}"
            return Clog.format("<a href=\"#{$1}\">#{$2}</a>", link, title)
        }
        else {
            val page = context.findPage(null, null, className.name)

            if (page != null) {
                val link = page.link
                return Clog.format("<a href=\"#{$1}\">#{$2}</a>", link, title)
            }
        }

        return title
    }

    private fun toAnchor(option: OptionsDescription): String {
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

    val tableAttrs: HtmlAttributes
        get() {
            return object : DefaultHtmlAttributes() {
                override val tableClass: String get() = this@PluginDocsTag.tableClass
                override val leaderClass: String get() = tableLeaderClass
                override val headerClass: String get() = tableHeaderClass
            }
        }

    @Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
    fun <T> provide(): T? {
        try {
            return context.resolve(findClass()) as? T
        }
        catch (e: Exception) {

        }

        return null
    }


}



