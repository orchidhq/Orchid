package com.eden.orchid.gradle

import org.gradle.api.Project

class OrchidPluginHelpers {

    public static ArrayList<String> getOrchidProjectArgs(Project project, String defaultTask, boolean forceTask) {
        def projectArgs = new ArrayList<String>()

        def projectArgsMap = getOrchidProjectArgsMap(project, defaultTask, forceTask)

        for (Map.Entry<String, String> entry : projectArgsMap.entrySet()) {
            projectArgs.add "-${entry.key} ${entry.value}"
        }

        return projectArgs
    }

    public static HashMap<String, String> getOrchidProjectArgsMap(Project project, String defaultTask, boolean forceTask) {
        def projectArgs = new HashMap<String, String>()

        projectArgs.put "src",          getPropertyValue(project, 'srcDir', project.sourceSets.orchid.resources.srcDirs[0].toString())
        projectArgs.put "dest",         getPropertyValue(project, 'destDir', project.buildDir.absolutePath + '/docs/orchid')
        projectArgs.put "version",      getPropertyValue(project, 'version', project.version)
        projectArgs.put "theme",        getPropertyValue(project, 'theme', 'Default')
        projectArgs.put "baseUrl",      getPropertyValue(project, 'baseUrl', '')
        projectArgs.put "environment",  getPropertyValue(project, 'environment', 'debug')
        projectArgs.put "task",         (forceTask) ? defaultTask : getPropertyValue(project, 'runTask', defaultTask)
        projectArgs.put "dryDeploy",    getPropertyValue(project, 'dryDeploy', 'false')

        if(project.orchid.args != null && project.orchid.args.size() > 0) {
            for(String arg : project.orchid.args) {
                String[] option = arg.split("\\s+")

                if(option.length == 2) {
                    projectArgs.put option[0], option[1]
                }
            }
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
