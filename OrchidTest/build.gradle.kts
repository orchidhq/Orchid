apply(from = "$rootDir/gradle/actions/java.gradle")
apply(from = "$rootDir/gradle/actions/kotlin.gradle")
apply(from = "$rootDir/gradle/actions/publish.gradle")
apply(from = "$rootDir/gradle/actions/testing.gradle")

dependencies {
    "api"(project(":OrchidCore"))

    "api"(Libs.hamcrest_library)
    "api"(Libs.strikt_core)
    "api"(Libs.mockito_core)
    "api"(Libs.jsoup)
    "api"(Libs.junit_jupiter_api)
    "api"(Libs.junit_jupiter_params)
    "api"(Libs.kotlin_stdlib_jdk8)
    "api"(Libs.kotlinx_html_jvm)

    testImplementation(Module.OrchidPages)
    "testRuntimeOnly"(Libs.junit_jupiter_engine)
}

