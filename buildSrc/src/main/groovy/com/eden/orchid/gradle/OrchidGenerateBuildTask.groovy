package com.eden.orchid.gradle

import org.gradle.api.tasks.JavaExec

class OrchidGenerateBuildTask extends JavaExec {

    @Override
    void exec() {

        def projectArgs = new ArrayList<String>()

        // set classpath from custom Orchid configuration
        if(project.orchid.includeMainConfiguration) {
            classpath += project.sourceSets.main.runtimeClasspath
        }
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

        projectArgs.add "build"

        if(project.orchid.version) {
            projectArgs.add "-v ${project.orchid.version}"
        }
        if(project.orchid.theme) {
            projectArgs.add "-theme ${project.orchid.theme}"
        }
        if(project.orchid.baseUrl) {
            projectArgs.add "-baseUrl ${project.orchid.baseUrl}"
        }

        if(project.orchid.disabledGenerators != null && project.orchid.disabledGenerators.size() > 0) {
            projectArgs.add "-disabledGenerators ${String.join(":", project.orchid.disabledGenerators)}"
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