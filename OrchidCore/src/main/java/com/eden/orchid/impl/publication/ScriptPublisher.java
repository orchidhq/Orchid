package com.eden.orchid.impl.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.publication.OrchidPublisher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class ScriptPublisher extends OrchidPublisher {

    private final String resourcesDir;

    @Getter @Setter
    @Option
    @Description("The executable name")
    private String[] command;

    @Getter @Setter
    @Option
    @Description("The working directory of the script to run")
    private String cwd;

    @Inject
    public ScriptPublisher(OrchidContext context, @Named("resourcesDir") String resourcesDir) {
        super(context, "script", 100);
        this.resourcesDir = resourcesDir;
    }

    @Override
    public boolean validate() {
        return !EdenUtils.isEmpty(command);
    }

    @Override
    public void publish() {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command);

            String directory;
            if(!EdenUtils.isEmpty(cwd)) {
                directory = cwd;
                if(directory.startsWith("~")) {
                    directory = System.getProperty("user.home") + directory.substring(1);
                }
            }
            else {
                directory = resourcesDir;
            }

            builder.directory(new File(directory));

            Clog.v("[{}]> {}", directory, String.join(" ", command));

            Process process = builder.start();

            Executors.newSingleThreadExecutor().submit(new InputStreamPrinter(process.getInputStream()));
            process.waitFor();
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @AllArgsConstructor
    private static class InputStreamPrinter implements Runnable {

        private final InputStream inputStream;

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(s -> Clog.log(s));
        }
    }



}
