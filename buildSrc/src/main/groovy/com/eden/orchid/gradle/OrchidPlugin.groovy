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

        project.sourceSets {
            orchidDocs
        }

        project.tasks.create('orchidBuild', OrchidGenerateBuildTask) {
            dependsOn 'classes', "${configurationName}Classes"
            main 'com.eden.orchid.Orchid'
        }
        project.tasks.create('orchidServe', OrchidGenerateServeTask) {
            dependsOn 'classes', "${configurationName}Classes"
            main 'com.eden.orchid.Orchid'
        }
        project.tasks.create('orchidRun', OrchidGenerateMainTask) {
            dependsOn 'classes', "${configurationName}Classes"
            main 'com.eden.orchid.Orchid'
        }

        // remove normal 'javadoc' task and replace it with our own
        project.tasks.replace('javadoc', OrchidGenerateJavadocTask)
        project.tasks.javadoc {
            dependsOn 'classes', "${configurationName}Classes"
            options.doclet = 'com.eden.orchid.Orchid'
        }
    }
}
