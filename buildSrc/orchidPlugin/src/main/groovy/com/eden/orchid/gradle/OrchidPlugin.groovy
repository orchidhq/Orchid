package com.eden.orchid.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec

class OrchidPlugin implements Plugin<Project> {
    public static String configurationName = 'orchid'
    public static String mainClassName = 'com.eden.orchid.Orchid'

    void apply(Project project) {
        // create Orchid closure, configuration, and sourceSet
        project.apply plugin: "java"
        project.extensions.create('orchid', OrchidPluginExtension)
        project.configurations.create(configurationName)

        project.sourceSets {
            orchid
        }

        project.tasks.create('orchidBuild',  OrchidGenerateBuildTask)
        project.tasks.create('orchidServe',  OrchidGenerateServeTask)
        project.tasks.create('orchidDeploy', OrchidGenerateDeployTask)
        project.tasks.create('orchidRun',    OrchidGenerateMainTask)
    }
}

// Task Implementations
//----------------------------------------------------------------------------------------------------------------------

class OrchidGenerateBuildTask extends OrchidGenerateMainTask {
    OrchidGenerateBuildTask() {
        super("build", true)
        description = 'Runs the main Orchid build process'
    }
}

class OrchidGenerateServeTask extends OrchidGenerateMainTask {
    OrchidGenerateServeTask() {
        super("serve", true)
        description = 'Sets up a development server and watches the source directory, and rebuilds the site on changes'
    }
}

class OrchidGenerateDeployTask extends OrchidGenerateMainTask {
    OrchidGenerateDeployTask() {
        super("deploy", true)
        description = 'Runs the main Orchid build process then publishes the results'
    }
}

class OrchidGenerateShellTask extends OrchidGenerateMainTask {
    OrchidGenerateShellTask() {
        super("interactive", true)
        description = 'Open the Orchid interactive shell'
    }
}

class OrchidGenerateMainTask extends JavaExec {

    private final String command
    private final boolean force

    OrchidGenerateMainTask() {
        this('build', false)
    }

    protected OrchidGenerateMainTask(String command, boolean force) {
        this.command = command
        this.force = force
        this.group = "Orchid"
        this.description = 'Runs an Orchid task specified by -PorchidRunTask'

        dependsOn 'classes', "${OrchidPlugin.configurationName}Classes"
        main = "${OrchidPlugin.mainClassName}"
    }

    void exec() {
        classpath += project.sourceSets.orchid.runtimeClasspath
        args(OrchidPluginHelpers.getOrchidProjectArgs(project, command, force))
        setStandardInput(System.in)
        setStandardOutput(System.out)
        super.exec()
    }
}
