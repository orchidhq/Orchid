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

    // Dynamic Component Registration
    api(libs.javax.inject)
    api(libs.guice)
    api(libs.classgraph)

    // core utilities
    api(libs.okhttp)
    api(libs.commons.io)
    api(libs.commons.lang3)
    api(libs.commons.text)
    api(libs.thumbnailator)
    api(libs.jsoup)

    // copper-leaf utilities
    api(libs.copperleaf.common)
    api(libs.copperleaf.clog)
    api(libs.copperleaf.krow)
    api(libs.copperleaf.clog)

    // Included parsers: JSON, YAML, TOML, CSV, Pebble, Markdown, Sass
    api(libs.lang.json)
    implementation(libs.lang.yaml)
    implementation(libs.lang.toml)
    implementation(libs.lang.pebble)
    implementation(libs.lang.scss)
    implementation(libs.lang.csv)

    // Flexmark extensions
    api(libs.flexmark.core)
    implementation(libs.bundles.flexmark)

    // for PDF wiki generation
    implementation(libs.bundles.openhtmltopdf)

    // server
    implementation(libs.bundles.nanohttpd)

    testImplementation(Modules.OrchidTest)
}
