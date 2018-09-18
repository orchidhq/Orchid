---
---

{% extends '_wikiBase' %}

{% block sectionBody %}
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
        - `gradle orchidBuild` - Build Orchid once then exit.
        - `gradle orchidWatch` - Watch Orchid for changes, rebuilding whenever a file changes. 
        - `gradle orchidServe` - Start a local development server to view your output site. Also watches Orchid for changes, rebuilding whenever a file changes.
        - `gradle orchidDeploy` - Build Orchid once, deploy the built site through the publication pipeline, then exit.
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

Add the [Orchid Gradle Plugin](https://plugins.gradle.org/plugin/com.eden.orchidPlugin) to your `build.gradle`.

The plugin adds several new tasks to run Orchid in various modes, along with replacing the Javadoc task with Orchid. It
also opens up a configuration block where you can set options such as the theme and input/output directories of Orchid. 
The plugin is explained in more detail in the {{anchor('Advanced Configuration')}} section.

**Step 2**

Add the following to your `dependencies` and `repositories` blocks:

{% highlight 'groovy' %}
repositories {
    jcenter()
    maven { url "https://kotlin.bintray.com/kotlinx" }
    maven { url 'https://dl.bintray.com/javaeden/Orchid/' }
    maven { url 'https://dl.bintray.com/javaeden/Eden/' }
    maven { url 'https://jitpack.io' }
}
dependencies {
    // 'compile' to distribute your plugin 
    compile "io.github.javaeden.orchid:OrchidCore:{{site.version}}"
    // 'orchidCompile' for creating private plugins
    orchidCompile "io.github.javaeden.orchid:OrchidCore:{{site.version}}"
    // 'orchidRuntime' for the plugins you want to use
    orchidRuntime "io.github.javaeden.orchid:OrchidAll:{{site.version}}"
}
{% endhighlight %}

The dependency in `orchidRuntime` adds all official Orchid core packages, themes, and plugins for ease of setup. You
may instead choose which specific packages you want to install, which are listed on the [homepage]({{site.baseUrl}}). 

The dependency in `compile` is optional, but is needed if you intend to create plugins to share with the community. You
may also use the `orchidCompile` dependency if you just want to develop plugins for private use, by keeping plugin code
iun you `orchid` source folder along with your content.

{% alert level='info' headline='Note on repositories' :: compileAs('md') %}
Orchid currently hosts its own artifacts on Bintray, but I am having issues getting them synced to JCenter properly. 
Using a `maven` repository at `https://dl.bintray.com/javaeden/Orchid/` will ensure Gradle will always be able to 
resolve all artifacts, and will also be available immediately after a new version is released (it usually takes them a 
bit longer to sync to JCenter).

In addition, Orchid has transitive dependencies hosted on Jitpack, though eventually Orchid will only depend on  
artifacts that can be resolved through JCenter.
{% endalert %}

**Step 3**

Add the following block to the top-level of your `build.gradle`:

{% highlight 'groovy' %}
orchid {
    version = "${project.version}" 
    theme = "FutureImperfect" // or whatever theme you choose
    baseUrl = "http://localhost:8080" // you may want to change this when deploying to production, typically from an environment variable in your CI build
}
{% endhighlight %}

**Step 3.5 (Optional, but necessary to document Android projects)**

The Gradle Android plugin does not extend the `java` plugin, so Android modules cannot directly use Orchid. However, you
can create a _separate_ module in your Gradle build just for Orchid and point it at your Android java sources to 
generate Javadoc for them.

{% highlight 'groovy' %}
// in your Orchid module's build.gradle
orchidJavadoc {
    sources = [file("${project('app')}}/src/main/java")]
}
{% endhighlight %}

This does not change the resouce dir, but just tells Javadoc where to look for Java sources, and this information gets 
passed back to Orchid. You can also use this technique to include Javadoc documentation from multiple Gradle modules in
one Orchid build, if desired.

**Step 4**

Add the following folder in your project: `src/orchid/resources`. This is where all your site configuration and 
content lives.

**Step 5**

Add a `config.yml` file inside `src/orchid/resources`.


And that's it! You can now run Orchid using any of the commands listed above. You should now refer to the documentation 
for your theme, all your plugins, and the OrchidCore to see what you can configure with your specific build, and how to 
add content to your plugins. 

{% endblock %}