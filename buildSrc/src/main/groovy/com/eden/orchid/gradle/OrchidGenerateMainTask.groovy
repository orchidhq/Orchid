package com.eden.orchid.gradle

import org.gradle.api.tasks.JavaExec

class OrchidGenerateMainTask extends JavaExec {

    @Override
    void exec() {

        def projectArgs = new ArrayList<String>()

        // set classpath from custom Orchid configuration
        classpath += project.sourceSets.orchidDocs.runtimeClasspath

        // Set the resources directory
        if(project.orchid.srcDir) {
            projectArgs.add "-resourcesDir ${project.orchid.srcDir}"
        }
        else {
            projectArgs.add "-resourcesDir ${project.sourceSets.orchidDocs.resources.srcDirs[0].toString()}"
        }

        // set the output directory
        if(project.orchid.destDir) {
            projectArgs.add "-d ${project.orchid.destDir}"
        }
        else {
            projectArgs.add "-d ${project.buildDir.absolutePath + '/docs/javadoc'}"
        }

        // set common configuration options
        if(project.hasProperty('runTask')) {
            projectArgs.add "-task ${project.property('runTask')}"
        }
        else if(project.orchid.runTask) {
            projectArgs.add "-task ${project.orchid.runTask}"
        }
        if(project.orchid.version) {
            projectArgs.add "-v ${project.orchid.version}"
        }
        if(project.orchid.theme) {
            projectArgs.add "-theme ${project.orchid.theme}"
        }
        if(project.orchid.baseUrl) {
            projectArgs.add "-baseUrl ${project.orchid.baseUrl}"
        }

        // set any additional arguments
        if(project.orchid.args != null && project.orchid.args.size() > 0) {
            for(String arg : project.orchid.args) {
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