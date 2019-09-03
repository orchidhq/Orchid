package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.java.JavadocInvokerImpl
import com.copperleaf.kodiak.java.models.JavaRootDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion
import javax.inject.Inject
import javax.inject.Named

class NewJavadocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: JavadocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<JavaRootDoc>("javadoc", resourcesDir, invoker, extractor) {
    companion object {
        val type = "java"
        val nodeKinds = listOf("packages", "classes")
        val otherSourceKinds = emptyList<String>()
    }
}

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
