package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.groovy.GroovydocInvokerImpl
import com.copperleaf.kodiak.groovy.models.GroovyModuleDoc
import com.copperleaf.kodiak.java.JavadocInvokerImpl
import com.copperleaf.kodiak.java.models.JavaRootDoc
import com.copperleaf.kodiak.kotlin.KotlindocInvokerImpl
import com.copperleaf.kodiak.kotlin.models.KotlinModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import javax.inject.Inject
import javax.inject.Named

class NewJavadocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: JavadocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<JavaRootDoc>("javadoc", resourcesDir, invoker, extractor)

class NewGroovydocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: GroovydocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<GroovyModuleDoc>("groovydoc", resourcesDir, invoker, extractor)

class NewKotlindocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: KotlindocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<KotlinModuleDoc>("kotlindoc", resourcesDir, invoker, extractor)
