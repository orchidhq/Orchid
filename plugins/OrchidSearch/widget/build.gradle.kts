plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization") version "1.3.41"
    id("kotlin-dce-js")
}

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "https://dl.bintray.com/kotlin/kotlinx.html")
}

group = "io.github.javaeden.orchid"

kotlin {
    js {
        browser {
            webpackTask {
                sourceMaps = false
            }
            testTask {
                useKarma {
                    useNodeJs()
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets["jsMain"].dependencies {
        implementation(kotlin("stdlib-js"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.1.0")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.11.0")
        implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.6.12")

        implementation(npm("lunr", "2.3.6"))
    }
}

tasks.getByName("assemble").dependsOn("runDceJsKotlin")