package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.registration.IgnoreModule;
import com.google.inject.AbstractModule;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

@IgnoreModule
public class ClasspathModuleInstaller extends AbstractModule {

    @Override
    protected void configure() {
        final StringBuilder moduleLog = new StringBuilder();
        moduleLog.append("Auto-loading modules: ");
        moduleLog.append("\n--------------------");

        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchSubclassesOf(OrchidModule.class, (matchingClass) -> {
            if(!matchingClass.isAnnotationPresent(IgnoreModule.class)) {
                try {
                    AbstractModule provider = matchingClass.newInstance();
                    if (provider != null) {
                        install(provider);
                        if (!provider.getClass().getName().startsWith("com.eden.orchid.OrchidModule")) {
                            moduleLog.append("\n * " + provider.getClass().getName());
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        scanner.scan();

        moduleLog.append("\n--------------------");
        Clog.i(moduleLog.toString());
    }

}
