import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.text.SimpleDateFormat
import java.util.Date
import org.gradle.accessors.dm.LibrariesForTestLibs

plugins {
    idea
    java
    kotlin("jvm")
    jacoco
}

val testLibs = the<LibrariesForTestLibs>()
dependencies {
    testImplementation(testLibs.bundles.all)
    testRuntimeOnly(testLibs.junit.jupiter.engine)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = Config.javaVersion
    targetCompatibility = Config.javaVersion
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.useIR = true
    kotlinOptions {
        jvmTarget = Config.javaVersion
        freeCompilerArgs = listOf("-Xjvm-default=compatibility", "-Xopt-in=kotlin.RequiresOptIn")
    }
}

tasks.withType<Jar>() {
    manifest {
        val projectVersion = Config.projectVersion(project)
        attributes(
            "Built-By" to System.getProperty("user.name"),
            "Build-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date()),
            "Build-Revision" to projectVersion.sha,
            "Created-By" to "Gradle ${gradle.gradleVersion}",
            "Build-Jdk" to "${System.getProperty("java.version")} (${System.getProperty("java.vendor")} ${System.getProperty("java.vm.version")})",
            "Build-OS" to "${System.getProperty("os.name")} ${System.getProperty("os.arch")} ${System.getProperty("os.version")}",
            "Name" to "${project.name}",
            "Plugin-Version" to "${project.version}",
            "Bundle-License" to Config.license.spdxIdentifier,
            "Bundle-DocURL" to "https://orchid.run"
        )
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

val javadoc by tasks
javadoc.onlyIf { false }

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
    }
    reports {
        junitXml.required.set(true)
        html.required.set(true)
    }
}
tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
val check by tasks
val jacocoTestReport by tasks
check.dependsOn(jacocoTestReport)

val checkForExtensionFile by tasks.registering {
    doLast {
        if(!file("${project.projectDir}/README.md").exists()) {
            error("Project ${project.name} needs a README.")
        }
    }
}
check.dependsOn(checkForExtensionFile)
