package com.eden.orchid.sourcedoc

import com.eden.orchid.javadoc.NewJavadocGenerator
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

fun Assertion.Builder<TestResults>.assertJava(baseDir: String = "/javadoc"): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("$baseDir/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaAnnotation/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaEnumClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaExceptionClass/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/JavaInterface/index.html") { }
        .pageWasRendered("$baseDir/com/eden/orchid/mock/index.html") { }
}

fun Assertion.Builder<TestResults>.assertJava(baseDirs: List<String>): Assertion.Builder<TestResults> {
    return baseDirs.fold(this) { acc, dir ->
        acc.assertJava("/javadoc/$dir")
    }
}
