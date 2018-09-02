package com.eden.orchid.kotlindoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.helpers.KotlindocInvoker
import com.eden.orchid.kotlindoc.model.KotlinClassdoc
import com.eden.orchid.kotlindoc.model.KotlinPackagedoc
import com.eden.orchid.kotlindoc.model.KotlindocModel
import com.eden.orchid.kotlindoc.page.KotlindocClassPage
import com.eden.orchid.kotlindoc.page.KotlindocPackagePage
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Creates a page for each Class and Package in your project, displaying the expected Javadoc information " + "of methods, fields, etc. but in your site's theme.")
class KotlindocGenerator @Inject
constructor(
        context: OrchidContext,
        private val invoker: KotlindocInvoker,
        private val model: KotlindocModel
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "kotlindoc"
    }

    @Option
    @StringDefault("../../main/kotlin")
    lateinit var sourceDirs: List<String>

    override fun startIndexing(): List<OrchidPage> {
        val rootDoc = invoker.getRootDoc(sourceDirs)

        if (rootDoc == null) return ArrayList()

        val classes = HashSet<KotlinClassdoc>()
        val packages = HashSet<KotlinPackagedoc>()

        for (classDoc in rootDoc.classes) {
            classes.add(classDoc)
            packages.add(classDoc.containingPackage)
        }

        model.initialize(ArrayList(), ArrayList())

        for (classDoc in classes) {
            model.allClasses.add(KotlindocClassPage(context, classDoc, model))
        }

        val packagePageMap = HashMap<KotlinPackagedoc, KotlindocPackagePage>()
        for (packageDoc in packages) {
            val classesInPackage = ArrayList<KotlindocClassPage>()
            for (classDocPage in model.allClasses) {
                if (classDocPage.classDoc.containingPackage == packageDoc) {
                    classesInPackage.add(classDocPage)
                }
            }

            classesInPackage.sortBy { it.title }

            val packagePage = KotlindocPackagePage(context, packageDoc, classesInPackage, model)

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
            classDocPage.packagePage = packagePageMap[classDocPage.classDoc.containingPackage]
        }

        return model.allPages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

    private fun isInnerPackage(parent: KotlinPackagedoc, possibleChild: KotlinPackagedoc): Boolean {

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

}