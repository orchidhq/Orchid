import org.gradle.api.Project
import java.io.ByteArrayOutputStream

fun getCurrentSha(project: Project): String {
    return project.runCommand("git", "rev-parse", "HEAD")
}

fun getProjectVersion(project: Project, failWithUncommittedChanges: Boolean = false, failIfNotRelease: Boolean = false): String {
    val latestTagName = project.runCommand("git", "describe", "--abbrev=0", "--tags")
    val latestTagSha = project.runCommand("git", "rev-list", "--tags", "--max-count=1")
    val currentSha = project.runCommand("git", "rev-parse", "HEAD")
    val commitsSinceLastTag = project.runCommand("git", "log", "${latestTagName}..HEAD", "--oneline", "--pretty=format:%s").lines().reversed()
    val isRelease = project.hasProperty("release")
    val hasUncommittedChanges = project.runCommand("git", "status", "--porcelain").isBlank()

    var (major, minor, patch) = latestTagName
        .split('.')
        .map { it.trim().toInt() }

    if(failWithUncommittedChanges) { check(!hasUncommittedChanges) { "There are uncommitted changes!" } }
    if(failIfNotRelease)           { check(isRelease)              { "This is not a release build!"   } }

    val referenceVersion = "$major.$minor.$patch"

    if(currentSha != latestTagSha || hasUncommittedChanges) {
        patch++
    }

    for(commit in commitsSinceLastTag) {
        if(commit.startsWith("[minor]")) {
            patch = 0
            minor++
        }else if(commit.startsWith("[major]")) {
            patch = 0
            minor = 0
            major++
        }
    }

    val actualVersion = "$major.$minor.$patch${if (isRelease) "" else "-SNAPSHOT"}"
    println("$referenceVersion -> $actualVersion ($latestTagSha)\n  * " + commitsSinceLastTag.joinToString(separator = "\n  * "))

    return actualVersion
}

fun createTag(project: Project) {
    val version = getProjectVersion(project, true, true)
}

fun Project.runCommand(vararg command: String): String {
    return runCatching {
        val stdout = ByteArrayOutputStream()

        exec {
            commandLine(*command)
            standardOutput = stdout
        }

        stdout.toString().trim()
    }.getOrElse { "" }
}

project.version = getProjectVersion(project)
project.extra.set("currentSha", getCurrentSha(project))
