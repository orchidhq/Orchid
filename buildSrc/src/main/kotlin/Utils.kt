import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.File

open class License(
    val spdxIdentifier: String,
    val url: String,
) {
    open val urlAliases: Set<String> = emptySet()
    open val compatibleWith: Set<License> = emptySet()


// Permissive Licenses
// ---------------------------------------------------------------------------------------------------------------------

    object Apache : License(
        spdxIdentifier = "Apache-2.0",
        url = "https://opensource.org/licenses/Apache-2.0"
    ) {
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object MIT : License(
        spdxIdentifier = "MIT",
        url = "https://opensource.org/licenses/MIT"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/MIT",
            "http://www.opensource.org/licenses/mit-license.php",
            "http://www.opensource.org/licenses/mit-license.html",
            "https://raw.githubusercontent.com/bit3/jsass/master/LICENSE",
            "https://github.com/mockito/mockito/blob/main/LICENSE",
            "http://json.org/license.html",
            "https://jsoup.org/license",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object BSD2 : License(
        spdxIdentifier = "BSD-2-Clause",
        url = "https://opensource.org/licenses/BSD-2-Clause"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-2-Clause",
            "http://www.opensource.org/licenses/BSD-2-Clause",
            "http://www.opensource.org/licenses/bsd-license.php",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object BSD3 : License(
        spdxIdentifier = "BSD-3-Clause",
        url = "https://opensource.org/licenses/BSD-3-Clause"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-3-Clause",
            "http://www.jcraft.com/jzlib/LICENSE.txt",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

    object CDDL : License(
        spdxIdentifier = "CDDL-1.0",
        url = "https://opensource.org/licenses/CDDL-1.0"
    ) {
        override val urlAliases = setOf(
            "https://github.com/javaee/javax.annotation/blob/master/LICENSE",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL
        )
    }

// Copyleft Licenses
// ---------------------------------------------------------------------------------------------------------------------

    object LGPL_V2 : License(
        spdxIdentifier = "LGPL-2.0",
        url = "https://opensource.org/licenses/LGPL-2.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/lgpl-2.1-standalone.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object LGPL_V3 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/LGPL-3.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/lgpl.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object GPL_V2 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/GPL-2.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/licenses/gpl-2.0-standalone.html",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            GPL_V2, GPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object GPL_V3 : License(
        spdxIdentifier = "GPL-2.0",
        url = "https://opensource.org/licenses/GPL-3.0"
    ) {
        override val urlAliases = setOf(
            "http://www.gnu.org/copyleft/gpl.html",
            "http://www.gnu.org/licenses/gpl.txt"
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            GPL_V2, GPL_V3,
            EPL_V1, EPL_V2
        )
    }

    object EPL_V1 : License(
        spdxIdentifier = "EPL-1.0",
        url = "https://opensource.org/licenses/EPL-1.0"
    ) {
        override val urlAliases = setOf(
            "http://opensource.org/licenses/BSD-3-Clause",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
    }
    object EPL_V2 : License(
        spdxIdentifier = "EPL-2.0",
        url = "https://opensource.org/licenses/EPL-2.0"
    ) {
        override val urlAliases = setOf(
            "https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt",
            "https://www.eclipse.org/legal/epl-v20.html",
            "http://www.eclipse.org/legal/epl-v20.html",
            "https://www.eclipse.org/legal/epl-2.0/",
        )
        override val compatibleWith = setOf(
            Apache, MIT, BSD2, BSD3, CDDL,
            LGPL_V2, LGPL_V3,
            EPL_V1, EPL_V2
        )
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
        if (it.startsWith("~/")) {
            // the value is a path to file on disk. Read its contents
            val filePath = it.replace("~/", System.getProperty("user.home") + "/")
            project.file(filePath).readText()
        } else {
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
fun Project.getCommitsSinceLastTag(latestTagName: String): List<String> =
    runCommand("git log ${latestTagName}..HEAD --oneline --pretty=format:%s").lines().reversed()

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
        previousVersion = latestTagName,
        major = major,
        minor = minor,
        patch = patch,
        snapshot = !isRelease,
        snapshotSuffix = snapshotSuffix,
        sha = currentSha,
        commits = commitsSinceLastTag
    ).also {
        if (logChanges) {
            println(
                "$latestTagName ($latestTagSha) -> $it ($currentSha) \n  * " + commitsSinceLastTag.joinToString(
                    separator = "\n  * "
                )
            )
        }
    }
}

data class ProjectVersion(
    val previousVersion: String,
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
    val documentationVersion: String = if (snapshot) {
        previousVersion
    } else {
        releaseVersion
    }

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
            |    documentationVersion=$documentationVersion
            |)
        """.trimMargin()
    }
}

data class PublishConfiguration(
    val mavenRepositoryBaseUrl: String,
    val stagingRepositoryIdFile: File,
    val stagingProfileId: String,

    val signingKeyId: String,
    val signingKey: String,
    val signingPassword: String,
    val ossrhUsername: String,
    val ossrhPassword: String,
) {

    var stagingRepositoryId: String
        get() {
            return if (stagingRepositoryIdFile.exists()) {
                stagingRepositoryIdFile.readText()
            } else {
                ""
            }
        }
        set(value) {
            if (stagingRepositoryIdFile.exists()) {
                stagingRepositoryIdFile.delete()
            }

            stagingRepositoryIdFile.parentFile.mkdirs()
            stagingRepositoryIdFile.createNewFile()

            stagingRepositoryIdFile.writeText(value)
        }

    fun debug(): String {
        return """
            |PublishConfiguration(
            |    mavenRepositoryBaseUrl=$mavenRepositoryBaseUrl
            |    stagingRepositoryIdFile=$stagingRepositoryIdFile
            |    stagingRepositoryId=$stagingRepositoryId
            |    stagingProfileId=$stagingProfileId
            |
            |    signingKeyId=${if (signingKeyId.isNotBlank()) "[REDACTED]" else ""}
            |    signingKey=${if (signingKey.isNotBlank()) signingKey.lineSequence().first() else ""}
            |    signingPassword=${if (signingPassword.isNotBlank()) "[REDACTED]" else ""}
            |    ossrhUsername=${if (ossrhUsername.isNotBlank()) "[REDACTED]" else ""}
            |    ossrhPassword=${if (ossrhPassword.isNotBlank()) "[REDACTED]" else ""}
            |)
        """.trimMargin()
    }
}
