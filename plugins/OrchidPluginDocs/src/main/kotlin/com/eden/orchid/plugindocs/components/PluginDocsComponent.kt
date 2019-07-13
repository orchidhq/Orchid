package com.eden.orchid.plugindocs.components

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Descriptive
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import io.github.classgraph.ClassGraph
import javax.inject.Inject

@Description("Show all options for your plugin's classes.", name = "Plugin Documentation")
class PluginDocsComponent : OrchidComponent("pluginDocs", 25) {

    @Option
    @Description("A list of fully-qualified class names to render options for.")
    var classNames = emptyArray<String>()

    @Option
    @Description("A list of fully-qualified package names. All OptionsHolder classes in these packages will have their " +
            "options displayed."
    )
    var packageNames = emptyArray<String>()

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

    fun getClassList(): Set<String> {
        val classList = emptyList<String>().toMutableSet()

        if (!EdenUtils.isEmpty(classNames)) {
            addClassNames(classList)
        }

        if (!EdenUtils.isEmpty(packageNames)) {
            addPackageClasses(classList)
        }

        return classList
    }

    private fun addClassNames(classList: MutableSet<String>) {
        classNames.forEach {
            if (isOptionsHolderClass(it)) {
                classList.add(it)
            }
        }
    }

    private fun addPackageClasses(classList: MutableSet<String>) {
        ClassGraph()
                .enableClassInfo()
                .whitelistPackages(*packageNames)
                .scan()
                .allStandardClasses
                .loadClasses()
                .forEach { matchingClass ->
                    if (isOptionsHolderClass(matchingClass)) {
                        classList.add(matchingClass.name)
                    }
                }
    }

    private fun isOptionsHolderClass(clazz: Class<*>): Boolean {
        return OptionsHolder::class.java.isAssignableFrom(clazz)
    }

    private fun isOptionsHolderClass(clazz: String): Boolean {
        try {
            return OptionsHolder::class.java.isAssignableFrom(Class.forName(clazz))
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun getDescriptiveName(o: Any): String {
        if(o is Class<*>) {
            return Descriptive.getDescriptiveName(o)
        }
        else if(o is String) {
            try {
                return Descriptive.getDescriptiveName(Class.forName(o))
            }
            catch (e: Exception) {
                return o.toString()
            }
        }
        else {
            return Descriptive.getDescriptiveName(o.javaClass)
        }
    }

    fun getDescription(o: Any): String {
        if(o is Class<*>) {
            return Descriptive.getDescription(o)
        }
        else if(o is String) {
            try {
                return Descriptive.getDescription(Class.forName(o))
            }
            catch (e: Exception) {
                return o.toString()
            }
        }
        else {
            return Descriptive.getDescription(o.javaClass)
        }
    }

    fun getDescriptionSummary(o: Any): String {
        if(o is Class<*>) {
            return Descriptive.getDescriptionSummary(o)
        }
        else if(o is String) {
            try {
                return Descriptive.getDescriptionSummary(Class.forName(o))
            }
            catch (e: Exception) {
                return o.toString()
            }
        }
        else {
            return Descriptive.getDescriptionSummary(o.javaClass)
        }
    }

}



