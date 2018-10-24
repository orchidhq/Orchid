package com.eden.orchid.maven;

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Executes the {@code interactive} task.
 */
@Mojo(name = "shell")
public class OrchidGenerateShellMojo extends OrchidGenerateMainMojo {

    public OrchidGenerateShellMojo() {
        super("interactive", true);
    }
}
