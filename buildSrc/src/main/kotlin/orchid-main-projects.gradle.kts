import org.gradle.kotlin.dsl.apply

plugins {
    idea
    java
}

apply(from = "$rootDir/gradle/actions/kotlin.gradle")
apply(from = "$rootDir/gradle/actions/testing.gradle")

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
