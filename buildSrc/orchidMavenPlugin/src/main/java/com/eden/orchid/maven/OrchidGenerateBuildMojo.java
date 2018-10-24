package com.eden.orchid.maven;

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Executes the {@code build} task.
 */
@Mojo(name = "build")
public class OrchidGenerateBuildMojo extends OrchidGenerateMainMojo {

    public OrchidGenerateBuildMojo() {
        super("build", true);
    }
}
