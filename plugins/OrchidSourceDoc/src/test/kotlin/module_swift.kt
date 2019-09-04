package com.eden.orchid.sourcedoc

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.swiftdoc.NewSwiftdocGenerator
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion

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
