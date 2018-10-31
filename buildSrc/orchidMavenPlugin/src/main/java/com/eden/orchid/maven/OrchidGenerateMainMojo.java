package com.eden.orchid.maven;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.lang.reflect.Method;

/**
 * Executes the {@code run} task.
 */
@Mojo(name = "run")
public class OrchidGenerateMainMojo extends AbstractMojo {

    private static final String ORCHID_CLASS = "com.eden.orchid.Orchid";

    /**
     * The base URL used in HTML links.
     */
    @Parameter(property = "orchid.baseUrl")
    private String baseUrl;

    /**
     * Source directory where Orchid resources will be loaded.
     */
    @Parameter(property = "orchid.srcDir", defaultValue = "${project.basedir}/src/orchid/resources")
    private String srcDir;

    /**
     * Destination directory where the static site will be generated.
     */
    @Parameter(property = "orchid.destDir", defaultValue = "${project.build.directory}/docs/orchid")
    private String destDir;

    /**
     * A run task that can be used to override the default 'run' command.
     */
    @Parameter(property = "orchid.runTask")
    private String runTask;

    /**
     * The theme to use in the static site.
     */
    @Parameter(property = "orchid.theme", defaultValue = "Default")
    private String theme;

    /**
     * The static site version.
     */
    @Parameter(property = "orchid.version", defaultValue = "${project.version}")
    private String version;

    /**
     * The environment used to run the site.
     */
    @Parameter(property = "orchid.environment", defaultValue = "debug")
    private String environment;

    /**
     * Allows running a dry deploy instead of a full deploy.
     */
    @Parameter(property = "orchid.dryDeploy", defaultValue = "false")
    private boolean dryDeploy;

    /**
     * The environment used to run the site.
     */
    @Parameter(property = "orchid.port", defaultValue = "8080")
    private int port;

    /**
     * The environment used to run the site.
     */
    @Parameter(property = "orchid.githubToken", defaultValue = "")
    private String githubToken;

    /**
     * Additional arguments to give to Orchid.
     */
    @Parameter
    private String[] args;

    private final String command;
    private final boolean force;

    public OrchidGenerateMainMojo() {
        this("build", false);
    }

    public OrchidGenerateMainMojo(String command, boolean force) {
        this.command = command;
        this.force = force;
    }

    @Override
    public void execute() throws MojoExecutionException {
        try {
            Class<?> orchid = Thread.currentThread().getContextClassLoader().loadClass(ORCHID_CLASS);
            Method main = orchid.getMethod("main", String[].class);

            String[] args = getOrchidProjectArgs();

            main.invoke(null, (Object) args);
        } catch (ReflectiveOperationException e) {
            throw new MojoExecutionException("Unable to call " + ORCHID_CLASS, e);
        }
    }

    private String[] getOrchidProjectArgs() {
        if(args == null) args = new String[0];
        return ArrayUtils.addAll(
                new String[]{
                        "--baseUrl",     "" + baseUrl,
                        "--src",         "" + srcDir,
                        "--dest",        "" + destDir,
                        "--task",        "" + (force ? command : runTask != null ? runTask : command),
                        "--theme",       "" + theme,
                        "--version",     "" + version,
                        "--environment", "" + environment,
                        "--dryDeploy",   "" + Boolean.toString(dryDeploy),
                        "--port",        "" + Integer.toString(port),
                        "--githubToken", "" + githubToken,
                },
                args
        );
    }
}
