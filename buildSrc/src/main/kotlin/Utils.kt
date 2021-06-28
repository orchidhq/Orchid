import org.gradle.api.Project
import java.io.ByteArrayOutputStream

data class License(
    val name: String,
    val url: String
) {
    companion object {
        val BSD3 = License("BSD-3-Clause", "https://opensource.org/licenses/BSD-3-Clause")
    }
}

fun Project.loadProperty(
    projectPropertyName: String,
    envName: String = projectPropertyName.toUpperCase()
): String {
    val envValue = System.getenv(envName)?.toString()
    if (envValue != null) return envValue

    val projectPropertiesValue = project.properties[projectPropertyName]?.toString()
    if (projectPropertiesValue != null) return projectPropertiesValue

    return ""
}

fun Project.loadFileContents(
    projectPropertyName: String,
    envName: String = projectPropertyName.toUpperCase()
): String {
    val decodeIfNeeded: (String) -> String = {
        if(it.startsWith("~/")) {
            // the value is a path to file on disk. Read its contents
            val filePath = it.replace("~/", System.getProperty("user.home") + "/")
            project.file(filePath).readText()
        }
        else {
            // the value itself is the file contents file
            it
        }
    }

    val envValue = System.getenv(envName)?.toString()
    if (envValue != null) return decodeIfNeeded(envValue)

    val projectPropertiesValue = project.properties[projectPropertyName]?.toString()
    if (projectPropertiesValue != null) return decodeIfNeeded(projectPropertiesValue)

    return ""
}

fun Project.runCommand(command: String): String {
    return runCatching {
        val stdout = ByteArrayOutputStream()

        exec {
            commandLine(*command.split(' ').toTypedArray())
            standardOutput = stdout
        }

        stdout.toString().trim()
    }.getOrElse { "" }
}

// Versioning Utils
// ---------------------------------------------------------------------------------------------------------------------

fun Project.getCurrentSha(): String = runCommand("git rev-parse HEAD")
fun Project.getLatestTagSha(): String = runCommand("git rev-list --tags --max-count=1")
fun Project.getLatestTagName(): String = runCommand("git describe --abbrev=0 --tags")
fun Project.getCommitsSinceLastTag(latestTagName: String): List<String> = runCommand("git log ${latestTagName}..HEAD --oneline --pretty=format:%s").lines().reversed()
fun Project.hasUncommittedChanges(): Boolean = runCommand("git status --porcelain").isBlank()

fun String.parseVersion(): Triple<Int, Int, Int> {
    return this
        .split('.')
        .map { it.trim().toIntOrNull() ?: 0 }
        .let { Triple(it.component1(), it.component2(), it.component3()) }
}

fun Project.getProjectVersion(
    logChanges: Boolean = false,
    failWithUncommittedChanges: Boolean = false,
    failIfNotRelease: Boolean = false,
    snapshotSuffix: String = "-SNAPSHOT",
    initialVersion: String = "0.1.0",
    majorVersionBumpCommitPrefix: String = "[major]",
    minorVersionBumpCommitPrefix: String = "[minor]"
): ProjectVersion {
    val latestTagName = getLatestTagName()
    val latestTagSha = getLatestTagSha()
    val currentSha = getCurrentSha()
    val commitsSinceLastTag = getCommitsSinceLastTag(latestTagName)
    val isRelease = hasProperty("release")
    val hasUncommittedChanges = hasUncommittedChanges()

    val (major, minor, patch) = when {
        latestTagName.isBlank() -> {
            initialVersion.parseVersion()
        }
        else -> {
            var (_major, _minor, _patch) = latestTagName.parseVersion()

            if (currentSha != latestTagSha || hasUncommittedChanges) {
                _patch++
            }

            for (commit in commitsSinceLastTag) {
                if (commit.startsWith(minorVersionBumpCommitPrefix)) {
                    _patch = 0
                    _minor++
                } else if (commit.startsWith(majorVersionBumpCommitPrefix)) {
                    _patch = 0
                    _minor = 0
                    _major++
                }
            }

            Triple(_major, _minor, _patch)
        }
    }

    // make checks on version
    if (failWithUncommittedChanges) {
        check(!hasUncommittedChanges) { "There are uncommitted changes!" }
    }
    if (failIfNotRelease) {
        check(isRelease) { "This is not a release build!" }
    }

    return ProjectVersion(
        major = major,
        minor = minor,
        patch = patch,
        snapshot = !isRelease,
        snapshotSuffix = snapshotSuffix,
        sha = currentSha,
        commits = commitsSinceLastTag
    ).also {
        if (logChanges) {
            println("$latestTagName ($latestTagSha) -> $it ($currentSha) \n  * " + commitsSinceLastTag.joinToString(separator = "\n  * "))
        }
    }
}

data class ProjectVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val snapshot: Boolean,
    val snapshotSuffix: String,
    val sha: String,
    val commits: List<String>
) {
    val shortVersion: String = "$major.$minor"
    val releaseVersion: String = "$major.$minor.$patch"
    val fullVersion: String = "$releaseVersion${if (snapshot) snapshotSuffix else ""}"

    override fun toString(): String = fullVersion

    fun debug(): String {
        return """
            |ProjectVersion(
            |    major=$major
            |    minor=$minor
            |    patch=$patch
            |    snapshot=$snapshot
            |    snapshotSuffix=$snapshotSuffix
            |    sha=$sha
            |    commits=$commits
            |    shortVersion=$shortVersion
            |    releaseVersion=$releaseVersion
            |    fullVersion=$fullVersion
            |)
        """.trimMargin()
    }
}
