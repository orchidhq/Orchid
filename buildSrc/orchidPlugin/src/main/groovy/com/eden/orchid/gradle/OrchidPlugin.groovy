package com.eden.orchid.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec

class OrchidPlugin implements Plugin<Project> {
    public static String configurationName = 'orchid'
    public static String mainClassName = 'com.eden.orchid.Orchid'

    void apply(Project project) {
        // create Orchid closure, configuration, and sourceSet
        project.extensions.create('orchid', OrchidPluginExtension)
        project.configurations.create(configurationName)
        project.apply plugin: "java"

        project.sourceSets {
            orchid
        }

        project.tasks.create('orchidBuild', OrchidGenerateBuildTask) {
            dependsOn 'classes', "${configurationName}Classes"
            main "${mainClassName}"
        }
        project.tasks.create('orchidWatch', OrchidGenerateWatchTask) {
            dependsOn 'classes', "${configurationName}Classes"
            main "${mainClassName}"
        }
        project.tasks.create('orchidServe', OrchidGenerateServeTask) {
            dependsOn 'classes', "${configurationName}Classes"
            main "${mainClassName}"
        }
        project.tasks.create('orchidDeploy', OrchidGenerateDeployTask) {
            dependsOn 'classes', "${configurationName}Classes"
            main "${mainClassName}"
        }
        project.tasks.create('orchidRun', OrchidGenerateMainTask) {
            dependsOn 'classes', "${configurationName}Classes"
            main "${mainClassName}"
        }
    }
}

// Task Implementations
//----------------------------------------------------------------------------------------------------------------------

class OrchidGenerateBuildTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchid.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'build', true))
        setStandardInput(System.in)
        super.exec()
    }
}

class OrchidGenerateWatchTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchid.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'watch', true))
        setStandardInput(System.in)
        super.exec()
    }
}

class OrchidGenerateServeTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchid.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'serve', true))
        setStandardInput(System.in)
        super.exec()
    }
}

class OrchidGenerateDeployTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchid.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'deploy', true))
        setStandardInput(System.in)
        super.exec()
    }
}

class OrchidGenerateMainTask extends JavaExec {
    void exec() {
        classpath += project.sourceSets.orchid.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, 'build', false))
        setStandardInput(System.in)
        super.exec()
    }
}