package com.eden.orchid.gradle

import org.gradle.api.Project

class OrchidPluginHelpers {

    public static ArrayList<String> getOrchidProjectArgs(Project project, String defaultTask, boolean forceTask) {
        def projectArgs = new ArrayList<String>()

        projectArgs.addAll([
                "--baseUrl",     getPropertyValue(project, 'baseUrl', ''),
                "--src",         getPropertyValue(project, 'srcDir', project.sourceSets.orchid.resources.srcDirs[0].toString()),
                "--dest",        getPropertyValue(project, 'destDir', project.buildDir.absolutePath + '/docs/orchid'),
                "--task",        (forceTask) ? defaultTask : getPropertyValue(project, 'runTask', defaultTask),
                "--theme",       getPropertyValue(project, 'theme', 'Default'),
                "--version",     getPropertyValue(project, 'version', project.version.toString()),
                "--environment", getPropertyValue(project, 'environment', 'debug'),
                "--dryDeploy",   getPropertyValue(project, 'dryDeploy', 'false'),
                "--port",        getPropertyValue(project, 'port', '8080'),
                "--diagnose",    getPropertyValue(project, 'diagnose', 'false'),
        ])

        def optionalFlags = ['azureToken', 'githubToken', 'gitlabToken', 'bitbucketToken', 'netlifyToken']
        for(def optionalFlag : optionalFlags) {
            def flagValue = getPropertyValue(project, optionalFlag, '')
            if(!flagValue.trim().isEmpty()) {
                projectArgs.addAll(["--$optionalFlag", flagValue])
            }
        }

        if (project.orchid.args != null && project.orchid.args.size() > 0) {
            projectArgs.addAll(project.orchid.args)
        }

        return projectArgs
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
