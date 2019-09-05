package com.eden.orchid.sourcedoc

import com.eden.orchid.groovydoc.NewGroovydocGenerator
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion

fun OrchidIntegrationTest.groovydocSetup(showRunnerLogs: Boolean = false) {
    sourceDocTestSetup(
        NewGroovydocGenerator.type,
        NewGroovydocGenerator.nodeKinds,
        NewGroovydocGenerator.otherSourceKinds,
        showRunnerLogs
    )
}

fun OrchidIntegrationTest.groovydocSetup(modules: List<String>, showRunnerLogs: Boolean = false) {
    sourceDocTestSetup(
        NewGroovydocGenerator.type,
        NewGroovydocGenerator.nodeKinds,
        NewGroovydocGenerator.otherSourceKinds,
        modules,
        showRunnerLogs
    )
}

fun Assertion.Builder<TestResults>.assertGroovy(baseDir: String = "/groovydoc"): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("$baseDir/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/groovyannotation/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/groovyclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/groovyenumclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/groovyexceptionclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/groovyinterface/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/groovytrait/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaannotation/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaenumclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaexceptionclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javainterface/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/index.html") { }
}

fun Assertion.Builder<TestResults>.assertGroovy(baseDirs: List<String>): Assertion.Builder<TestResults> {
    return baseDirs.fold(this) { acc, dir ->
        acc.assertGroovy("/groovydoc/$dir")
    }
}
