package com.eden.orchid.gradle

class OrchidPluginExtension {
    boolean includeMainConfiguration
    String srcDir
    String destDir
    String task
    String version
    String theme
    String baseUrl
    List<String> disabledGenerators = new ArrayList<String>()
    List<String> args = new ArrayList<String>()
}
