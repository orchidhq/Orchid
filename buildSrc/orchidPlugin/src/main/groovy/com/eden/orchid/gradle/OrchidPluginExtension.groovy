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
    int port
    String githubToken
    List<String> args = new ArrayList<String>()
}
