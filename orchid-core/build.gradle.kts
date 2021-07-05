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
    api("javax.inject:javax.inject:1")
    api("com.google.inject:guice:5.0.1")
    api("io.github.classgraph:classgraph:4.8.109")

    // core utilities
    api("com.squareup.okhttp3:okhttp:4.9.1")
    api("commons-io:commons-io:2.10.0")
    api("org.apache.commons:commons-lang3:3.12.0")
    api("org.apache.commons:commons-text:1.9")
    api("net.coobird:thumbnailator:0.4.14")
    api("org.jsoup:jsoup:1.13.1")

    // copper-leaf utilities
    api("io.github.copper-leaf:common-core:3.0.0")
    api("io.github.copper-leaf:clog-core:4.1.1")
    api("io.github.copper-leaf:krow-core:1.0.0")
    api("io.github.copper-leaf:thistle-core:1.1.0")

    // validation
    api("javax.validation:validation-api:2.0.1.Final")
    implementation("org.hibernate.validator:hibernate-validator:6.1.5.Final")
    implementation("org.glassfish:javax.el:3.0.1-b11")

    // Included parsers: JSON, YAML, TOML, CSV, Pebble, Markdown, Sass
    api("org.json:json:20210307")
    implementation("org.yaml:snakeyaml:1.29")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("io.pebbletemplates:pebble:3.1.5")
    implementation("io.bit3:jsass:5.10.4")
    implementation("com.univocity:univocity-parsers:2.9.1")

    // Flexmark extensions
    api("com.vladsch.flexmark:flexmark:0.62.2")
    implementation("com.vladsch.flexmark:flexmark-ext-aside:0.62.2")
    implementation("com.vladsch.flexmark:flexmark-ext-attributes:0.62.2")
    implementation("com.vladsch.flexmark:flexmark-ext-enumerated-reference:0.62.2")
    implementation("com.vladsch.flexmark:flexmark-ext-gfm-tasklist:0.62.2")
    implementation("com.vladsch.flexmark:flexmark-ext-toc:0.62.2")
    implementation("com.vladsch.flexmark:flexmark-ext-anchorlink:0.62.2")

    // server
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation("org.nanohttpd:nanohttpd-websocket:2.3.1")

    testImplementation(Modules.OrchidTest)
}
