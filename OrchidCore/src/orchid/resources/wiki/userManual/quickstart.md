---
---

## Start a new Orchid project

The simplest way to get started with Orchid is to use the Orchid Starter repo as a base. 

1) `git clone https://github.com/JavaEden/OrchidStarter.git`
    * Clone the Starter repo anywhere you want on your system
2) `cd OrchidStarter`
    * Navigate into the directory containing the Starter repo
3) `./gradlew orchidServe`
    * Run Orchid using the included Gradle wrapper. No complicated and brittle gemfiles, NPM packages, or anything else 
    required. As long as you have a Java JDK installed, Orchid works right out-of-the-box without any configuration or
    system packages to install.
    * All available commands:
        - `gradle orchidBuild` - Build Orchid once then exit
        - `gradle orchidWatch` - Watch Orchid for changes, rebuilding whenever a file changes. 
        - `gradle orchidServe` - Start a local development server to view your output site. Also watches Orchid for changes, rebuilding whenever a file changes.
        - `gradle assemble` - Not strictly an Orchid command, but if you are set up with the OrchidJavadoc plugin, this will run Orchid from the Javadoc tool instead of generating the standard Javadocs. 

## Deploy to Netlify
    
Alternatively, you can simply click the "Deploy to Netlify" button below to automatically clone, build, and deploy the 
Starter repo to the Netlify CDN. 

[![Deploy to Netlify](https://www.netlify.com/img/deploy/button.svg)](https://app.netlify.com/start/deploy?repository=https://github.com/JavaEden/OrchidStarter)
    
## Integrate Orchid into an existing Gradle project

The Starter repo is great if you are setting up Orchid as a standalone website, but Orchid was designed to be integrated
into any project. Here's how to add Orchid to an existing Gradle project and use it to start building really, really, 
ridiculously good-looking Javadocs, wikis, and more. 

**Step 1**

Add the [Orchid Gradle Plugin](https://plugins.gradle.org/plugin/com.eden.orchidPlugin) to your `build.gradle`

The plugin adds several new tasks to run Orchid in various modes, along with replacing the Javadoc task with Orchid. It
also opens up a configuration block where you can set options such as the theme and input/output directories of Orchid. 
The plugin is explained in more detail in the [Advanced Configuration](#) section.

**Step 2**

Add the following lines to your `dependencies` block:

```groovy
dependencies {
    compile "io.github.javaeden.orchid:OrchidCore:{{site.version}}"
    orchidCompile "io.github.javaeden.orchid:OrchidAll:{{site.version}}"
}
```

The dependency in `orchidCompile` adds all official Orchid core packages, themes, and plugins for ease of setup. You
may instead choose which specific packages you want to install, which are listed on the [homepage]({{site.baseUrl}}). 

The dependency in `compile` is optional, but is needed if you intend to create plugins specifically for this project.

**Step 3**

Add the following block to the top-level of your `build.gradle`:

```groovy
orchid {
    version = "${project.version}" 
    theme = "FutureImperfect" // or whatever theme you choose
    baseUrl = "http://localhost:8080" // you may want to change this when deploying to production
}
```

**Step 4**

Add the following folder in your project: `src/orchid/resources`. This is where all your site configuration and 
content lives.

**Step 5**

Add a `config.yml` file inside `src/orchid/resources`.


And that's it! You can now run Orchid using any of the commands listed above. You should now refer to the documentation 
for your theme, all your plugins, and the OrchidCore to see what you can configure with your specific build, and how to 
add content to your plugins. 