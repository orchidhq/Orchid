package com.eden.orchid.gradle

import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.javadoc.Javadoc

class OrchidGenerateJavadocTask extends Javadoc {

    OrchidGenerateJavadocTask() {
        dependsOn 'classes', "${OrchidJavadocPlugin.configurationName}Classes"
        onlyIf {
            !(project.hasProperty('noJavadoc') && project.property('noJavadoc')) && !project.orchid.noJavadoc
        }

        destinationDir = project.file(getPropertyValue(project, 'destDir', project.buildDir.absolutePath + '/docs/orchid'))
    }

    @InputFiles
    @SkipWhenEmpty
    public FileTree getSource() {
        def setSources = []
        if(project.orchidJavadoc.sources != null && project.orchidJavadoc.sources.size() > 0) {
            setSources = project.orchidJavadoc.sources
        }
        else {
            setSources = project.sourceSets.main.allJava.getSrcDirs()
        }

        def allFiles = []
        for(String source : setSources) {
            project.fileTree(source).visit { FileVisitDetails details ->
                if(details.file.path.endsWith(".java")) {
                    allFiles << details.file.path
                }
            }
        }

        source = [allFiles]

        return super.getSource()
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
        options.addStringOption "dryDeploy",    getPropertyValue(project, 'dryDeploy', 'false')

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
