plugins {
    `maven-publish`
    signing
}

// MavenCentral Signing and Publishing
// ---------------------------------------------------------------------------------------------------------------------

// taken and modified from https://dev.to/kotlin/how-to-build-and-publish-a-kotlin-multiplatform-library-going-public-4a8k

// Stub secrets to let the project sync and build without the publication values set up
val publishConfiguration: PublishConfiguration = Config.publishConfiguration(project)

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
        maven(url = "${publishConfiguration.mavenRepositoryBaseUrl}/service/local/staging/deployByRepositoryId/${publishConfiguration.stagingRepositoryId}") {
            name = "mavenCentral"
            credentials {
                username = publishConfiguration.ossrhUsername
                password = publishConfiguration.ossrhPassword
            }
        }
        maven(url = "${publishConfiguration.mavenRepositoryBaseUrl}/content/repositories/snapshots/") {
            name = "mavenCentralSnapshots"
            credentials {
                username = publishConfiguration.ossrhUsername
                password = publishConfiguration.ossrhPassword
            }
        }
    }

    publications {
        create("mavenKotlin", MavenPublication::class.java) {
            from(components.getByName("kotlin"))
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
                    name.set(Config.license.spdxIdentifier)
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
    useInMemoryPgpKeys(
        publishConfiguration.signingKeyId,
        publishConfiguration.signingKey,
        publishConfiguration.signingPassword
    )
    sign(publishing.publications)
}
