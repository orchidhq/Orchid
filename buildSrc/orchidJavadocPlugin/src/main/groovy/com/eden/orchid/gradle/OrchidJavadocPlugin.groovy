package com.eden.orchid.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class OrchidJavadocPlugin implements Plugin<Project> {
    public static String configurationName = 'orchid'
    public static String mainClassName = 'com.eden.orchid.javadoc.OrchidJavadoc'

    void apply(Project project) {
        project.apply plugin: "com.eden.orchidPlugin"
        project.extensions.create('orchidJavadoc', OrchidJavadocPluginExtension)

        project.tasks.replace("javadoc", OrchidGenerateJavadocTask)
    }

}
