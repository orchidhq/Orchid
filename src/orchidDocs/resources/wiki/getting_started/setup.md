Orchid is best used with Gradle. Below is a sample `build.gradle` file which shows how to best set up your library to 
produce an Orchid site as part of the build process. Every important line is commented, and after reading the rest of 
the pages in the 'Getting Started' section, it should make more sense.

```groovy

plugins {
    id 'java' 
}

group 'com.eden'
version = 'v1.0.0' 

// specify 'orchidDocs' configuration so Orchid plugins and resources will not be included in your library
configurations { orchidDocs }
sourceSets     { orchidDocs }

repositories {
    maven { url 'http://jcenter.bintray.com/' }
    
    // make sure to include JitPack as a Maven repository so the Orchid artifacts can be retrieved
    maven { url 'https://jitpack.io' }
}

dependencies {

    project dependencies
    
    ...

    // Always include Orchid Core 
    orchidDocsCompile 'com.github.JavaEden:Orchid-Core:{Orchid Version}'
    
    // Set your chosen theme as a dependency
    orchidDocsCompile 'com.github.JavaEden:OrchidEditorialTheme:{Theme Version}'
    
    // Specify any other desired Extensions as dependencies
    orchidDocsCompile 'com.github.JavaEden:OrchidWiki:{Extension Version}'
}


// Javadoc Generation
//------------------------------------------------------------------------------
javadoc {

    // Specify the sourceSet to parse JavaDoc comments from (don't change)
    source = sourceSets.main.allJava

    // add all 'orchidDocsCompile' dependencies to the doclet classpath
    options.docletpath.addAll(configurations.orchidDocsCompile)
    
    // Set Orchid as the doclet class (don't change)
    options.doclet = 'com.eden.orchid.Orchid'
    
    // Arbitrary options to be passed to Orchid as command-line args

    // Set the theme as the fully-qualified class name of the Theme (required)
    options.addStringOption("theme", "com.eden.orchid.editorial.EditorialTheme")
    
    // Specify an absolute path to be used as the resource directory. Your custom configurations, pages, etc. will live here.
    // If set up exactly as below, it will be in '{project root}/src/orchidDocs/resources' (required)
    options.addStringOption("resourcesDir", sourceSets.orchidDocs.resources.srcDirs[0].toString())

    // Sets the build scripts version as the site version, which can be used to ensure up-to-date version information 
    // in your site (optional)
    options.addStringOption("v", version)
    
    // Sets the baseUrl so links become absolute. If publishing on Github Pages, this is necessary to allow resources
    // to be loaded properly and links to be navigable
    options.addStringOption("baseUrl", "http://localhost:8080")
}

// The following closures are standard Gradle javadoc publishing 
task sourcesJar(type: Jar, dependsOn: classes) {
    classifier = 'sources'
    from sourceSets.main.allSource
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