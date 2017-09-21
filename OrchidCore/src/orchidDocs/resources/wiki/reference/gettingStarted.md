---

title: Getting Started

---

Orchid was designed to improve documentation for technical projects, and while it's not just useful for software 
developers, it helps to be comfortable working in a terminal and to know a bit about build management systems.

Orchid was created be used with Gradle, and while Orchid could be run without it, it is best to use Gradle because the 
official Orchid Gradle Plugin makes it simple to manage your Orchid project while keeping it separate from the project
it is documenting. It includes several tasks which make it easy to build your Orchid site, and even hooks Orchid into
the Java build process to replace Javadoc with Orchid.

To get started, pick a Bundle (OrchidAll, OrchidBlog, or OrchidProduct) or manually choose your desired Orchid plugins. 
You may pick a bundle to start with and add any number of plugins afterward, both official and unofficial. Then, setup 
your project's `build.gradle` file like so:

```groovy

plugins {
    // Add the official Orchid Gradle plugin so you can use Orchid with the custom DSL
    id: 'io.github.javaeden.orchid:orchidPlugin:{{site.version}}'
}

repositories {
    // Orchid uses dependencies from both Jcenter and Jitpack, so both must be included. jcenter also includes 
    // everything available from MavenCentral, while Jitpack makes accessible any Github project.
    jcenter() 
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Add an Orchid Bundle. OrchidAll comes with all official themes included.
    // You must include a theme separately when using the OrchidBlog and OrchidProduct bundles 
    orchidDocsCompile 'io.github.javaeden.orchid:OrchidAll:{{site.version}}'
}

orchid {
    // Version, theme, and baseUrl are required
    version = "${project.version}"
    theme   = "{{flags.theme}}"
    
    // Add this is you are developing a theme or plugin to include its own `main module` sources in the Orchid build
    includeMainConfiguration = true
    
    // The following properties are optional
    
    baseUrl = "{{site.baseUrl}}"                  // a baseUrl appended to all generated links. Defaults to '/'
    srcDir  = "path/to/new/source/directory"      // defaults to 'src/orchidDocs/resources'
    destDir = "path/to/new/destination/directory" // defaults to 'build/docs/javadoc'
    runTask = "build"                             // specify a task to run with 'gradle orchidRun'
}


```

You can now run Orchid in the following ways:

1) `gradle orchidRun` - Runs an Orchid task. The `runTask` should be specified in `build.gradle` or passed as a Gradle
    project property (`-PrunTask=build`). The task `listTasks` will show a list of all tasks that can be run given the 
    plugins currently installed. Similarly, `listOptions` will list all options that can be set through Gradle. 
2) `gradle orchidBuild` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in 
    `build/docs/javadoc` unless the output directory has been changed.
3) `gradle orchidServe` - Sets up a development server and watches files for changes. Must have the `OrchidServer` 
    plugin installed for this task to work, which is included in all the above bundles.
4) If you are developing a Java application, Orchid replaces the standard Javadoc task with its own `build` task. In 
    addition to running the standard Orchid build, when Orchid is run from Javadoc it will be able to create pages 
    for all your project's classes and packages, just like you'd expect from a normal Javadoc site, but embedded within
    your chosen Orchid theme. You must have the `OrchidJavadoc` plugin installed for this to work properly.

Congrats, you've now got a fully-working Orchid site! Read on to learn more about site configuration, and refer to the 
documentation for all your plugins to learn more about how to use each one.