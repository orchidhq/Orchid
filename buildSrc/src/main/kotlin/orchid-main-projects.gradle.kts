import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    idea
    java
    kotlin("jvm")
    jacoco
}

dependencies {
    testImplementation("org.hamcrest:hamcrest-library:2.2") // remove this
    testImplementation("io.strikt:strikt-core:0.31.0")
    testImplementation("org.mockito:mockito-core:3.3.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
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
//        attributes(
//            "Built-By": System.properties["user.name"],
//            "Build-Timestamp": new java.text.SimpleDateFormat("yyyy-MM-dd"T"HH:mm:ss.SSSZ").format(new Date()),
//            "Build-Revision": project.version.sha,
//            "Created-By": "Gradle ${gradle.gradleVersion}",
//            "Build-Jdk": "${System.properties["java.version"]} (${System.properties["java.vendor"]} ${System.properties["java.vm.version"]})",
//            "Build-OS": "${System.properties["os.name"]} ${System.properties["os.arch"]} ${System.properties["os.version"]}",
//            "Name": "${project.name}",
//            "Plugin-Version": "${project.version}",
//            "Bundle-License": "LGPL-3.0",
//            "Bundle-DocURL": "https://orchid.run"
//        )
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
        junitXml.isEnabled = true
        html.isEnabled = true
    }
}
tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
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
