package com.eden.orchid.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class OrchidJavadocPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.apply plugin: "com.eden.orchidPlugin"

        project.tasks.replace("javadoc", OrchidGenerateJavadocTask)
    }

}
