package com.eden.orchid.gradle

import org.gradle.api.tasks.javadoc.Javadoc

class OrchidGenerateJavadocTask extends Javadoc {

    OrchidGenerateJavadocTask() {
        source = [project.sourceSets.main.allJava.getSrcDirs()]
        dependsOn 'classes', "${OrchidPlugin.configurationName}Classes"
        onlyIf { !(project.hasProperty('noJavadoc') && project.property('noJavadoc')) }
    }

    @Override
    protected void generate() {
        options.doclet = 'com.eden.orchid.javadoc.OrchidJavadoc'

        // set classpath from custom Orchid configuration
        options.docletpath.addAll(project.configurations.getByName('orchidDocsCompile'))
        classpath += project.sourceSets.orchidDocs.runtimeClasspath
        options.docletpath.addAll(classpath.files)

        // Set the resources directory
        if(project.orchid.srcDir) {
            options.addStringOption("resourcesDir", project.orchid.srcDir)
        }
        else {
            options.addStringOption("resourcesDir", project.sourceSets.orchidDocs.resources.srcDirs[0].toString())
        }

        // set the output directory
        if(project.orchid.destDir) {
            options.addStringOption("d", project.orchid.destDir)
        }

        // set common configuration options
        if(project.orchid.version) {
            options.addStringOption("v", project.orchid.version)
        }
        if(project.orchid.theme) {
            options.addStringOption("theme", project.orchid.theme)
        }
        if(project.orchid.baseUrl) {
            options.addStringOption("baseUrl", project.orchid.baseUrl)
        }

        // set any additional arguments
        if(project.orchid.args != null && project.orchid.args.size() > 0) {
            for(String arg : project.orchid.args) {
                String[] option = arg.split("\\s+")

                if(option.length == 2) {
                    options.addStringOption(option[0], option[1])
                }
            }
        }

        // let superclass execute Doclet class
        super.generate()
    }
}
