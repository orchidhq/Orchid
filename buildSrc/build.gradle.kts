import java.io.ByteArrayOutputStream

plugins {
    java
    `kotlin-dsl`
}
apply(from = "../gradle/semver.gradle.kts")

allprojects {
    repositories {
        jcenter()
    }
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
