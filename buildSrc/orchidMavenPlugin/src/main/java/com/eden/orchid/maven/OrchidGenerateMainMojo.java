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
     * The static site version.
     */
    @Parameter(property = "orchid.version", defaultValue = "${project.version}")
    private String version;

    /**
     * The theme to use in the static site.
     */
    @Parameter(property = "orchid.theme", defaultValue = "Default")
    private String theme;

    /**
     * The base URL used in HTML links.
     */
    @Parameter(property = "orchid.baseUrl")
    private String baseUrl;

    /**
     * The environment used to run the site.
     */
    @Parameter(property = "orchid.environment", defaultValue = "debug")
    private String environment;

    /**
     * A run task that can be used to override the default 'run' command.
     */
    @Parameter(property = "orchid.runTask")
    private String runTask;

    /**
     * Allows running a dry deploy instead of a full deploy.
     */
    @Parameter(property = "orchid.dryDeploy", defaultValue = "false")
    private boolean dryDeploy;

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

            getLog().info("Args : " + StringUtils.join(args, ", "));

            main.invoke(null, (Object) args);
        } catch (ReflectiveOperationException e) {
            throw new MojoExecutionException("Unable to call " + ORCHID_CLASS, e);
        }
    }

    private String[] getOrchidProjectArgs() {
        return ArrayUtils.addAll(
                new String[]{
                        "--src", srcDir,
                        "--dest", destDir,
                        "--version", version,
                        "--theme", theme,
                        "--baseUrl", baseUrl == null ? "" : baseUrl,
                        "--environment", environment,
                        "--task", (force ? command : runTask != null ? runTask : command),
                        "--dryDeploy", Boolean.toString(dryDeploy),
                },
                args
        );
    }
}
