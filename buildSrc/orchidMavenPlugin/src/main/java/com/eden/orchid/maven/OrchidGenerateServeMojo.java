package com.eden.orchid.maven;

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Executes the {@code serve} task.
 */
@Mojo(name = "serve")
public class OrchidGenerateServeMojo extends OrchidGenerateMainMojo {

    public OrchidGenerateServeMojo() {
        super("serve", true);
    }
}
