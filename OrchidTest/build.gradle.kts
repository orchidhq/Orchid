apply(from = "$rootDir/gradle/actions/java.gradle")
apply(from = "$rootDir/gradle/actions/kotlin.gradle")
apply(from = "$rootDir/gradle/actions/publish.gradle")
apply(from = "$rootDir/gradle/actions/testing.gradle")
apply(from = "$rootDir/gradle/actions/repositories.gradle")

dependencies {
    "api"(project(":OrchidCore"))

    "api"(Libs.hamcrest_library)
    "api"(Libs.strikt_core)
    "api"(Libs.filepeek) //force to maven central version (> 0.1.2)
    "api"(Libs.mockito_core)
    "api"(Libs.jsoup)
    "api"(Libs.junit_jupiter_api)
    "api"(Libs.junit_jupiter_params)
    "api"(Libs.kotlin_stdlib_jdk8)
    "api"(Libs.kotlinx_html_jvm)

    testImplementation(Modules.OrchidPages)
    "testRuntimeOnly"(Libs.junit_jupiter_engine)
}

