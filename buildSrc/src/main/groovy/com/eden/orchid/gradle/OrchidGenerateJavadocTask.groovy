package com.eden.orchid.gradle

import org.gradle.api.tasks.javadoc.Javadoc

class OrchidGenerateJavadocTask extends Javadoc {

    OrchidGenerateJavadocTask() {
        source = [project.sourceSets.main.allJava.getSrcDirs()]
    }

    @Override
    protected void generate() {
        def orchid = project.orchid

        // set classpath from custom Orchid configuration
        if(orchid.includeMainConfiguration) {
            setClasspath(project.sourceSets.main.runtimeClasspath + project.sourceSets.orchidDocs.runtimeClasspath)
            options.docletpath.addAll(project.configurations.getByName('compile'))
            options.docletpath.addAll(project.configurations.getByName('orchidDocsCompile'))
        }
        else {
            setClasspath(project.sourceSets.orchidDocs.runtimeClasspath)
            options.docletpath.addAll(project.configurations.getByName('orchidDocsCompile'))
        }

        // set the Doclet class
        options.doclet = 'com.eden.orchid.Orchid'

        // Set the resources directory
        if(orchid.srcDir) {
            options.addStringOption("resourcesDir", orchid.srcDir)
        }
        else {
            options.addStringOption("resourcesDir", project.sourceSets.orchidDocs.resources.srcDirs[0].toString())
        }

        // set the output directory
        if(orchid.destDir) {
            options.addStringOption("d", orchid.destDir)
        }
        else {
            options.addStringOption("d", project.buildDir.absolutePath + '/docs/javadoc')
        }

        // set common configuration options
        if(orchid.version) {
            options.addStringOption("v", orchid.version)
        }
        if(orchid.theme) {
            options.addStringOption("theme", orchid.theme)
        }
        if(orchid.baseUrl) {
            options.addStringOption("baseUrl", orchid.baseUrl)
        }

        if(orchid.disabledGenerators != null && orchid.disabledGenerators.size() > 0) {
            options.addStringOption("disabledGenerators", String.join(":", orchid.disabledGenerators))
        }

        // set any additional arguments
        if(orchid.args != null && orchid.args.size() > 0) {
            for(String arg : orchid.args) {
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
