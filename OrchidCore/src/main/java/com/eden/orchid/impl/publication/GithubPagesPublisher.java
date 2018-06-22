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

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
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

        return valid;
    }

    @Override
    public void publish() {
        try {
            Path repo = copySite();

            gitCommand(repo, null, "git", "init");

            gitCommand(repo, null, "git", "config", "user.name", "Orchid");
            gitCommand(repo, null, "git", "config", "user.email", "orchid@orchid");

            gitCommand(repo, null, "git", "add",    "-A");
            gitCommand(repo, null, "git", "commit", "-m", getCommitMessage());

            gitCommand(repo, new String[] {"git", "remote", "add", "origin", getDisplayedRemoteUrl()}, "git", "remote", "add", "origin", getRemoteUrl());

            gitCommand(repo, null, "git", "push", "-f", "origin", getRemoteBranch());
        }
        catch (Exception e) {
            e.printStackTrace();
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

    private Path copySite() throws Exception {
        Path sourceDir = Paths.get(destinationDir);
        Path targetDir = OrchidUtils.getTempDir(destinationDir, "gh-pages", true);
        Files.walkFileTree(sourceDir, new CopyDir(sourceDir, targetDir));
        return targetDir;
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

    private String getRemoteBranch() {
        return Clog.format("master:{}", branch);
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
