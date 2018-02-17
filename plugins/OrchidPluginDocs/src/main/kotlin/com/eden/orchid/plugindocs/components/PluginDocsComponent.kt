package com.eden.orchid.plugindocs.components

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import javax.inject.Inject

class PluginDocsComponent
@Inject constructor(context: OrchidContext)
    : OrchidComponent(context, "pluginDocs", 25) {

    @Option
    @Description("A list of fully-qualified class names to render options for.")
    var classNames = emptyArray<String>()

    @Option
    @Description("A list of fully-qualified package names. All OptionsHolder classes in these packages will have their " +
            "options displayed."
    )
    var packageNames = emptyArray<String>()

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
        val scanner = FastClasspathScanner(*packageNames)
        scanner.strictWhitelist().matchAllStandardClasses { matchingClass ->
            if(isOptionsHolderClass(matchingClass)) {
                classList.add(matchingClass.name)
            }
        }
        scanner.scan()
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

}



