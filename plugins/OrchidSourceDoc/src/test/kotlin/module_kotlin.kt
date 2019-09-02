package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.kotlin.KotlindocInvokerImpl
import com.copperleaf.kodiak.kotlin.models.KotlinModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion
import javax.inject.Inject
import javax.inject.Named

class NewKotlindocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: KotlindocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<KotlinModuleDoc>("kotlindoc", resourcesDir, invoker, extractor)

fun OrchidIntegrationTest.kotlindocSetup(showRunnerLogs: Boolean = false) {
    configObject(
        "kotlindoc",
        """
        |{
        |    "sourceDirs": [
        |        "./../../OrchidJavadoc/src/mockJava",
        |        "./../../OrchidKotlindoc/src/mockKotlin"
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
        |            "module": "kotlindoc",
        |            "node": "packages",
        |            "asSubmenu": true,
        |            "submenuTitle": "Kotlindoc Packages"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "kotlindoc",
        |            "node": "classes",
        |            "asSubmenu": true,
        |            "submenuTitle": "Kotlindoc Classes"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "kotlindoc"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |    ]
        |}
        |""".trimMargin()
    )
}

fun Assertion.Builder<TestResults>.assertKotlin(): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/CustomString/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaAnnotation/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaClass/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaEnumClass/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaExceptionClass/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaInterface/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinAnnotation/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinClass/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinClassWithCompanionObject/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinEnumClass/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinExceptionClass/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinInlineClass/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinInterface/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinObjectClass/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinSealedClass1/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinSealedClass2/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinSealedClass3/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinSealedClasses/index.html") { }
        .pageWasRendered("/kotlindoc/com/eden/orchid/mock/index.html") { }
}
