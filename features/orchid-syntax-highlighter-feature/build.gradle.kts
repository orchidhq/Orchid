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

    implementation("org.python:jython-standalone:2.7.2")
    implementation("org.pygments:pygments:2.5.2")
}
