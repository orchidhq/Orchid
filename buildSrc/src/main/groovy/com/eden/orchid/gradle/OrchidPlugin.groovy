package com.eden.orchid.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.javadoc.Javadoc

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
        project.tasks.create('orchidWatch', OrchidGenerateWatchTask) {
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

// Task Implementations
//----------------------------------------------------------------------------------------------------------------------

class OrchidGenerateBuildTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchidDocs.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'build', true))
        super.exec()
    }
}

class OrchidGenerateWatchTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchidDocs.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'watch', true))
        super.exec()
    }
}

class OrchidGenerateServeTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchidDocs.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'serve', true))
        super.exec()
    }
}

class OrchidGenerateMainTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchidDocs.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'build', false))
        super.exec()
    }
}