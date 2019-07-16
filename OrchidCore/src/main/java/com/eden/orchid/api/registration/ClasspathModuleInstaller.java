package com.eden.orchid.api.registration;

import com.caseyjbrooks.clog.Clog;
import io.github.classgraph.ClassGraph;

import java.lang.reflect.Modifier;
import java.util.stream.Stream;

@IgnoreModule
public class ClasspathModuleInstaller extends OrchidModule {

    @Override
    protected void configure() {
        final StringBuilder moduleLog = new StringBuilder();
        moduleLog.append("\n--------------------");

        new ClassGraph()
                .enableClassInfo()
                .scan()
                .getSubclasses(OrchidModule.class.getName())
                .loadClasses(OrchidModule.class).forEach((matchingClass) -> {
                    if (isInstantiable(matchingClass)) {
                        try {
                            OrchidModule provider = matchingClass.newInstance();
                            if (provider != null) {
                                install(provider);
                                // don't log anonymous-class modules
                                if (!provider.getClass().getName().startsWith("com.eden.orchid.OrchidModule")) {
                                    moduleLog.append("\n * " + provider.getClass().getName());
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        moduleLog.append("\n");
        Clog.tag("Auto-loaded modules").log(moduleLog.toString());
    }

    private boolean isInstantiable(Class<?> matchingClass) {
        if (matchingClass.isAnnotationPresent(IgnoreModule.class)) {
            return false;
        }
        if (matchingClass.isInterface()) {
            return false;
        }
        if (Modifier.isAbstract(matchingClass.getModifiers())) {
            return false;
        }
        if (!hasParameterlessPublicConstructor(matchingClass)) {
            return false;
        }

        return true;
    }

    private boolean hasParameterlessPublicConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors()).anyMatch((c) -> c.getParameterCount() == 0);
    }

}
