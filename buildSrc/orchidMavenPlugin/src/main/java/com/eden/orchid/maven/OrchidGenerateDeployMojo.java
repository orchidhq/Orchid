package com.eden.orchid.maven;

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Executes the {@code deploy} task.
 */
@Mojo(name = "deploy")
public class OrchidGenerateDeployMojo extends OrchidGenerateMainMojo {

    public OrchidGenerateDeployMojo() {
        super("deploy", false);
    }
}
