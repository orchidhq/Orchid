package com.eden.orchid.gradle

class OrchidPluginExtension {

    // required flags
    String srcDir
    String destDir
    String runTask
    String version
    String theme
    String baseUrl
    String environment
    String dryDeploy
    int port

    // optional flags
    String azureToken
    String githubToken
    String gitlabToken
    String bitbucketToken
    String netlifyToken

    // user-provided flags
    List<String> args = new ArrayList<String>()
}
