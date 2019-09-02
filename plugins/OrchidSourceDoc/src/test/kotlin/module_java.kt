package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.java.JavadocInvokerImpl
import com.copperleaf.kodiak.java.models.JavaRootDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion
import javax.inject.Inject
import javax.inject.Named

class NewJavadocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: JavadocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<JavaRootDoc>("javadoc", resourcesDir, invoker, extractor)

fun OrchidIntegrationTest.javadocSetup(showRunnerLogs: Boolean = false) {
    configObject(
        "javadoc",
        """
        |{
        |    "sourceDirs": [
        |        "./../../OrchidJavadoc/src/mockJava"
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
        |            "module": "javadoc",
        |            "node": "packages",
        |            "asSubmenu": true,
        |            "submenuTitle": "Javadoc Packages"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "javadoc",
        |            "node": "classes",
        |            "asSubmenu": true,
        |            "submenuTitle": "Javadoc Classes"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "javadoc"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |    ]
        |}
        |""".trimMargin()
    )
}

fun Assertion.Builder<TestResults>.assertJava(): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("/javadoc/com/eden/orchid/mock/JavaAnnotation/index.html") { }
        .pageWasRendered("/javadoc/com/eden/orchid/mock/JavaClass/index.html") { }
        .pageWasRendered("/javadoc/com/eden/orchid/mock/JavaEnumClass/index.html") { }
        .pageWasRendered("/javadoc/com/eden/orchid/mock/JavaExceptionClass/index.html") { }
        .pageWasRendered("/javadoc/com/eden/orchid/mock/JavaInterface/index.html") { }
        .pageWasRendered("/javadoc/com/eden/orchid/mock/index.html") { }
}
