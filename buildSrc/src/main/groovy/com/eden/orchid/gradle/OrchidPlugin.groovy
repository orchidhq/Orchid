package com.eden.orchid.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class OrchidPlugin implements Plugin<Project> {
    String configurationName = 'orchidDocs'

    void apply(Project project) {
        // create Orchid closure, configuration, and sourceSet
        project.extensions.create('orchid', OrchidPluginExtension)
        project.configurations.create(configurationName)
        project.apply plugin: "java"
        project.sourceSets { orchidDocs }

        // Setup individual tasks
        project.tasks.create('orchidRun', OrchidGenerateMainTask)
        project.tasks.orchidRun.dependsOn 'classes'

        // remove normal 'javadoc' task and replace it with our own
        project.tasks.replace('javadoc', OrchidGenerateJavadocTask)
    }
}
