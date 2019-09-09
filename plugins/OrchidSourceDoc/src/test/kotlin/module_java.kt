package com.eden.orchid.sourcedoc

import com.eden.orchid.javadoc.NewJavadocGenerator
import com.eden.orchid.strikt.collectionWasCreated
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion

fun OrchidIntegrationTest.javadocSetup(showRunnerLogs: Boolean = false) {
    sourceDocTestSetup(
        NewJavadocGenerator.type,
        NewJavadocGenerator.nodeKinds,
        NewJavadocGenerator.otherSourceKinds,
        showRunnerLogs
    )
}

fun OrchidIntegrationTest.javadocSetup(modules: List<String>, showRunnerLogs: Boolean = false) {
    sourceDocTestSetup(
        NewJavadocGenerator.type,
        NewJavadocGenerator.nodeKinds,
        NewJavadocGenerator.otherSourceKinds,
        modules,
        showRunnerLogs
    )
}

fun Assertion.Builder<TestResults>.assertJavaPages(baseDir: String = "/javadoc"): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("$baseDir/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaannotation/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaenumclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javaexceptionclass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/javainterface/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/index.html") { }
}

fun Assertion.Builder<TestResults>.assertJavaPages(baseDirs: List<String>): Assertion.Builder<TestResults> {
    return baseDirs.fold(this) { acc, dir ->
        acc.assertJavaPages("/javadoc/$dir")
    }
}

fun Assertion.Builder<TestResults>.assertJavaCollections(baseDirs: List<String> = emptyList()): Assertion.Builder<TestResults> {
    return if(baseDirs.isNotEmpty()) {
        baseDirs.fold(this) { acc, dir ->
            acc
                .collectionWasCreated(NewJavadocGenerator.GENERATOR_KEY, dir)
                .collectionWasCreated(NewJavadocGenerator.GENERATOR_KEY, "$dir-classes")
                .collectionWasCreated(NewJavadocGenerator.GENERATOR_KEY, "$dir-packages")
        }.collectionWasCreated(NewJavadocGenerator.GENERATOR_KEY, "modules")
    }
    else {
        this
            .collectionWasCreated(NewJavadocGenerator.GENERATOR_KEY, "modules")
            .collectionWasCreated(NewJavadocGenerator.GENERATOR_KEY, NewJavadocGenerator.GENERATOR_KEY)
            .collectionWasCreated(NewJavadocGenerator.GENERATOR_KEY, "classes")
            .collectionWasCreated(NewJavadocGenerator.GENERATOR_KEY, "packages")
    }
}
