import java.io.ByteArrayOutputStream
import java.util.*

plugins {
    `maven-publish`
    signing
}

// MavenCentral Signing and Publishing
// ---------------------------------------------------------------------------------------------------------------------

// taken and modified from https://dev.to/kotlin/how-to-build-and-publish-a-kotlin-multiplatform-library-going-public-4a8k

// Stub secrets to let the project sync and build without the publication values set up
var signingKeyId: String by project.extra
var signingKey: String by project.extra
var signingPassword: String by project.extra
var ossrhUsername: String by project.extra
var ossrhPassword: String by project.extra

// Grabbing secrets from project properties or from environment variables, which could be used on CI
signingKeyId = loadProperty("signing_key_id")
signingKey = loadFileContents("signing_key")
signingPassword = loadProperty("signing_password")
ossrhUsername = loadProperty("ossrh_username")
ossrhPassword = loadProperty("ossrh_password")

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    // Configure maven central repository
    repositories {
        // publish to the project buildDir to make sure things are getting published correctly
        maven(url = "${project.buildDir}/.m2/repository") {
            name = "project"
        }
        maven(url = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "mavenCentral"
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {

        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set(project.name)
            description.set("${project.description}")
            url.set(Config.githubUrl)

            licenses {
                license {
                    name.set(Config.license.name)
                    url.set(Config.license.url)
                }
            }
            developers {
                developer {
                    id.set(Config.Developer.id)
                    name.set(Config.Developer.name)
                    email.set(Config.Developer.email)
                }
            }
            scm {
                url.set("${Config.githubUrl}.git")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
}
