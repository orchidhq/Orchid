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
) : SourcedocGenerator<SwiftModuleDoc>("swiftdoc", resourcesDir, invoker, extractor) {
    companion object {
        val type = "swift"
        val nodeKinds = listOf("sourceFiles", "classes")
        val otherSourceKinds = emptyList<String>()
    }
}

fun OrchidIntegrationTest.swiftdocSetup(showRunnerLogs: Boolean = false) {
    sourceDocTestSetup(
        NewSwiftdocGenerator.type,
        NewSwiftdocGenerator.nodeKinds,
        NewSwiftdocGenerator.otherSourceKinds,
        showRunnerLogs
    )
}

fun OrchidIntegrationTest.swiftdocSetup(modules: List<String>, showRunnerLogs: Boolean = false) {
    sourceDocTestSetup(
        NewSwiftdocGenerator.type,
        NewSwiftdocGenerator.nodeKinds,
        NewSwiftdocGenerator.otherSourceKinds,
        modules,
        showRunnerLogs
    )
}

fun Assertion.Builder<TestResults>.assertSwift(baseDir: String = "/swiftdoc"): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("$baseDir/index.html") { }
        .pageWasRendered("$baseDir/swiftClass/SwiftClass/index.html") { }
        .pageWasRendered("$baseDir/swiftClass/index.html") { }
        .pageWasRendered("$baseDir/swiftStruct/SwiftStruct/index.html") { }
        .pageWasRendered("$baseDir/swiftStruct/index.html") { }
}

fun Assertion.Builder<TestResults>.assertSwift(baseDirs: List<String>): Assertion.Builder<TestResults> {
    return baseDirs.fold(this) { acc, dir ->
        acc.assertSwift("/swiftdoc/$dir")
    }
}
