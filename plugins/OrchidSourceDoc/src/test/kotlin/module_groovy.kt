package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.groovy.GroovydocInvokerImpl
import com.copperleaf.kodiak.groovy.models.GroovyModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion
import javax.inject.Inject
import javax.inject.Named

class NewGroovydocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: GroovydocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<GroovyModuleDoc>("groovydoc", resourcesDir, invoker, extractor) {
    companion object {
        val type = "groovy"
        val nodeKinds = listOf("packages", "classes")
        val otherSourceKinds = listOf("java")
    }
}

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
