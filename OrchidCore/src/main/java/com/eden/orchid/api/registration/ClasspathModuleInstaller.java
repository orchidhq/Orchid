package com.eden.orchid.api.registration;

import com.caseyjbrooks.clog.Clog;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.reflect.Modifier;

@IgnoreModule
public class ClasspathModuleInstaller extends AbstractModule {

    @Override
    protected void configure() {
        final StringBuilder moduleLog = new StringBuilder();
        moduleLog.append("Auto-loading modules: ");
        moduleLog.append("\n--------------------");

        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchClassesImplementing(Module.class, (matchingClass) -> {
            if (!matchingClass.isAnnotationPresent(IgnoreModule.class) && !(
                    matchingClass.isInterface() || Modifier.isAbstract(matchingClass.getModifiers())
            )) {
                try {
                    Module provider = matchingClass.newInstance();
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
