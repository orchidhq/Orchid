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
    implementation(Modules.OrchidCore)
    testImplementation(Modules.OrchidTest)

    implementation("net.sourceforge.plantuml:plantuml:1.2021.7")
}
