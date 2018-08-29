package com.eden.orchid.kotlindoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.helpers.KotlindocInvoker
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Creates a page for each Class and Package in your project, displaying the expected Javadoc information " + "of methods, fields, etc. but in your site's theme.")
class KotlindocGenerator @Inject
constructor(context: OrchidContext, val invoker: KotlindocInvoker) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "kotlindoc"
    }

    @Option
    @StringDefault("../../main/kotlin")
    lateinit var sourceDirs: List<String>

    override fun startIndexing(): List<OrchidPage> {
        val result = invoker.getRootDoc(sourceDirs)

        return emptyList()
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

}