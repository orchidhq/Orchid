package com.eden.orchid.sourcedoc

import com.eden.orchid.kotlindoc.NewKotlindocGenerator
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion

fun OrchidIntegrationTest.kotlindocSetup(showRunnerLogs: Boolean = false) {
    sourceDocTestSetup(
        NewKotlindocGenerator.type,
        NewKotlindocGenerator.nodeKinds,
        NewKotlindocGenerator.otherSourceKinds,
        showRunnerLogs
    )
}

fun OrchidIntegrationTest.kotlindocSetup(modules: List<String>, showRunnerLogs: Boolean = false) {
    sourceDocTestSetup(
        NewKotlindocGenerator.type,
        NewKotlindocGenerator.nodeKinds,
        NewKotlindocGenerator.otherSourceKinds,
        modules,
        showRunnerLogs
    )
}

fun Assertion.Builder<TestResults>.assertKotlin(baseDir: String = "/kotlindoc"): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("$baseDir/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/customstring/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaannotation/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaenumclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaexceptionclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javainterface/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinannotation/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinclasswithcompanionobject/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinenumclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinexceptionclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlininlineclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlininterface/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinobjectclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinsealedclass1/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinsealedclass2/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinsealedclass3/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/kotlinsealedclasses/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/index.html") { }
}

fun Assertion.Builder<TestResults>.assertKotlin(baseDirs: List<String>): Assertion.Builder<TestResults> {
    return baseDirs.fold(this) { acc, dir ->
        acc.assertKotlin("/kotlindoc/$dir")
    }
}
