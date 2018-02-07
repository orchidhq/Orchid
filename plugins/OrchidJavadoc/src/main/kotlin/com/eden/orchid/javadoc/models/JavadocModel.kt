package com.eden.orchid.javadoc.models

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JavadocModel
@Inject
constructor(val context: OrchidContext) {

    var allClasses: MutableList<JavadocClassPage> = ArrayList()
    var allPackages: MutableList<JavadocPackagePage> = ArrayList()

    private var classPageCache: MutableMap<String, OrchidPage?> = HashMap()
    private var packagePageCache: MutableMap<String, OrchidPage?> = HashMap()

    val allPages: List<OrchidPage>
        get() {
            val pages = ArrayList<OrchidPage>()
            pages.addAll(allClasses)
            pages.addAll(allPackages)

            return pages
        }

    fun initialize(allClasses: MutableList<JavadocClassPage>, allPackages: MutableList<JavadocPackagePage>) {
        this.allClasses = allClasses
        this.allPackages = allPackages
        this.classPageCache = HashMap()
        this.packagePageCache = HashMap()
    }

    fun getPackagePage(packageName: String): OrchidPage? {
        if (packagePageCache.containsKey(packageName)) {
            return packagePageCache[packageName]
        } else {
            for (packagePage in allPackages) {
                if (packagePage.packageDoc.name() == packageName) {
                    packagePageCache[packageName] = packagePage
                    return packagePage
                }
            }
        }

        // Find the page in an external page that has been loaded to Orchid's external index
        val page = context.externalIndex.findPage(packageName.replace("\\.".toRegex(), "/"))
        if (page != null) {
            classPageCache[packageName] = page
            return page
        }

        // The page cannot be found, cache the fact that it wasn't found.
        classPageCache[packageName] = null
        return null
    }

    fun getClassPage(className: String): OrchidPage? {
        // we've already identified the page for the class, return it now. It may be null.
        if (classPageCache.containsKey(className)) {
            return classPageCache[className]
        }

        // Find the page in one of our own Javadoc pages. Here, we may use just the simple class name for simplicity
        for (classPage in allClasses) {
            if (classPage.classDoc.qualifiedName() == className) {
                classPageCache[className] = classPage
                return classPage
            } else if (classPage.classDoc.name() == className) {
                classPageCache[className] = classPage
                return classPage
            }
        }

        // Find the page in an external page that has been loaded to Orchid's external index
        val page = context.externalIndex.findPage(className.replace("\\.".toRegex(), "/"))
        if (page != null) {
            classPageCache[className] = page
            return page
        }

        // The page cannot be found, cache the fact that it wasn't found.
        classPageCache[className] = null
        return null
    }

}
