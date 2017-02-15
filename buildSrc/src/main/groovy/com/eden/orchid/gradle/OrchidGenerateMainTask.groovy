package com.eden.orchid.gradle

import org.gradle.api.tasks.JavaExec

class OrchidGenerateMainTask extends JavaExec {

    @Override
    void exec() {

        def orchid = project.orchid
        def projectArgs = new ArrayList<String>()

        // set classpath from custom Orchid configuration
        if(orchid.includeMainConfiguration) {
            classpath = project.sourceSets.main.runtimeClasspath
            classpath += project.sourceSets.orchidDocs.runtimeClasspath
        }
        else {
            classpath = project.sourceSets.orchidDocs.runtimeClasspath
        }

        // set the Main class
        setMain('com.eden.orchid.Orchid')

        // Set the resources directory
        if(orchid.srcDir) {
            projectArgs.add "-resourcesDir ${orchid.srcDir}"
        }
        else {
            projectArgs.add "-resourcesDir ${project.sourceSets.orchidDocs.resources.srcDirs[0].toString()}"
        }

        // set the output directory
        if(orchid.destDir) {
            projectArgs.add "-d ${orchid.destDir}"
        }
        else {
            projectArgs.add "-d ${project.buildDir.absolutePath + '/docs/javadoc'}"
        }

        // set common configuration options
        if(orchid.task) {
            projectArgs.add "${orchid.task}"
        }
        if(orchid.version) {
            projectArgs.add "-v ${orchid.version}"
        }
        if(orchid.theme) {
            projectArgs.add "-theme ${orchid.theme}"
        }
        if(orchid.baseUrl) {
            projectArgs.add "-baseUrl ${orchid.baseUrl}"
        }

        if(orchid.disabledGenerators != null && orchid.disabledGenerators.size() > 0) {
            projectArgs.add "-disabledGenerators ${String.join(":", orchid.disabledGenerators)}"
        }

        // set any additional arguments
        if(orchid.args != null && orchid.args.size() > 0) {
            for(String arg : orchid.args) {
                String[] option = arg.split("\\s+")

                if(option.length == 2) {
                    projectArgs.add "-${option[0]} ${option[1]}"
                }
            }
        }

        args(projectArgs)

        // let superclass execute Main class
        super.exec()
    }
}