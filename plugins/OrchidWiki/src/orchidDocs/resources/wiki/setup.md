---

pluginVersion: v0.1.7

---

Orchid can be used as a command-line utility, but using it from the command line is quite verbose and so the recommended
setup is to use Gradle to build your projects. Orchid offers an official Gradle plugin which makes the process of using
Orchid dead-simple, configuring many things for you based on the default Gradle project structure, and even plugs directly
into your Java build pipeline, replacing the default Javadoc task with Orchid.

Below is a sample `build.gradle` file which contains everything needed to build your Orchid documentation site.

```groovy

// #1
plugins {
    id 'java'
    id "com.eden.orchidPlugin" version "{{ pluginVersion }}"
}

group 'your.groupId'
version = 'yourVersion'

// #2 
repositories {
    jcenter()
    maven { url 'https://jitpack.io' }
}

// #3 
dependencies {
    ...
    orchidDocsCompile 'io.github.javaeden.orchid:OrchidAll:{{ flags.version }}'
}

// #4
orchid {
    version = "${project.version}"
    theme   = "com.eden.orchid.bsdoc.BSDocTheme"
    baseUrl = "http://www.example.com"
}

// #5 
task sourcesJar(type: Jar, dependsOn: classes) {
    classifier = 'sources'
    from project.sourceSets.main.allSource
}

task javadocJar(type: Jar, dependsOn: javadoc) {
    classifier = 'javadoc'
    from javadoc.destinationDir
}

artifacts {
    archives sourcesJar
    archives javadocJar
}
```

1) Include the 'com.eden.orchidPlugin' plugin for every project you wish to run Orchid with. The plugin is distrubuted
    through the Gradle Plugin repository, so no buildscript dependencies (for Gradle 2.1 and newer)!
    - See https://plugins.gradle.org/plugin/com.eden.orchidPlugin if you're using a Gradle version lower than 2.1, and 
     to find the latest plugin version.
2) Point your repositories to both JCenter and Jitpack. The sources for Orchid are hosted on both JCenter and MavenCentral, 
    but several internal dependencies must be resolved from only JCenter or Jitpack. I do not believe I will be able to 
    support only MavenCentral, but I hope to remove the JitPack repository in th future.
3) The Orchid Gradle plugin adds the 'orchidDocsCompile' dependency configuration. Any plugins you wish to include in 
    your Orchid site should be added as dependencies to 'orchidDocsCompile'.
    - There are several officially-supported Orchid plugins. By using the 'OrchidAll' bundle, you will be automatically
     set up with the core framework and all officially-supported extensions and themes. Alternatively, you can see the
     full list of available pacakges at {{ 'All Orchid Projects' }}, and pick and choose the features
     you want from there.
4) The 'orchid' closure allows you to customize your Orchid build. There are several properties that must be set in 
    order for Orchid to run successfully. The 'theme' must be set as the fully-qualified class name of the Theme you
    wish to use (the theme developer should make it clear what this is). The 'version' and 'baseUrl' properties are very
    useful, and 'baseUrl' is necessary if you wish to publish your documentation somewhere like Github pages where the 
    content root is not the server root and you don't have access to any server-side configuration (such as .htaccess).
    View
5) Generating jars for your javadoc and sources is useful, and necessary if you wish to publish your library on JCenter
    or MavenCentral. Note that some services (like JitPack) will even automatically deploy the contents of your Javadoc 
    jar, but you should make sure the default task ran by those services sets the baseurl of your Orchid closure so that
    it will display correctly.