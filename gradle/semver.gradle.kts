import java.io.ByteArrayOutputStream

// Helper Methods
//----------------------------------------------------------------------------------------------------------------------

data class ProjectVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val snapshot: Boolean,
    val snapshotSuffix: String,
    val sha: String,
    val commits: List<String>
) {
    override fun toString(): String = fullVersion

    val shortVersion: String = "$major.$minor"
    val releaseVersion: String = "$major.$minor.$patch"
    val fullVersion: String = "$releaseVersion${if (snapshot) snapshotSuffix else ""}"

    fun toFile(): File {
        return project.file("$rootDir/docs/src/orchid/resources/changelog/$shortVersion/$releaseVersion.md")
    }
}

fun getCurrentSha(): String = runCommand("git", "rev-parse", "HEAD")
fun getLatestTagSha(): String = runCommand("git", "rev-list", "--tags", "--max-count=1")
fun getLatestTagName(): String = runCommand("git", "describe", "--abbrev=0", "--tags")
fun getCommitsSinceLastTag(latestTagName: String): List<String> = runCommand("git", "log", "${latestTagName}..HEAD", "--oneline", "--pretty=format:%s").lines().reversed()
fun hasUncommittedChanges(): Boolean = runCommand("git", "status", "--porcelain").isBlank()

fun getProjectVersion(
    logChanges: Boolean = true,
    failWithUncommittedChanges: Boolean = false,
    failIfNotRelease: Boolean = false,
    snapshotSuffix: String = "-SNAPSHOT"
): ProjectVersion {
    var latestTagName = getLatestTagName()
    val latestTagSha = getLatestTagSha()
    val currentSha = getCurrentSha()
    val commitsSinceLastTag = getCommitsSinceLastTag(latestTagName)
    val isRelease = hasProperty("release")
    val hasUncommittedChanges = hasUncommittedChanges()

    if(latestTagName.length < 6) {
        latestTagName = "0.0.1"
    }

    var (major, minor, patch) = latestTagName
        .split('.')
        .map { it.trim().toInt() }

    if (failWithUncommittedChanges) {
        check(!hasUncommittedChanges) { "There are uncommitted changes!" }
    }
    if (failIfNotRelease) {
        check(isRelease) { "This is not a release build!" }
    }

    val referenceVersion = "$major.$minor.$patch"

    if (currentSha != latestTagSha || hasUncommittedChanges) {
        patch++
    }

    for (commit in commitsSinceLastTag) {
        if (commit.startsWith("[minor]")) {
            patch = 0
            minor++
        } else if (commit.startsWith("[major]")) {
            patch = 0
            minor = 0
            major++
        }
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
            println("$referenceVersion -> $it ($latestTagSha)\n  * " + commitsSinceLastTag.joinToString(separator = "\n  * "))
        }
    }
}

fun runCommand(vararg command: String): String {
    return runCatching {
        val stdout = ByteArrayOutputStream()

        exec {
            commandLine(*command)
            standardOutput = stdout
        }

        stdout.toString().trim()
    }.getOrElse { "" }
}

// Create tasks about versions/changelogs
//----------------------------------------------------------------------------------------------------------------------

val getReleaseName by project.tasks.registering {
    doLast {
        println(getProjectVersion(logChanges = false))
    }
}

val createChangelogFile by project.tasks.registering {
    doLast {
        val version = getProjectVersion()
        val changelogFile = version.toFile()

        project.mkdir(changelogFile.parentFile)
        changelogFile.writeText(
            """
            |---
            |---
            |
            |- ${version.commits.joinToString(separator = "\n- ")}
            |
            |## Breaking Changes
            |
            |- 
            |
            """.trimMargin()
        )
    }
}

val checkForChangelogFile by project.tasks.registering {
    doLast {
        if (project.hasProperty("release")) {
            val version = getProjectVersion(logChanges = false)
            val changelogFile = version.toFile()

            if (!changelogFile.exists()) {
                throw java.io.FileNotFoundException("There is no changelog entry for this version, expected '${changelogFile.absolutePath}' to exist.")
            }
        }
    }
}

// Inject data into build
//----------------------------------------------------------------------------------------------------------------------

project.version = getProjectVersion(logChanges = false)
