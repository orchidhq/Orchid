package com.eden.orchid.groovydoc

import com.caseyjbrooks.clog.Clog
import com.copperleaf.groovydoc.json.models.GroovydocPackageDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.PageCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.groovydoc.helpers.OrchidGroovydocInvoker
import com.eden.orchid.groovydoc.models.GroovydocModel
import com.eden.orchid.groovydoc.pages.GroovydocClassPage
import com.eden.orchid.groovydoc.pages.GroovydocPackagePage
import com.eden.orchid.sourcedoc.SourcedocGenerator
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

@Description(
    "Creates a page for each Class and Package in your project, displaying the expected Groovydoc information " +
            "of methods, fields, etc. but in your site's theme.",
    name = "Groovydoc"
)
@Deprecated(SourcedocGenerator.deprecationWarning)
class GroovydocGenerator
@Inject
constructor(
    private val groovydocInvoker: OrchidGroovydocInvoker
) : OrchidGenerator<GroovydocModel>(GENERATOR_KEY, PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "groovydoc"
    }

    @Option
    @StringDefault("../../main/groovy", "../../main/java")
    @Description("The source directories with Kotlin files to document.")
    lateinit var sourceDirs: List<String>

    @Option
    @Description("Arbitrary command line arguments to pass through directly to Groovydoc.")
    lateinit var args: List<String>

    override fun startIndexing(context: OrchidContext): GroovydocModel {
        Clog.w(SourcedocGenerator.deprecationWarning)
        groovydocInvoker.extractOptions(context, allData)

        val rootDoc = groovydocInvoker.getRootDoc(sourceDirs, args)
        if (rootDoc == null) return GroovydocModel(context, emptyList(), emptyList())

        val allClasses = mutableListOf<GroovydocClassPage>()
        val allPackages = mutableListOf<GroovydocPackagePage>()

        for (classDoc in rootDoc.classes) {
            allClasses.add(GroovydocClassPage(context, classDoc))
        }

        val packagePageMap = HashMap<String, GroovydocPackagePage>()
        for (packageDoc in rootDoc.packages) {
            val classesInPackage = ArrayList<GroovydocClassPage>()
            for (classDocPage in allClasses) {
                if (classDocPage.classDoc.`package` == packageDoc.name) {
                    classesInPackage.add(classDocPage)
                }
            }

            classesInPackage.sortBy { it.title }

            val packagePage = GroovydocPackagePage(context, packageDoc, classesInPackage)
            allPackages.add(packagePage)
            packagePageMap[packageDoc.name] = packagePage
        }

        for (packagePage in packagePageMap.values) {
            for (possibleInnerPackage in packagePageMap.values) {
                if (isInnerPackage(packagePage.packageDoc, possibleInnerPackage.packageDoc)) {
                    packagePage.innerPackages.add(possibleInnerPackage)
                }
            }
        }

        for (classDocPage in allClasses) {
            classDocPage.packagePage = packagePageMap[classDocPage.classDoc.`package`]
        }

        return GroovydocModel(context, allClasses, allPackages).also { createdModel ->
            createdModel.allClasses.forEach { it.model = createdModel }
        }
    }

    override fun startGeneration(context: OrchidContext, model: GroovydocModel) {
        model.allPages.forEach { context.renderTemplate(it) }
    }

    override fun getCollections(
        context: OrchidContext,
        model: GroovydocModel
    ): List<OrchidCollection<*>> {
        return listOf(
            PageCollection(this, "classes", model.allClasses),
            PageCollection(this, "packages", model.allPackages)
        )
    }

    private fun isInnerPackage(parent: GroovydocPackageDoc, possibleChild: GroovydocPackageDoc): Boolean {

        // packages start the same...
        if (possibleChild.name.startsWith(parent.name)) {

            // packages are not the same...
            if (possibleChild.name != parent.name) {

                val parentSegmentCount =
                    parent.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size
                val possibleChildSegmentCount =
                    possibleChild.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size

                // child has one segment more than the parent, so is immediate child
                if (possibleChildSegmentCount == parentSegmentCount + 1) {
                    return true
                }
            }
        }

        return false
    }

}
