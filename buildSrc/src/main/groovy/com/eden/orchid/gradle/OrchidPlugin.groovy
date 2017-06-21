package com.eden.orchid.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class OrchidPlugin implements Plugin<Project> {
    public static String configurationName = 'orchidDocs'

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

        project.tasks.replace("javadoc", OrchidGenerateJavadocTask)
    }
}
