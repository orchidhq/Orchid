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
    runtimeOnly(project(":orchidPlugin"))

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.1.0")
    implementation("app.cash.licensee:licensee-gradle-plugin:1.1.0")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
