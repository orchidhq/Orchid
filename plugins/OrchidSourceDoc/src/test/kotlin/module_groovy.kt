package com.eden.orchid.sourcedoc

import com.eden.orchid.groovydoc.NewGroovydocGenerator
import com.eden.orchid.strikt.collectionWasCreated
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

fun Assertion.Builder<TestResults>.assertGroovyPages(baseDir: String = "/groovydoc"): Assertion.Builder<TestResults> {
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

fun Assertion.Builder<TestResults>.assertGroovyPages(baseDirs: List<String>): Assertion.Builder<TestResults> {
    return baseDirs.fold(this) { acc, dir ->
        acc.assertGroovyPages("/groovydoc/$dir")
    }
}

fun Assertion.Builder<TestResults>.assertGroovyCollections(baseDirs: List<String> = emptyList()): Assertion.Builder<TestResults> {
    return if(baseDirs.isNotEmpty()) {
        baseDirs.fold(this) { acc, dir ->
            acc
                .collectionWasCreated(NewGroovydocGenerator.GENERATOR_KEY, dir)
                .collectionWasCreated(NewGroovydocGenerator.GENERATOR_KEY, "$dir-classes")
                .collectionWasCreated(NewGroovydocGenerator.GENERATOR_KEY, "$dir-packages")
        }.collectionWasCreated(NewGroovydocGenerator.GENERATOR_KEY, "modules")
    }
    else {
        this
            .collectionWasCreated(NewGroovydocGenerator.GENERATOR_KEY, "modules")
            .collectionWasCreated(NewGroovydocGenerator.GENERATOR_KEY, NewGroovydocGenerator.GENERATOR_KEY)
            .collectionWasCreated(NewGroovydocGenerator.GENERATOR_KEY, "classes")
            .collectionWasCreated(NewGroovydocGenerator.GENERATOR_KEY, "packages")
    }
}
