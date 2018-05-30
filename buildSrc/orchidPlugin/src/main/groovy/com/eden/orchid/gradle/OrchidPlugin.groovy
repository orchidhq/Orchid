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
        project.tasks.create('orchidWatch',  OrchidGenerateWatchTask)
        project.tasks.create('orchidServe',  OrchidGenerateServeTask)
        project.tasks.create('orchidDeploy', OrchidGenerateDeployTask)
        project.tasks.create('orchidShell',  OrchidGenerateShellTask)
        project.tasks.create('orchidRun',    OrchidGenerateMainTask)
    }
}

// Task Implementations
//----------------------------------------------------------------------------------------------------------------------

class OrchidGenerateBuildTask extends OrchidGenerateMainTask {
    OrchidGenerateBuildTask() { super("build", true) }
}

class OrchidGenerateWatchTask extends OrchidGenerateMainTask {
    OrchidGenerateWatchTask() { super("watch", true) }
}

class OrchidGenerateServeTask extends OrchidGenerateMainTask {
    OrchidGenerateServeTask() { super("serve", true) }
}

class OrchidGenerateDeployTask extends OrchidGenerateMainTask {
    OrchidGenerateDeployTask() { super("deploy", true) }
}

class OrchidGenerateShellTask extends OrchidGenerateMainTask {
    OrchidGenerateShellTask() { super("interactive", true) }
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