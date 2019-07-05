package com.eden.orchid.javadoc

import com.copperleaf.javadoc.json.models.JavaPackageDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.PageCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.helpers.OrchidJavadocInvoker
import com.eden.orchid.javadoc.models.JavadocModel
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage
import java.util.ArrayList
import java.util.HashMap
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Named

@Description(
    "Creates a page for each Class and Package in your project, displaying the expected Javadoc information " +
            "of methods, fields, etc. but in your site's theme.",
    name = "Javadoc"
)
class JavadocGenerator
@Inject
constructor(
    context: OrchidContext,
    @Named("javadocClasspath") private val javadocClasspath: String,
    private val model: JavadocModel,
    private val javadocInvoker: OrchidJavadocInvoker
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "javadoc"
    }

    @Option
    @StringDefault("../../main/java")
    @Description("The source directories with Kotlin files to document.")
    lateinit var sourceDirs: List<String>

    @Option
    @Description("Arbitrary command line arguments to pass through directly to Javadoc.")
    lateinit var args: List<String>

    override fun startIndexing(): List<OrchidPage> {
        val args = if (javadocClasspath.isNotBlank()) listOf("-classpath", javadocClasspath, *args.toTypedArray()) else args

        javadocInvoker.extractOptions(context, allData)

        val rootDoc = javadocInvoker.getRootDoc(sourceDirs, args)

        if (rootDoc == null) return ArrayList()

        model.initialize(ArrayList(), ArrayList())

        for (classDoc in rootDoc.classes) {
            model.allClasses.add(JavadocClassPage(context, classDoc, model))
        }

        val packagePageMap = HashMap<String, JavadocPackagePage>()
        for (packageDoc in rootDoc.packages) {

            val classesInPackage = ArrayList<JavadocClassPage>()
            for (classDocPage in model.allClasses) {
                if (classDocPage.classDoc.`package` == packageDoc.name) {
                    classesInPackage.add(classDocPage)
                }
            }

            classesInPackage.sortBy { it.title }

            val packagePage = JavadocPackagePage(context, packageDoc, classesInPackage)

            model.allPackages.add(packagePage)
            packagePageMap[packageDoc.name] = packagePage
        }

        for (packagePage in packagePageMap.values) {
            for (possibleInnerPackage in packagePageMap.values) {
                if (isInnerPackage(packagePage.packageDoc, possibleInnerPackage.packageDoc)) {
                    packagePage.innerPackages.add(possibleInnerPackage)
                }
            }
        }

        for (classDocPage in model.allClasses) {
            classDocPage.packagePage = packagePageMap[classDocPage.classDoc.`package`]
        }

        return model.allPages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

    override fun getCollections(pages: List<OrchidPage>): List<OrchidCollection<*>> {
        return listOf(
            PageCollection(this, "classes", model.allClasses),
            PageCollection(this, "packages", model.allPackages)
        )
    }

    private fun isInnerPackage(parent: JavaPackageDoc, possibleChild: JavaPackageDoc): Boolean {

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