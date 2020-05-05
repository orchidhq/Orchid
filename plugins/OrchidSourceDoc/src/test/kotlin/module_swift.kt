package com.eden.orchid.sourcedoc

import com.eden.orchid.strikt.collectionWasCreated
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.swiftdoc.NewSwiftdocGenerator
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion

fun OrchidIntegrationTest.swiftdocSetup(modules: List<String> = emptyList(), showRunnerLogs: Boolean = false) {
    val type = NewSwiftdocGenerator.type
    val nodeKinds = NewSwiftdocGenerator.nodeKinds
    val otherSourceKinds = NewSwiftdocGenerator.otherSourceKinds

    if(modules.isEmpty()) {
        singleModuleSetup(type, showRunnerLogs, nodeKinds, otherSourceKinds, null)
    }
    else {
        multiModuleSetup(type, modules, showRunnerLogs, nodeKinds, otherSourceKinds)
    }
}

fun Assertion.Builder<TestResults>.assertSwiftPages(baseDir: String = "/swiftdoc"): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("$baseDir/index.html") { }
        .pageWasRendered("$baseDir/privateswiftclass/index.html") { }
        .pageWasRendered("$baseDir/swiftclass/index.html") { }
        .pageWasRendered("$baseDir/swiftclass/swiftclass/index.html") { }
        .pageWasRendered("$baseDir/swiftclasswithprivatemembers/index.html") { }
        .pageWasRendered("$baseDir/swiftclasswithprivatemembers/swiftclasswithprivatemembers/index.html") { }
        .pageWasRendered("$baseDir/swiftclasswithsuppressedmembers/index.html") { }
        .pageWasRendered("$baseDir/swiftclasswithsuppressedmembers/swiftclasswithsuppressedmembers/index.html") { }
        .pageWasRendered("$baseDir/swiftstruct/index.html") { }
        .pageWasRendered("$baseDir/swiftstruct/swiftstruct/index.html") { }
        .pageWasRendered("$baseDir/swiftsuppressedclass/index.html") { }
}

fun Assertion.Builder<TestResults>.assertSwiftPages(baseDirs: List<String>): Assertion.Builder<TestResults> {
    return baseDirs.fold(this) { acc, dir ->
        acc.assertSwiftPages("/swiftdoc/$dir")
    }
}

fun Assertion.Builder<TestResults>.assertSwiftCollections(baseDirs: List<String> = emptyList()): Assertion.Builder<TestResults> {
    return if (baseDirs.isNotEmpty()) {
        baseDirs.fold(this) { acc, dir ->
            acc
                .collectionWasCreated(NewSwiftdocGenerator.GENERATOR_KEY, dir)
                .collectionWasCreated(NewSwiftdocGenerator.GENERATOR_KEY, "$dir-classes")
                .collectionWasCreated(NewSwiftdocGenerator.GENERATOR_KEY, "$dir-sourceFiles")
        }.collectionWasCreated(NewSwiftdocGenerator.GENERATOR_KEY, "modules")
    } else {
        this
            .collectionWasCreated(NewSwiftdocGenerator.GENERATOR_KEY, "modules")
            .collectionWasCreated(NewSwiftdocGenerator.GENERATOR_KEY, NewSwiftdocGenerator.GENERATOR_KEY)
            .collectionWasCreated(NewSwiftdocGenerator.GENERATOR_KEY, "classes")
            .collectionWasCreated(NewSwiftdocGenerator.GENERATOR_KEY, "sourceFiles")
    }
}
