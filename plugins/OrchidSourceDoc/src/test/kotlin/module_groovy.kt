package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.groovy.GroovydocInvokerImpl
import com.copperleaf.kodiak.groovy.models.GroovyModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion
import javax.inject.Inject
import javax.inject.Named

class NewGroovydocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: GroovydocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<GroovyModuleDoc>("groovydoc", resourcesDir, invoker, extractor)

fun OrchidIntegrationTest.groovydocSetup(showRunnerLogs: Boolean = false) {
    configObject(
        "groovydoc",
        """
        |{
        |    "sourceDirs": [
        |        "./../../OrchidJavadoc/src/mockJava",
        |        "./../../OrchidGroovydoc/src/mockGroovy",
        |    ],
        |    "showRunnerLogs": $showRunnerLogs
        |}
        |""".trimMargin()
    )
    configObject(
        "theme",
        """
        |{
        |    "menu": [
        |        {
        |            "type": "sourcedocPages",
        |            "module": "groovydoc",
        |            "node": "packages",
        |            "asSubmenu": true,
        |            "submenuTitle": "Groovydoc Packages"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "groovydoc",
        |            "node": "classes",
        |            "asSubmenu": true,
        |            "submenuTitle": "Groovydoc Classes"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "groovydoc"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |    ]
        |}
        |""".trimMargin()
    )
}

fun Assertion.Builder<TestResults>.assertGroovy(): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyAnnotation/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyClass/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyEnumClass/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyExceptionClass/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyInterface/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyTrait/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaAnnotation/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaClass/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaEnumClass/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaExceptionClass/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaInterface/index.html") { }
        .pageWasRendered("/groovydoc/com/eden/orchid/mock/index.html") { }
}
