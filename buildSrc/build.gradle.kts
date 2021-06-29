plugins {
    java
    `kotlin-dsl`
}
group = "com.eden"

tasks.create("publishPlugins") {
    doLast {}
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    runtime(project(":orchidPlugin"))

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.0.0")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
