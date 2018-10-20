package com.eden.orchid.maven;

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Executes the {@code watch} task.
 */
@Mojo(name = "watch")
public class OrchidGenerateWatchMojo extends OrchidGenerateMainMojo {

    public OrchidGenerateWatchMojo() {
        super("watch", true);
    }
}
