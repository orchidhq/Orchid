import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object Utils {
    @JvmStatic
    fun getGitHash(project: Project): String = with(project) {
        runCatching {
            val stdout = ByteArrayOutputStream()

            exec {
                commandLine("git", "rev-parse", "HEAD")
                standardOutput = stdout
            }

            stdout.toString().trim()
        }.getOrElse { "" }
    }
}
