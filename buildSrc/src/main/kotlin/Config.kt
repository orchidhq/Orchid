import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate

object Config {
    val groupId = "io.github.copper-leaf.orchid"
    val githubUrl = "https://github.com/orchidhq/orchid"
    val license = License.GPL_V3
    val javaVersion = JavaVersion.VERSION_1_8.toString()

    object Developer {
        val id = "cjbrooks12"
        val name = "Casey Brooks"
        val email = "cjbrooks12@gmail.com"
    }

    fun projectVersion(project: Project): ProjectVersion {
        val projectVersion: ProjectVersion by project.extra
        return projectVersion
    }

    fun publishConfiguration(project: Project): PublishConfiguration {
        return PublishConfiguration(
            mavenRepositoryBaseUrl = "https://s01.oss.sonatype.org",
            stagingRepositoryIdFile = project.rootProject.buildDir.resolve("export").resolve("stagingRepositoryId"),
            stagingProfileId = project.loadProperty("staging_profile_id"),

            signingKeyId = project.loadProperty("signing_key_id"),
            signingKey = project.loadFileContents("signing_key"),
            signingPassword = project.loadProperty("signing_password"),
            ossrhUsername = project.loadProperty("ossrh_username"),
            ossrhPassword = project.loadProperty("ossrh_password"),
        )
    }
}
