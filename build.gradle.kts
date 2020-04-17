// Configure build, dependencies, output jar, and Java compatibility
//------------------------------------------------------------------------------
buildscript {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
        jcenter()
    }
    dependencies {
        classpath(Libs.gradle_bintray_plugin)
        classpath(Libs.kotlin_gradle_plugin)
    }
}

plugins {
    base
    jacoco
    id("de.fayard.buildSrcVersions") version "0.7.0"
}
apply(from = "$rootDir/gradle/semver.gradle.kts")

//val delegate: org.gradle.kotlin.dsl.support.delegates.ProjectDelegate = this
group = "io.github.javaeden.orchid"

buildSrcVersions {
    versionsOnlyMode = null
    versionsOnlyFile = null
}

// Add check to make sure every release version has a Changelog file
//----------------------------------------------------------------------------------------------------------------------
val check by tasks
check.dependsOn("checkForChangelogFile")

val archiveDocumentation by tasks.registering(Zip::class) {
    from("${project(":OrchidCore").buildDir}/docs/orchid")
//    from(project.version.toFile())

    include("**/*")

    archiveFileName.set("docs-${project.version}.zip")
    destinationDirectory.set(file("$buildDir/orchid/archives"))
}

val publish by tasks.registering {
    dependsOn(archiveDocumentation)
}

// Code Coverage Reports
//----------------------------------------------------------------------------------------------------------------------

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

val codacy: Configuration by configurations.creating
dependencies {
    codacy(Libs.codacy_coverage_reporter)
}

val codeCoverageReport by tasks.registering(JacocoReport::class) {
    dependsOn(*ModuleGroups.all.tasksNamed("test"))
    executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))
    sourceSets(*ModuleGroups.all(project).sourceSetsNamed("main"))

    reports {
        xml.isEnabled = true
        html.isEnabled = false
        csv.isEnabled = false
    }
}

val sendCoverageToCodacy by tasks.registering(JavaExec::class) {
    dependsOn(codeCoverageReport)
    main = "com.codacy.CodacyCoverageReporter"
    classpath = codacy

    args = listOf(
        "report",
        "-l",
        "Java",
        "-r",
        "${buildDir}/reports/jacoco/codeCoverageReport/codeCoverageReport.xml"
    )
}
