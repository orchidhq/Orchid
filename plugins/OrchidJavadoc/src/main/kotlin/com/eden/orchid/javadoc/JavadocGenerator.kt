package com.eden.orchid.javadoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.models.JavadocModel
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage
import com.sun.javadoc.ClassDoc
import com.sun.javadoc.PackageDoc
import com.sun.javadoc.RootDoc
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Creates a page for each Class and Package in your project, displaying the expected Javadoc information " + "of methods, fields, etc. but in your site's theme.")
class JavadocGenerator @Inject
constructor(context: OrchidContext, private val rootDoc: RootDoc, private val model: JavadocModel) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "javadoc"
    }

    override fun startIndexing(): List<OrchidPage> {
        val classes = HashSet<ClassDoc>()
        val packages = HashSet<PackageDoc>()

        for (classDoc in rootDoc.classes()) {
            classes.add(classDoc)
            packages.add(classDoc.containingPackage())
        }

        model.initialize(ArrayList(), ArrayList())

        for (classDoc in classes) {
            model.allClasses.add(JavadocClassPage(context, classDoc, model))
        }

        val packagePageMap = HashMap<PackageDoc, JavadocPackagePage>()
        for (packageDoc in packages) {
            val classesInPackage = ArrayList<JavadocClassPage>()
            for (classDocPage in model.allClasses) {
                if (classDocPage.classDoc.containingPackage() == packageDoc) {
                    classesInPackage.add(classDocPage)
                }
            }

            classesInPackage.sortBy { it.title }

            val packagePage = JavadocPackagePage(context, packageDoc, classesInPackage)

            model.allPackages.add(packagePage)
            packagePageMap[packageDoc] = packagePage
        }

        for (packagePage in packagePageMap.values) {
            for (possibleInnerPackage in packagePageMap.values) {
                if (isInnerPackage(packagePage.packageDoc, possibleInnerPackage.packageDoc)) {
                    packagePage.innerPackages.add(possibleInnerPackage)
                }
            }
        }

        for (classDocPage in model.allClasses) {
            classDocPage.packagePage = packagePageMap[classDocPage.classDoc.containingPackage()]
        }

        return model.allPages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach({ context.renderTemplate(it) })
    }

    override fun getCollections(): List<OrchidCollection<*>>? {
        val collections = ArrayList<OrchidCollection<*>>()

        collections.add(JavadocCollection(this, "classes", model.allClasses))
        collections.add(JavadocCollection(this, "packages", model.allPackages))
        collections.add(ExternalJavadocCollection(context, this))

        return collections
    }

    private fun isInnerPackage(parent: PackageDoc, possibleChild: PackageDoc): Boolean {

        // packages start the same...
        if (possibleChild.name().startsWith(parent.name())) {

            // packages are not the same...
            if (possibleChild.name() != parent.name()) {

                val parentSegmentCount = parent.name().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size
                val possibleChildSegmentCount = possibleChild.name().split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size

                // child has one segment more than the parent, so is immediate child
                if (possibleChildSegmentCount == parentSegmentCount + 1) {
                    return true
                }
            }
        }

        return false
    }

}