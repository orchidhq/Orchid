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
        .pageWasRendered("$baseDir/com/eden/orchid/mock/GroovyAnnotation/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/GroovyClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/GroovyEnumClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/GroovyExceptionClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/GroovyInterface/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/GroovyTrait/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaAnnotation/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaEnumClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaExceptionClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaInterface/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/index.html") { }
}

fun Assertion.Builder<TestResults>.assertGroovy(baseDirs: List<String>): Assertion.Builder<TestResults> {
    return baseDirs.fold(this) { acc, dir ->
        acc.assertGroovy("/groovydoc/$dir")
    }
}
