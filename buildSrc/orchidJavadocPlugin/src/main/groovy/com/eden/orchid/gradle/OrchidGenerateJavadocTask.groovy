package com.eden.orchid.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.javadoc.Javadoc

class OrchidGenerateJavadocTask extends Javadoc {

    OrchidGenerateJavadocTask() {
        source = [project.sourceSets.main.allJava.getSrcDirs()]
        dependsOn 'classes', "${OrchidJavadocPlugin.configurationName}Classes"
        onlyIf {
            !(project.hasProperty('noJavadoc') && project.property('noJavadoc')) && !project.orchid.noJavadoc
        }

        destinationDir = project.file(getPropertyValue(project, 'destDir', project.buildDir.absolutePath + '/docs/orchid'))
    }

    @Override
    protected void generate() {
        options.doclet = OrchidJavadocPlugin.mainClassName

        // set classpath from custom Orchid configuration
        options.docletpath.addAll(project.configurations.getByName('orchidCompile'))
        classpath += project.sourceSets.orchid.runtimeClasspath
        options.docletpath.addAll(classpath.files)

        // set common configuration options
        options.addStringOption "resourcesDir", getPropertyValue(project, 'srcDir', project.sourceSets.orchid.resources.srcDirs[0].toString())
        options.addStringOption "v",            getPropertyValue(project, 'version', project.version)
        options.addStringOption "theme",        getPropertyValue(project, 'theme', 'Default')
        options.addStringOption "baseUrl",      getPropertyValue(project, 'baseUrl', '')
        options.addStringOption "environment",  getPropertyValue(project, 'environment', 'debug')
        options.addStringOption "task",         getPropertyValue(project, 'runTask', 'build')

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

    public static String getPropertyValue(Project project, String propName, String defaultValue) {
        if(project.hasProperty('orchid' + propName.capitalize())) {
            return project.property('orchid' + propName.capitalize())
        }
        else if(project.orchid."$propName") {
            return project.orchid."$propName"
        }
        else {
            return defaultValue
        }
    }
}
