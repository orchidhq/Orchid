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

    implementation(buildscriptLibs.kotlin)
    implementation(buildscriptLibs.ktlint)
    implementation(buildscriptLibs.licensee)
    implementation(libs.okhttp)

    // small hack from https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 to access these catalogs in
    // precompiled script plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(files(testLibs.javaClass.superclass.protectionDomain.codeSource.location))
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
