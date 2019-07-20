package com.eden.orchid.api.util

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.utilities.InputStreamIgnorer
import com.eden.orchid.utilities.InputStreamPrinter
import com.eden.orchid.utilities.OrchidUtils
import com.google.inject.ImplementedBy
import org.apache.commons.io.FileUtils
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named

@ImplementedBy(CliGitFacade::class)
interface GitFacade {
    fun init(remoteUrl: String, displayedRemoteUrl: String, remoteBranch: String, verbose: Boolean = true): GitRepoFacade
    fun clone(remoteUrl: String, displayedRemoteUrl: String, remoteBranch: String, verbose: Boolean = true): GitRepoFacade
}

interface GitRepoFacade {

    val repoDir: Path

    fun initRepo(): GitRepoFacade
    fun cloneRepo(): GitRepoFacade
    fun commit(commitUsername: String, commitEmail: String, commitMessage: String): GitRepoFacade
    fun push(force: Boolean): GitRepoFacade

}

// CLI Git Facade
//----------------------------------------------------------------------------------------------------------------------

class CliGitFacade
@Inject
constructor(
    @Named("dest")
    private val destinationDir: String
) : GitFacade {

    override fun init(remoteUrl: String, displayedRemoteUrl: String, remoteBranch: String, verbose: Boolean): GitRepoFacade {
        val repoDir = OrchidUtils.getTempDir(destinationDir, "git", true)
        val repo = CliGitRepoFacade(
            repoDir,
            remoteUrl,
            displayedRemoteUrl,
            remoteBranch,
            verbose
        )
        repo.initRepo()
        return repo
    }

    override fun clone(remoteUrl: String, displayedRemoteUrl: String, remoteBranch: String, verbose: Boolean): GitRepoFacade {
        val repoDir = OrchidUtils.getTempDir(destinationDir, "git", true)
        val repo = CliGitRepoFacade(
            repoDir,
            remoteUrl,
            displayedRemoteUrl,
            remoteBranch,
            verbose
        )
        repo.cloneRepo()
        return repo
    }
}

class CliGitRepoFacade(
    override val repoDir: Path,
    private val remoteUrl: String,
    private val displayedRemoteUrl: String,
    private val remoteBranch: String,
    private val verbose: Boolean = true
) : GitRepoFacade {

    override fun initRepo(): GitRepoFacade {
        gitCommand("git", "init")
        gitCommand("git", "checkout", "-b", remoteBranch)
        gitCommand(
            "git",
            "remote",
            "add",
            "origin",
            remoteUrl
        )

        return this
    }

    override fun cloneRepo(): GitRepoFacade {
        gitCommand(
            "git",
            "clone",
            "--single-branch",
            "-b",
            remoteBranch,
            remoteUrl,
            "."
        )

        return this
    }

    override fun commit(commitUsername: String, commitEmail: String, commitMessage: String): GitRepoFacade {
        gitCommand("git", "config", "user.name", commitUsername)
        gitCommand("git", "config", "user.email", commitEmail)
        gitCommand("git", "add", "-A")
        gitCommand("git", "commit", "-m", commitMessage)

        return this
    }

    override fun push(force: Boolean): GitRepoFacade {
        if (force) {
            gitCommand("git", "push", "-f", "origin", "$remoteBranch:$remoteBranch")
        } else {
            gitCommand("git", "push", "origin", "$remoteBranch:$remoteBranch")
        }

        return this
    }

// Execute Git commands
//----------------------------------------------------------------------------------------------------------------------

    private fun gitCommand(vararg command: String) {
        // display git command without printing potentially sensitive info
        val displayedCommand = command.joinToString().replace(remoteUrl, displayedRemoteUrl)

        if(verbose) Clog.tag("GIT").d(displayedCommand)

        // synchronously run Git command
        val process = ProcessBuilder()
            .command(*command)
            .redirectErrorStream(true)
            .directory(repoDir.toFile())
            .start()

        val streamHandler = if(verbose)
            InputStreamPrinter(process.inputStream, null) { it.replace(remoteUrl, displayedRemoteUrl) }
        else
            InputStreamIgnorer(process.inputStream)

        val executor = Executors.newSingleThreadExecutor()
        executor.submit(streamHandler)
        val exitValue = process.waitFor()
        executor.shutdown()

        // throw error if the command did not return exit code 0
        if(exitValue != 0) {
            throw IOException("Git command [$displayedCommand] failed with exit code $exitValue")
        }
    }

}

// Helpers
//----------------------------------------------------------------------------------------------------------------------

fun GitRepoFacade.makeSubDir(subfolder: String): Path {
    return repoDir.resolve(subfolder)
}

fun GitRepoFacade.copy(subdirectory: Path? = null, from: String) {
    val sourceDir = Paths.get(from)
    Files.walkFileTree(sourceDir, CopyDir(sourceDir, subdirectory ?: this.repoDir))
}

fun GitRepoFacade.delete(subdirectory: Path? = null) {
    (subdirectory ?: repoDir)
        .toFile()
        .listFiles()
        ?.forEach { file ->
            // don't delete the .git directory
            if (file.isDirectory && file.name == ".git")

            // recursively delete the directory and its files
            else if (file.isDirectory) FileUtils.deleteDirectory(file)

            // delete the file as normal
            else if (file.isFile) file.delete()
        }
}

fun GitRepoFacade.addFile(filename: String, content: String) {
    val newFile = repoDir.resolve(filename)
    newFile.toFile().writeText(content)
}

// Helper Classes
//----------------------------------------------------------------------------------------------------------------------
class CopyDir(private val sourceDir: Path, private val targetDir: Path) : SimpleFileVisitor<Path>() {

    override fun visitFile(file: Path, attributes: BasicFileAttributes): FileVisitResult {
        try {
            val targetFile = targetDir.resolve(sourceDir.relativize(file))
            Files.copy(file, targetFile)
        } catch (e: IOException) {
            Clog.e(e)
            e.printStackTrace()
        }

        return FileVisitResult.CONTINUE
    }

    override fun preVisitDirectory(dir: Path, attributes: BasicFileAttributes): FileVisitResult {
        try {
            val newDir = targetDir.resolve(sourceDir.relativize(dir))
            if(!Files.exists(newDir)) {
                Files.createDirectory(newDir)
            }
        } catch (e: IOException) {
            Clog.e(e)
            e.printStackTrace()
        }

        return FileVisitResult.CONTINUE
    }
}
