package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.swift.SwiftdocInvokerImpl
import com.copperleaf.kodiak.swift.models.SwiftModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion
import javax.inject.Inject
import javax.inject.Named

class NewSwiftdocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: SwiftdocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<SwiftModuleDoc>("swiftdoc", resourcesDir, invoker, extractor)

fun OrchidIntegrationTest.swiftdocSetup(showRunnerLogs: Boolean = false) {
    configObject(
        "swiftdoc",
        """
        |{
        |    "sourceDirs": [
        |        "./../../OrchidSwiftdoc/src/mockSwift"
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
        |            "module": "swiftdoc",
        |            "node": "sourceFiles",
        |            "asSubmenu": true,
        |            "submenuTitle": "Swiftdoc Source Files"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "swiftdoc",
        |            "node": "classes",
        |            "asSubmenu": true,
        |            "submenuTitle": "Swiftdoc Classes"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "swiftdoc"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |    ]
        |}
        |""".trimMargin()
    )
}

fun Assertion.Builder<TestResults>.assertSwift(): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("/swiftdoc/swiftClass/SwiftClass/index.html") { }
        .pageWasRendered("/swiftdoc/swiftClass/index.html") { }
        .pageWasRendered("/swiftdoc/swiftStruct/SwiftStruct/index.html") { }
        .pageWasRendered("/swiftdoc/swiftStruct/index.html") { }
}
