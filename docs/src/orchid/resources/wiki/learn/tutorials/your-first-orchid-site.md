---
description: 'Learn the basics of setting up and building an Orchid site with Gradle.'
---

## Introduction

So you've decided to give [Orchid](https://orchid.run/) a try? Great! Follow along with this guide to get your 
first Orchid site set up, and learn the basics of what you can do with Orchid.

You can follow along with this tutorial on your own, or find the source for this in the 
[OrchidTutorials repository](https://github.com/orchidhq/OrchidTutorials/tree/master/01). 

## First Steps

Orchid is a tool built in Java and Kotlin, and is designed to be run from Gradle. Gradle is a build tool that uses 
Groovy scripts to configure your build. While it is a super-powerful tool, it can be pretty complex to use and configure 
on its own, which is why Orchid has an official Gradle plugin that makes it easy for you to get up an running with 
Orchid even if you aren't familiar with Gradle. 

### Install Gradle

To start, we'll need to install Gradle. If you're on a Mac, installing Gradle is easily done with Homebrew.

```bash
# Install Homebrew
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

# Use Homebrew to install Gradle 
brew install gradle
```

If you're on another operating system, you can find the steps to install it for your system on Gradle's 
[installation page](https://gradle.org/install/).

### Create a Gradle project

Now that you've got Gradle installed, let's go to an empty directory where you want to work on your Orchid site.

```bash
cd ~/Documents/personal/orchid
```

In this empty folder, run the following command, which will initialize a Gradle project for you and set up all the files
and folders it needs.

```bash
gradle init --type java-library
```

This will make your project look like:

```bash
|__.gradle/
|__gradle/
|__gradlew
|__build.gradle
|__gradlew.bat
|__settings.gradle
|__src
|____test/
|____main/
```

### Add Orchid to your Gradle project

Now that we've got our Gradle project set up, let's configure it to be able to run Orchid. First, we'll need to open up 
`build.gradle` and find the `plugins` section. 

```groovy
plugins {
    // Apply the java-library plugin to add support for Java Library
    id 'java-library'
}
```

Let's add the `com.eden.orchidPlugin` plugin to this block.

```groovy
plugins {
    // Apply the java-library plugin to add support for Java Library
    id 'java-library'
    id 'com.eden.orchidPlugin' version '{{ site.version }}'
}
```

By itself, the Orchid Gradle Plugin isn't quite enough to get Orchid running. We'll also need to tell Gradle:

1. The Orchid plugins and themes we want to include in our site
2. Where it can find those plugins and themes
3. Some basic configuration values so Orchid can run properly

To do this, add the following lines to at the end of `build.gradle`.

```groovy
// 1. Include desired official Orchid plugins and themes
dependencies {
    orchidCompile "io.github.copper-leaf.orchid:orchid-core:{{ site.version }}"
    orchidCompile "io.github.copper-leaf.orchid:orchid-bsdoc-theme:{{ site.version }}"
    orchidCompile "io.github.copper-leaf.orchid:orchid-pages-feature:{{ site.version }}"
    orchidCompile "io.github.copper-leaf.orchid:orchid-asciidoc-feature:{{ site.version }}"
}

// 2. Get Orchid from Jcenter
repositories {
    mavenCentral()
}

// 3. Use the 'BsDoc' theme, and view the site locally at 'http://localhost:8080'
orchid { 
    theme = "BsDoc"
    baseUrl = "http://localhost:8080"
}
```

### Run Orchid

You now have everything you need to run Orchid and view the site. Go back to your terminal and run the following command
to start Orchid and have it host your site with its local HTTP server so you can preview it.

```bash
gradle orchidServe
```

After a few seconds, you should be able to visit [http://localhost:8080](http://localhost:8080) in your browser to see 
your very first Orchid site!

## What's Really Going On?

After starting Orchid, you'll see a lot of stuff get logged to the console. Let's step briefly through the output so you
can understand what's going on.

```text
Using the following modules: 
--------------------
 * com.eden.orchid.StandardModule

Auto-loaded modules: 
--------------------
 * com.eden.orchid.bsdoc.BsDocModule
 * com.eden.orchid.impl.compilers.markdown.FlexmarkModule
 * com.eden.orchid.impl.compilers.pebble.PebbleModule
 * com.eden.orchid.languages.asciidoc.AsciidocModule
 * com.eden.orchid.pages.PagesModule
```

This lets you know which plugins are currently being used. Orchid will auto-load any plugin included in your Gradle 
`dependencies` block, which themselves may include other plugins, and this will let you know exactly what's being used 
in your Orchid site.

```text
Flag values: 
--------------------
-adminTheme: Default
-baseUrl: http://localhost:8080
-defaultTemplateExtension: peb
-dest: /path/to/your/site/build/docs/orchid
-dryDeploy: false
-environment: debug
-logLevel: VERBOSE
-port: 8080
-src: /path/to/your/site/src/orchid/resources
-task: serve
-theme: BsDoc
-version: unspecified
```

This shows the command-line flags passed to Orchid from Gradle. This may be helpful for debugging your build, 
especially if it has been run in a CI environment.

```text
[INFO] Orchid: Running Orchid version {{ site.version }}, site version unspecified in debug environment
[INFO] OrchidWebserver: Webserver Running at http://localhost:8080
[INFO] OrchidWebsocket: Websocket running at http://localhost:8081/
```

This lets you know which version of Orchid you are using, as well as the version of your site. In addition, it will let 
you know which URL your site is currently being served at. By default, this will be `localhost:8080`, but if that port 
is being used by another process, the port will be changed to the nearest free port.

```text
[INFO] TaskServiceImpl: Build Starting...
[INFO] GeneratorServiceImpl: Indexing [10000: assets]
[INFO] GeneratorServiceImpl: Indexing [1000: home]
[INFO] GeneratorServiceImpl: Indexing [1000: pages]
[INFO] GeneratorServiceImpl: Indexing [11: sitemap]
[INFO] GeneratorServiceImpl: Generating [10000: assets]
[INFO] GeneratorServiceImpl: Generating [1000: home]
[INFO] GeneratorServiceImpl: Generating [1000: pages]
[INFO] GeneratorServiceImpl: Generating [11: sitemap]
```

Orchid works in two distinct phases: the _indexing_ phase and the _generation_ phase. During the indexing phase, Orchid 
will let all plugins work together to build a model of what your final site will look like. During the generating phase, 
Orchid returns control to plugins to render all of their pages. This section of output shows you which plugins are being 
indexed and generated in which order. 

By keeping two distinct phases, Orchid can give all plugins a great way to work together to build up your site, but also 
gives everything a guarantee that all the data it needs is there before any pages are rendered. This may not make much 
sense right now, but as you get more familiar with Orchid you will start to see the power that this really gives that no
other static site generator can match. 

The result of all this is that Orchid takes data from many different places (many different plugins), and generates an 
entire website in your destination directory (the `-d` flag) as static HTML, Javascript, CSS, and other related static 
assets. 

```text
Build Metrics: 
┌───────┬────────────┬───────────────┬─────────────────┬───────────────────────────┬─────────────────────────────┐
│       │ Page Count │ Indexing Time │ Generation Time │ Mean Page Generation Time │ Median Page Generation Time │
├───────┼────────────┼───────────────┼─────────────────┼───────────────────────────┼─────────────────────────────┤
│  home │     2      │     50ms      │      334ms      │           166ms           │            322ms            │
├───────┼────────────┼───────────────┼─────────────────┼───────────────────────────┼─────────────────────────────┤
│ TOTAL │          2 │         114ms │           355ms │                     166ms │                       322ms │
└───────┴────────────┴───────────────┴─────────────────┴───────────────────────────┴─────────────────────────────┘

Build Complete
Generated 2 pages in 470ms

Webserver Running at http://localhost:8080
Hit [CTRL-C] to stop the server and quit Orchid
```

Orchid is a very fast static site generator, with build speed on par with the best tools on the market, despite being so
much more powerful and flexible. Generation time is primarily limited by the raw speed of the template engine rendering 
your pages, but this table may help you debug slow builds. Here you can see how many pages Orchid generated, and also
the total time taken indexing and rendering for each plugin. 

The `home` plugin is typically the slowest just because it includes the first page rendered and so the template cache 
warm-up time is included in its generation time. For all plugins, you should expect a mean and median generation time to
be pretty similar, and if they are vastly different you may need to see if any of the pages from that plugin are causing
issues. This can also help you identify entire plugins that are running very slowly. 

The larger sites get, the longer the total build time is, but the mean page generation time tends to drop. This is in 
contrast to most other tools which per-page generation times tend to _increase significantly_ for really large sites. It
is common for the mean generation time to be closer to 20ms or less for individual pages, which means a site of 
thousands of pages could easily be built in just a couple minutes, while other tools could easily take 10 minutes or
more (if they can even handle sites of that scale!).

## Conclusion

So now that we have a bit of an idea of how to start running Orchid, let's open up 
[http://localhost:8080](http://localhost:8080) in our browser. Orchid has built your site into the `build/docs/orchid/`
directory and is serving these pages directly with its own lightweight embedded HTTP server. Since an Orchid site is 
just a bunch of files on disk being served directly (with no additional server-side processing), you can even stop the 
Orchid process and start any HTTP server (like [this one](https://github.com/indexzero/http-server)) in that directory
and you'd get the same result.

Congratulations, you now have a basic Orchid site up and running! Let's go back over what we learned:

1. Orchid is a command-line tool that is run through Gradle. Orchid provides a plugin for Gradle so that it is easy to 
    get set up and running Orchid without needing to know much about Gradle.
2. Orchid is a _static site generator_, which is a tool that takes content and generates and entire website consisting 
    of only HTML, CSS, and other static files that can be hosted from any webserver. 
3. Orchid uses a series of plugins to generate your site, and has a unique way of managing the build lifecycle that 
    actually improves per-page generation times as your site grows, unlike most other tools which tend to slow down 
    dramatically.

Stay tuned for more tutorials on how to use Orchid. We will take what we learned here and expand upon that to learn how 
to add your first pages to start customizing your site and make it unique.
