@file:Suppress("UnstableApiUsage")
plugins {
    java
    kotlin("jvm")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
    `orchid-main-projects`
}

dependencies {
    implementation(moduleLibs.core)
    testImplementation(moduleLibs.test)

    implementation(moduleLibs.features.archives)
    implementation(moduleLibs.features.asciidoc)
    implementation(moduleLibs.features.azure)
    implementation(moduleLibs.features.bible)
    implementation(moduleLibs.features.bitbucket)
    implementation(moduleLibs.features.changelog)
    implementation(moduleLibs.features.diagrams)
    implementation(moduleLibs.features.forms)
    implementation(moduleLibs.features.github)
    implementation(moduleLibs.features.gitlab)
    implementation(moduleLibs.features.groovydoc)
    implementation(moduleLibs.features.javadoc)
    implementation(moduleLibs.features.kotlindoc)
    implementation(moduleLibs.features.kss)
    implementation(moduleLibs.features.netlify)
    implementation(moduleLibs.features.netlifyCMS)
    implementation(moduleLibs.features.pages)
    implementation(moduleLibs.features.pluginDocs)
    implementation(moduleLibs.features.posts)
    implementation(moduleLibs.features.presentations)
    implementation(moduleLibs.features.search)
    implementation(moduleLibs.features.snippets)
    implementation(moduleLibs.features.sourceDoc)
    implementation(moduleLibs.features.swagger)
    implementation(moduleLibs.features.swiftdoc)
    implementation(moduleLibs.features.syntaxHighlighter)
    implementation(moduleLibs.features.wiki)
    implementation(moduleLibs.features.writersBlocks)
    implementation(moduleLibs.themes.bsdoc)
    implementation(moduleLibs.themes.copper)
    implementation(moduleLibs.themes.editorial)
    implementation(moduleLibs.themes.futureImperfect)
}
