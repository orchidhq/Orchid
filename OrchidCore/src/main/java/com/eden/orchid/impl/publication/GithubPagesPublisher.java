package com.eden.orchid.impl.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.publication.OrchidPublisher;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class GithubPagesPublisher extends OrchidPublisher {

    public enum PublishType {
        CLEAN_BRANCH,
        CLEAN_BRANCH_MAINTAIN_HISTORY,
        VERSIONED_BRANCH,
        VERSIONED_BRANCH_WITH_LATEST
    }

    private final String destinationDir;
    private final String githubToken;

    @Getter @Setter
    @Option @StringDefault("Deploy to GitHub Pages from Orchid.")
    @Description("The commit message to attach to this deploy.")
    private String commitMessage;

    @Getter @Setter
    @Option @StringDefault("gh-pages")
    @Description("The branch to push to.")
    private String branch;

    @Getter @Setter
    @Option
    @Description("The user or organization with push access to your repo, used for authenticating with GitHub.")
    private String username;

    @Getter @Setter
    @Option
    @Description("The repository to push to, as [username/repo], or just [repo] to use the authenticating username.")
    private String repo;

    @Getter @Setter
    @Option @StringDefault("CLEAN_BRANCH")
    @Description("The type of publication to use for Github Pages." +
            "- CLEAN_BRANCH: Create a new branch with no history and force-push to the remote. Overwrites existing branch completely." +
            "- CLEAN_BRANCH_MAINTAIN_HISTORY: Clone existing branch, remove all files, then push to the remote. Overwrites all files, but maintains history." +
            "- VERSIONED_BRANCH: Clone existing branch, add current site to a versioned subfolder, then push to the remote. Maintains history and all prior versions' content." +
            "- VERSIONED_BRANCH_WITH_LATEST: Clone existing branch, add current site to a 'latest' and a versioned subfolder, then force-push to the remote. Maintains history and all prior versions' content." +
            "")
    private PublishType publishType;

    @Getter @Setter
    @Option @StringDefault("latest")
    @Description("The name of the 'latest' directory used for the VERSIONED_BRANCH_WITH_LATEST publish type.")
    private String latestDirName;

    @Inject
    public GithubPagesPublisher(
            OrchidContext context,
            @Named("d") String destinationDir,
            @Nullable @Named("githubToken") String githubToken) {
        super(context, "ghPages", 100);
        this.destinationDir = destinationDir;
        this.githubToken = githubToken;
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && exists(githubToken,  "A GitHub Personal Access Token is required for deploys, set as 'githubToken' flag.");
        valid = valid && exists(username,     "Must set the GitHub user or organization.");
        valid = valid && exists(repo,         "Must set the GitHub repository.");
        valid = valid && exists(branch,       "Must set the repository branch.");
        valid = valid && exists(publishType,  "Must set the publish type.");

        return valid;
    }

    @Override
    public void publish() {
        try {
            switch (publishType) {
                case CLEAN_BRANCH: doCleanBranch(); break;
                case CLEAN_BRANCH_MAINTAIN_HISTORY: doCleanBranchMaintainHistory(); break;
                case VERSIONED_BRANCH: doVersionedBranch(); break;
                case VERSIONED_BRANCH_WITH_LATEST: doVersionedBranchWithLatest(); break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
// Git push methods
//----------------------------------------------------------------------------------------------------------------------

    private void doCleanBranch() throws Exception {
        Path repo = getSiteDir();
        copySite(repo);
        initRepo(repo);
        createCommit(repo);
        pushBranch(repo, "master", true);
    }

    private void doCleanBranchMaintainHistory() throws Exception {
        Path repo = getSiteDir();
        cloneRepo(repo);
        deleteSite(repo);
        copySite(repo);
        createCommit(repo);
        pushBranch(repo, branch, false);
    }

    private void doVersionedBranch() throws Exception {
        Path repo = getSiteDir();
        cloneRepo(repo);

        Path versionDir = makeSubDir(repo, context.getVersion());
        copySite(versionDir);
        createCommit(repo);
        pushBranch(repo, branch, false);
    }

    private void doVersionedBranchWithLatest() throws Exception {
        Path repo = getSiteDir();
        cloneRepo(repo);

        Path versionDir = makeSubDir(repo, context.getVersion());
        copySite(versionDir);

        Path latestDir = makeSubDir(repo, latestDirName);
        deleteSite(latestDir);
        copySite(latestDir);

        createCommit(repo);
        pushBranch(repo, branch, true);
    }

    private void initRepo(Path repo) throws Exception {
        gitCommand(repo, null, "git", "init");
        gitCommand(repo, new String[] {"git", "remote", "add", "origin", getDisplayedRemoteUrl()}, "git", "remote", "add", "origin", getRemoteUrl());
    }

    private void cloneRepo(Path repo) throws Exception {
        gitCommand(repo, new String[] {"git", "clone", "--single-branch", "-b", branch, getDisplayedRemoteUrl()}, "git", "clone", "--single-branch", "-b", branch, getRemoteUrl());
    }

    private void createCommit(Path repo) throws Exception {
        gitCommand(repo, null, "git", "config", "user.name", "Orchid");
        gitCommand(repo, null, "git", "config", "user.email", "orchid@orchid");

        gitCommand(repo, null, "git", "add",    "-A");
        gitCommand(repo, null, "git", "commit", "-m", getCommitMessage());
    }

    private void pushBranch(Path repo, String localBranch, boolean force) throws Exception {
        if(force) {
            gitCommand(repo, null, "git", "push", "-f", "origin", getRemoteBranch(localBranch));
        }
        else {
            gitCommand(repo, null, "git", "push", "origin", getRemoteBranch(localBranch));
        }
    }

// Execute Git commands
//----------------------------------------------------------------------------------------------------------------------

    private void gitCommand(Path temporaryDir, String[] displayedCommand, String... command) throws Exception {
        if(displayedCommand != null) {
            Clog.d("Github Pages GIT: {}", String.join(" ", displayedCommand));
        }
        else {
            Clog.d("Github Pages GIT: {}", String.join(" ", command));
        }
        execGitCommand(temporaryDir, command);
    }

    private void execGitCommand(Path temporaryDir, String... command) throws Exception {
        Process process = new ProcessBuilder()
                .command(Arrays.asList(command))
                .directory(temporaryDir.toFile())
                .start();

        Executors.newSingleThreadExecutor().submit(new ScriptPublisher.InputStreamPrinter(process.getInputStream()));
        process.waitFor();
    }

// Helper Methods
//----------------------------------------------------------------------------------------------------------------------

    private Path getSiteDir() throws Exception {
        return OrchidUtils.getTempDir(destinationDir, "gh-pages", true);
    }

    private Path makeSubDir(Path sourceDir, String subfolder) throws Exception {
        return Files.createDirectories(sourceDir.resolve(subfolder));
    }

    private void copySite(Path targetDir) throws Exception {
        Path sourceDir = Paths.get(destinationDir);
        Files.walkFileTree(sourceDir, new CopyDir(sourceDir, targetDir));
    }

    private void deleteSite(Path targetDir) throws Exception {
        File[] files = targetDir.toFile().listFiles();

        for(File file : files) {
            if(file.isDirectory() && file.getName().equals(".git")) continue;
            if(file.isDirectory()) FileUtils.deleteDirectory(file);
            else if(file.isFile()) file.delete();
        }
    }

    private String getDisplayedRemoteUrl() {
        String[] repoParts = repo.split("/");
        String repoUsername = (repoParts.length == 2) ? repoParts[0] : username;
        String repoName     = (repoParts.length == 2) ? repoParts[1] : repoParts[0];
        return Clog.format("https://github.com/{}/{}.git", repoUsername, repoName);
    }

    private String getRemoteUrl() {
        String[] repoParts = repo.split("/");
        String repoUsername = (repoParts.length == 2) ? repoParts[0] : username;
        String repoName     = (repoParts.length == 2) ? repoParts[1] : repoParts[0];
        return Clog.format("https://{}:{}@github.com/{}/{}.git", username, githubToken, repoUsername, repoName);
    }

    private String getRemoteBranch(String localBranch) {
        return Clog.format("{}:{}", localBranch, branch);
    }

// Helper Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class CopyDir extends SimpleFileVisitor<Path> {
        private final Path sourceDir;
        private final Path targetDir;

        public CopyDir(Path sourceDir, Path targetDir) {
            this.sourceDir = sourceDir;
            this.targetDir = targetDir;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
            try {
                Path targetFile = targetDir.resolve(sourceDir.relativize(file));
                Files.copy(file, targetFile);
            }
            catch (IOException e) {
                System.err.println(e);
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) {
            try {
                Path newDir = targetDir.resolve(sourceDir.relativize(dir));
                Files.createDirectory(newDir);
            }
            catch (IOException e) {
                System.err.println(e);
            }

            return FileVisitResult.CONTINUE;
        }
    }

}
