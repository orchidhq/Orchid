import java.io.ByteArrayOutputStream

plugins {
    java
    `kotlin-dsl`
}
apply(from = "${rootProject.rootDir}/../gradle/semver.gradle.kts")

allprojects {
    apply(from = "${rootProject.rootDir}/../gradle/actions/repositories.gradle")
}

group = "com.eden"

tasks.create("publishPlugins") {
    doLast {}
}

dependencies {
    runtime(project(":orchidPlugin"))
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
