package com.eden.orchid.kotlindoc

import com.copperleaf.dokka.json.models.KotlinPackageDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.PageCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.helpers.OrchidKotlindocInvoker
import com.eden.orchid.kotlindoc.model.KotlindocModel
import com.eden.orchid.kotlindoc.page.KotlindocClassPage
import com.eden.orchid.kotlindoc.page.KotlindocPackagePage
import java.util.ArrayList
import java.util.HashMap
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Named

@Description(
    "Creates a page for each Class and Package in your project, displaying the expected KDoc information "
            + "of methods, fields, etc. but in your site's theme.",
    name = "Kotlindoc"
)
class KotlindocGenerator
@Inject
constructor(
    context: OrchidContext,
    @Named("kotlindocClasspath") private val kotlindocClasspath: String,
    private val invoker: OrchidKotlindocInvoker,
    private val model: KotlindocModel
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "kotlindoc"
    }

    @Option
    @StringDefault("../../main/kotlin")
    @Description("The source directories with Kotlin files to document.")
    lateinit var sourceDirs: List<String>

    @Option
    @Description("Arbitrary command line arguments to pass through directly to Dokka.")
    lateinit var args: List<String>

    override fun startIndexing(): List<OrchidPage> {
        val args = if (kotlindocClasspath.isNotBlank()) listOf("-classpath", kotlindocClasspath, *args.toTypedArray()) else args
        val rootDoc = invoker.getRootDoc(sourceDirs, args)

        if (rootDoc == null) return ArrayList()

        model.initialize(ArrayList(), ArrayList())

        for (classDoc in rootDoc.classes) {
            model.allClasses.add(KotlindocClassPage(context, classDoc, model))
        }

        val packagePageMap = HashMap<String, KotlindocPackagePage>()
        for (packageDoc in rootDoc.packages) {
            val classesInPackage = ArrayList<KotlindocClassPage>()
            for (classDocPage in model.allClasses) {
                if (classDocPage.classDoc.`package` == packageDoc.qualifiedName) {
                    classesInPackage.add(classDocPage)
                }
            }

            classesInPackage.sortBy { it.title }

            val packagePage = KotlindocPackagePage(context, packageDoc, classesInPackage, model)

            model.allPackages.add(packagePage)
            packagePageMap[packageDoc.qualifiedName] = packagePage
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

    private fun isInnerPackage(parent: KotlinPackageDoc, possibleChild: KotlinPackageDoc): Boolean {

        // packages start the same...
        if (possibleChild.qualifiedName.startsWith(parent.qualifiedName)) {

            // packages are not the same...
            if (possibleChild.qualifiedName != parent.qualifiedName) {

                val parentSegmentCount = parent.qualifiedName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size
                val possibleChildSegmentCount = possibleChild.qualifiedName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size

                // child has one segment more than the parent, so is immediate child
                if (possibleChildSegmentCount == parentSegmentCount + 1) {
                    return true
                }
            }
        }

        return false
    }

    override fun getCollections(pages: List<OrchidPage>): List<OrchidCollection<*>> {
        return listOf(
            PageCollection(this, "classes", model.allClasses),
            PageCollection(this, "packages", model.allPackages)
        )
    }

}