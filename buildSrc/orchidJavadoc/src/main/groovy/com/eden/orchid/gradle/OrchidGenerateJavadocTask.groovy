package com.eden.orchid.gradle

import org.gradle.api.tasks.javadoc.Javadoc

class OrchidGenerateJavadocTask extends Javadoc {

    OrchidGenerateJavadocTask() {
        source = [project.sourceSets.main.allJava.getSrcDirs()]
        dependsOn 'classes', "${OrchidPlugin.configurationName}Classes"
        onlyIf {
            !(project.hasProperty('noJavadoc') && project.property('noJavadoc')) && !project.orchid.noJavadoc
        }

        destinationDir = project.file(OrchidPluginHelpers.getPropertyValue(project, 'destDir', project.buildDir.absolutePath + '/docs/orchid'))
    }

    @Override
    protected void generate() {
        options.doclet = 'com.eden.orchid.javadoc.OrchidJavadoc'

        // set classpath from custom Orchid configuration
        options.docletpath.addAll(project.configurations.getByName('orchidCompile'))
        classpath += project.sourceSets.orchid.runtimeClasspath
        options.docletpath.addAll(classpath.files)

        // set common configuration options
        options.addStringOption "resourcesDir", OrchidPluginHelpers.getPropertyValue(project, 'srcDir', project.sourceSets.orchid.resources.srcDirs[0].toString())
        options.addStringOption "v",            OrchidPluginHelpers.getPropertyValue(project, 'version', project.version)
        options.addStringOption "theme",        OrchidPluginHelpers.getPropertyValue(project, 'theme', 'Default')
        options.addStringOption "baseUrl",      OrchidPluginHelpers.getPropertyValue(project, 'baseUrl', '')
        options.addStringOption "environment",  OrchidPluginHelpers.getPropertyValue(project, 'environment', 'debug')
        options.addStringOption "task",         OrchidPluginHelpers.getPropertyValue(project, 'runTask', 'build')

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
