import org.gradle.api.JavaVersion

object Config {
    val groupId = "io.github.copper-leaf"
    val githubUrl = "https://github.com/orchidhq/orchid"
    val license = License.BSD3
    val javaVersion = JavaVersion.VERSION_1_8.toString()

    object Developer {
        val id = "cjbrooks12"
        val name = "Casey Brooks"
        val email = "cjbrooks12@gmail.com"
    }
}
