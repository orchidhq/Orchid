package com.eden.orchid.gradle

class OrchidPluginExtension {
    String srcDir
    String destDir
    String runTask
    String version
    String theme
    String baseUrl
    String environment
    String dryDeploy
    List<String> args = new ArrayList<String>()
}
