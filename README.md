
# Orchid

[![All Platforms Build Status](https://casey-brooks.visualstudio.com/Orchid/_apis/build/status/Orchid-Gradle-CI?branchName=dev "All Platforms Build Status")](https://casey-brooks.visualstudio.com/Orchid/_build/latest?definitionId=2?branchName=dev)
[![Linux and Mac Build Status](https://travis-ci.org/JavaEden/Orchid.svg?branch=dev "Linux and Mac Build Status")](https://travis-ci.org/JavaEden/Orchid)
[![Windows Build status](https://ci.appveyor.com/api/projects/status/0358qdkmfhbqedo1/branch/dev?svg=true "Windows Build status")](https://ci.appveyor.com/project/cjbrooks12/orchid/branch/dev)
[![Current Version](https://api.bintray.com/packages/javaeden/Orchid/OrchidCore/images/download.svg "Current Version") ](https://bintray.com/javaeden/Orchid/OrchidCore/_latestVersion)
[![License: LGPL-3.0](https://img.shields.io/badge/License-LGPL%20v3-blue.svg "Licensed under LGPL-3.0")](http://www.gnu.org/licenses/lgpl-3.0)
[![Codacy Grade](https://api.codacy.com/project/badge/Grade/8bca7e84b6094c03ae1316278cf63ae1 "Codacy Grade")](https://www.codacy.com/app/cjbrooks12/Orchid?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=JavaEden/Orchid&amp;utm_campaign=Badge_Grade)
[![Codacy Coverage](https://api.codacy.com/project/badge/Coverage/8bca7e84b6094c03ae1316278cf63ae1 "Codacy Coverage")](https://www.codacy.com/app/cjbrooks12/Orchid?utm_source=github.com&utm_medium=referral&utm_content=JavaEden/Orchid&utm_campaign=Badge_Coverage)
[![Gitter chat](https://img.shields.io/gitter/room/nwjs/nw.js.svg "Gitter Chat")](https://gitter.im/JavaEden/Orchid)
[![Backers on Open Collective](https://opencollective.com/orchidssg/backers/badge.svg)](#backers) 
[![Sponsors on Open Collective](https://opencollective.com/orchidssg/sponsors/badge.svg)](#sponsors) 

> A beautiful and truly unique documentation engine and static site generator.

![Example Orchid site](https://orchid.netlify.com/assets/media/sample.jpg)

Orchid is a brand-new, general-purpose static site generator for Java and Kotlin, with a focus on extensibility and 
aimed at developers looking to improve their technical documentation. Orchid was born out of a desire for better-looking 
Javadocs and frustration with how difficult is it to manage large Jekyll sites and keep it up-to-date with your code.

Orchid supports a variety of plugins, including a wiki, static pages, blogs, and much more. It aims to have high 
compatibility with many of the existing static site generators, such as Jekyll, Gitbook, and Hugo, so that migration to 
Orchid is painless. And if you can't find a plugin to do what you need, Orchid provides an intuitive way to add your own 
private plugins and a rich API so you can make your site as beautiful and unique as an Orchid.

### Features

- Plugin-driven architecture
- Multi-theme support
- Admin interface to manage your content and gain deep insights into your build
- Link to other Orchid sites
- Powerful and flexible indexing, unique to Orchid
- Full-text static search
- Component-based content management
- Integrated deployment pipeline
- Fully replaces Jekyll, Hugo, Gitbook, Javadocs, and more!

[View the full documentation](https://orchid.netlify.com) or see the quick-start below.

## Table of Contents

1. [Quick Start](#quick-start)
    1. [Gradle](#configuring-a-gradle-project)
    1. [Maven](#configuring-a-maven-project)
    1. [KScript](#using-scriptlets)
1. [Example Orchid Sites](#example-orchid-sites)
1. [Development Progress](#development-progress)
    1. [Core Packages](#core-packages)
    1. [Themes](#themes)
    1. [Plugins](#plugins)
1. [License](#license)
1. [Contributing](#contributing)
1. [Contact](#contact)

## Quick Start

Orchid integrates with any new or existing Gradle/Maven project. The simplest way to get started is to deploy the 
[starter repo](https://github.com/JavaEden/OrchidStarter) directly to Netlify. Just click the button below to 
automatically clone this repo and deploy it to Netlify. The starter repo includes the 
[Netlify CMS](https://www.netlifycms.org/), so you will be up and publishing content as soon as possible. You will need 
to set the Github user/repo in `src/orchid/resources/config.yml`, but the rest of the CMS config is automatically 
generated based on your current Orchid plugins and configurations. 

[![Deploy to Netlify](https://www.netlify.com/img/deploy/button.svg)](https://app.netlify.com/start/deploy?repository=https://github.com/JavaEden/OrchidStarter)

To run Orchid locally, the only system dependency necessary is a valid Java 8 JDK. Orchid can be integrated with any new
 or existing Gradle or Maven project or bootstrapped manually in any JVM-based scriptlet (such as 
[kscript](https://github.com/holgerbrandl/kscript)). To get started, pick a Bundle (OrchidAll or OrchidBlog) or manually 
choose your desired Orchid plugins. You may pick a bundle to start with and add any number of plugins afterward, both 
official and unofficial. 

### Configuring a Gradle project

To use Orchid from a Gradle project, setup your project's build.gradle file like so:

```groovy
plugins {
    // Add the official Orchid Gradle plugin so you can use Orchid with the custom DSL   
    id "com.eden.orchidPlugin" version "{version}"
}

repositories {
    // Orchid uses dependencies from both Jcenter and Jitpack, so both must be included. jcenter also includes 
    // everything available from MavenCentral, while Jitpack makes accessible any Github project.
    jcenter()
    maven { url "https://kotlin.bintray.com/kotlinx" }
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Add an Orchid Bundle. OrchidAll comes with all official themes included.
    // You must include a theme separately when using the OrchidBlog bundle.
    // Any additional plugins may be added as dependencies here as well.
    orchidRuntime 'io.github.javaeden.orchid:OrchidAll:{version}'
}

orchid {
    // Theme is required
    theme   = "{theme}"
    
    // The following properties are optional
    version = "${project.version}"
    baseUrl = "{baseUrl}"                         // a baseUrl prepended to all generated links. Defaults to '/'
    srcDir  = "path/to/new/source/directory"      // defaults to 'src/orchid/resources'
    destDir = "path/to/new/destination/directory" // defaults to 'build/docs/orchid'
    runTask = "build"                             // specify a task to run with 'gradle orchidRun'
}
```

You can now run Orchid in the following ways:

1) `./gradlew orchidRun` - Runs an Orchid task. The `runTask` should be specified in `build.gradle` or passed as a 
    Gradle project property (`-PorchidRunTask=build`). The task `help` will show a list of all tasks that can be 
    run given the plugins currently installed.
2) `./gradlew orchidBuild` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in 
    `build/docs/orchid` unless the output directory has been changed. You can then view the site by starting any HTTP 
    file server in the root of the output directory, or deploy this folder directly to your webserver.
3) `./gradlew orchidWatch` - Runs the Orchid build task a single time, then begins watching the source directory for 
    changes. Anytime a file is changes, the build will run again, and the resulting Orchid site will be in 
    `build/docs/orchid` unless the output directory has been changed.
4) `./gradlew orchidServe` - Sets up a development server and watches files for changes. The site can be viewed at 
    `localhost:8080` (or the closest available port).
4) `./gradlew orchidDeploy` - Runs the orchid build, then deploys it using Orchid's [deployment pipeline](https://orchid.netlify.com/wiki/user-manual/publication/deployment-pipelines)
    You can create and run your own deployment scripts, create a release on Github from changelogs, or publish the site 
    directly to Github Pages or Netlify.
    
_On windows, all the above commands need to be run with `gradlew` instead of `./gradlew`._

The Orchid Gradle plugin adds a new configuration and content root to your project, in the `src/orchid` directory 
(you may have to create this folder yourself). All your site content sits in `src/orchid/resources`, and any 
additional classes you'd like to include as a private plugin can be placed in `src/orchid/java`.

### Configuring a Maven project

To use Orchid from a Maven project, setup your project's pom.xml file like so:

```xml
<project>
    ...
    
    <properties>
        <orchid.version>{version}</orchid.version>
    </properties>

    <build>
        <plugins>
            <!-- Add the official Orchid Gradle plugin so you can use Orchid with the custom DSL -->
            <plugin>
                <groupId>io.github.javaeden.orchid</groupId>
                <artifactId>orchid-maven-plugin</artifactId>
                <version>${orchid.version}</version>

                <!-- Add an Orchid Bundle. OrchidAll comes with all official themes included.
                     You must include a theme separately when using the OrchidBlog bundle.
                     Any additional plugins may be added as dependencies here as well. -->
                <dependencies>
                    <dependency>
                        <groupId>io.github.javaeden.orchid</groupId>
                        <artifactId>OrchidAll</artifactId>
                        <version>${orchid.version}</version>
                    </dependency>
                </dependencies>

                <configuration>
                    <!-- Theme is required -->
                    <theme>${theme}</theme>
                    
                    <!-- The following properties are optional -->
                    <version>${project.version}</version>
                    <baseUrl>${baseUrl}</baseUrl>                        <!-- a baseUrl prepended to all generated links. Defaults to '/' -->
                    <srcDir>path/to/new/source/directory</srcDir>        <!-- defaults to 'src/orchid/resources' -->
                    <destDir>path/to/new/destination/directory</destDir> <!-- defaults to 'target/docs/orchid' -->
                    <runTask>build</runTask>                             <!-- specify a task to run with 'mvn orchid:run' -->
                </configuration>
            </plugin>
        </plugins>
    </build>

    <!-- Orchid uses dependencies from both Jcenter and Jitpack, so both must be included. jcenter also includes 
         everything available from MavenCentral, while Jitpack makes accessible any Github project. -->
    <pluginRepositories>
        <pluginRepository>
            <id>jcenter</id>
            <name>bintray-plugins</name>
            <url>http://jcenter.bintray.com</url>
        </pluginRepository>
        <pluginRepository>
            <id>kotlinx</id>
            <url>https://kotlin.bintray.com/kotlinx</url>
        </pluginRepository>
        <pluginRepository>
            <id>jitpack</id>
            <url>https://jitpack.io</url>
        </pluginRepository>
    </pluginRepositories>
</project>
```

You can now run Orchid in the following ways:

1) `./mvn orchid:run` - Runs an Orchid task. The `runTask` property should be specified in `pom.xml` or passed as a 
    Maven system property (`-Dorchid.runTask=build`). The task `help` will show a list of all tasks that can be 
    run given the plugins currently installed.
2) `./mvn orchid:build` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in 
    `target/docs/orchid` unless the output directory has been changed. You can then view the site by starting any HTTP 
    file server in the root of the output directory, or deploy this folder directly to your webserver.
3) `./mvn orchid:watch` - Runs the Orchid build task a single time, then begins watching the source directory for 
    changes. Anytime a file is changes, the build will run again, and the resulting Orchid site will be in 
    `build/docs/orchid` unless the output directory has been changed.
4) `./mvn orchid:serve` - Sets up a development server and watches files for changes. The site can be viewed at 
    `localhost:8080` (or the closest available port).
4) `./mvn orchid:deploy` - Runs the Orchid build, then deploys it using Orchid's [deployment pipeline](https://orchid.netlify.com/wiki/user-manual/publication/deployment-pipelines)
    You can create and run your own deployment scripts, create a release on Github from changelogs, or publish the site 
    directly to Github Pages or Netlify.
    
### Using Scriptlets

If you're using Orchid to build a standalone site (not integrated as the docs for another project in the same repo), a 
full Gradle or Maven setup may be a bit overkill. Instead, you may use a tool like 
[kscript](https://github.com/holgerbrandl/kscript) to bootstrap and run Orchid yourself with a more minimalistic project 
structure. The basic API below is specifically created for kscript, but can be easily adapted for other JVM scripting
tools, or used like a library and started from another application.

```kotlin
@file:MavenRepository("kotlinx", "https://kotlin.bintray.com/kotlinx")
@file:MavenRepository("jitpack", "https://jitpack.io")

@file:DependsOn("io.github.javaeden.orchid:OrchidAll:{version}")

import com.eden.orchid.Orchid
import com.eden.orchid.StandardModule

val flags = HashMap<String, Any?>()

// Theme is required
flags["theme"] = "{theme}"

// The following properties are optional
flags["version"] = "{project.version}"
flags["baseUrl"] = "{baseUrl}"                         // a baseUrl prepended to all generated links. Defaults to '/'
flags["srcDir"]  = "path/to/new/source/directory"      // defaults to './src'
flags["destDir"] = "path/to/new/destination/directory" // defaults to './site'
flags["runTask"] = "build"                             // specify a default task to run when not supplied on the command line

val modules = listOf(StandardModule.builder()
        .args(args) // pass in the array of command-line args and let Orchid parse them out
        .flags(flags) // pass a map with any additional args
        .build()
)
Orchid.getInstance().start(modules)
```

You can now start Orchid directly with its CLI, using the following commands:

1) `kscript ./path/to/scriptlet.kts <task> [--<flag> <flag value>]` - Runs an Orchid task by name. Additional parameters
    may be specified after the task name like `--theme Editorial`, which take precedence over the default values 
    specified in the scriptlet. The default tasks are:
    1) `build` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in 
        `build/docs/orchid` unless the output directory has been changed. You can then view the site by starting any 
        HTTP file server in the root of the output directory, or deploy this folder directly to your webserver.
    2) `watch` - Runs the Orchid build task a single time, then begins watching the source directory for changes. 
        Anytime a file is changes, the build will run again, and the resulting Orchid site will be in 
        `build/docs/orchid` unless the output directory has been changed.
    3) `serve` - Sets up a development server and watches files for changes. The site can be viewed at `localhost:8080` 
        (or the closest available port).
    4) `deploy` - Runs the Orchid build, then deploys it using Orchid's [deployment pipeline](https://orchid.netlify.com/wiki/user-manual/publication/deployment-pipelines)
        You can create and run your own deployment scripts, create a release on Github from changelogs, or publish the
        site directly to Github Pages or Netlify.
2) `kscript ./path/to/scriptlet.kts help` - Print out basic usage and all available tasks and command-line options. 

## Example Orchid Sites

* [Official Orchid documentation](https://orchid.netlify.com)
* [Clog documentation](https://javaeden.github.io/Clog/)
* [Krow documentation](https://javaeden.github.io/Krow/)
* [caseyjbrooks.com](https://www.caseyjbrooks.com/)
* [Strikt.io](https://strikt.io/)
* [PebbleTemplates.io](https://pebbletemplates.io/)

## Development Progress

As of v0.7.35, Orchid is now stable and ready for general use, but is still under constant development. Features and 
APIs are mostly stable and are not expected to change significantly, but bugs will be fixed and new features added 
rapidly. All plugins, themes, bundles, and Gradle plugins should use the same version, the most recent is shown here.
 
![Latest Version](https://api.bintray.com/packages/javaeden/Orchid/OrchidCore/images/download.svg)

The following lists all official Orchid packages:

- #### Core Packages
  - [Orchid Core                 ](https://bintray.com/javaeden/Orchid/OrchidCore/_latestVersion)
  - [Orchid Gradle Plugin        ](https://plugins.gradle.org/plugin/com.eden.orchidPlugin)
  - [Orchid Maven Plugin        ](https://bintray.com/javaeden/Orchid/orchid-maven-plugin/_latestVersion)
- #### Themes
  - [OrchidBsDoc          ](https://bintray.com/javaeden/Orchid/OrchidBsDoc/_latestVersion)
  - [OrchidEditorial      ](https://bintray.com/javaeden/Orchid/OrchidEditorial/_latestVersion)
  - [OrchidFutureImperfect](https://bintray.com/javaeden/Orchid/OrchidFutureImperfect/_latestVersion)
- #### Plugins
  - [OrchidChangelog    ](https://bintray.com/javaeden/Orchid/OrchidChangelog/_latestVersion)
  - [OrchidForms        ](https://bintray.com/javaeden/Orchid/OrchidForms/_latestVersion)
  - [OrchidJavadoc      ](https://bintray.com/javaeden/Orchid/OrchidJavadoc/_latestVersion)
  - [OrchidKotlindoc    ](https://bintray.com/javaeden/Orchid/OrchidKotlindoc/_latestVersion)
  - [OrchidKSS          ](https://bintray.com/javaeden/Orchid/OrchidKSS/_latestVersion)
  - [OrchidNetlifyCMS   ](https://bintray.com/javaeden/Orchid/OrchidNetlifyCMS/_latestVersion)
  - [OrchidPages        ](https://bintray.com/javaeden/Orchid/OrchidPages/_latestVersion)
  - [OrchidPluginDocs   ](https://bintray.com/javaeden/Orchid/OrchidPluginDocs/_latestVersion)
  - [OrchidPosts        ](https://bintray.com/javaeden/Orchid/OrchidPosts/_latestVersion)
  - [OrchidPresentations](https://bintray.com/javaeden/Orchid/OrchidPresentations/_latestVersion)
  - [OrchidSearch       ](https://bintray.com/javaeden/Orchid/OrchidSearch/_latestVersion)
  - [OrchidSwagger      ](https://bintray.com/javaeden/Orchid/OrchidSwagger/_latestVersion)
  - [OrchidSwiftdoc     ](https://bintray.com/javaeden/Orchid/OrchidSwiftdoc/_latestVersion)
  - [OrchidTaxonomies   ](https://bintray.com/javaeden/Orchid/OrchidTaxonomies/_latestVersion)
  - [OrchidWiki         ](https://bintray.com/javaeden/Orchid/OrchidWiki/_latestVersion)
- #### Language Extensions
  - [OrchidAsciidoc         ](https://bintray.com/javaeden/Orchid/OrchidAsciidoc/_latestVersion)
  - [OrchidBible            ](https://bintray.com/javaeden/Orchid/OrchidBible/_latestVersion)
  - [OrchidDiagrams         ](https://bintray.com/javaeden/Orchid/OrchidDiagrams/_latestVersion)
  - [OrchidSyntaxHighlighter](https://bintray.com/javaeden/Orchid/OrchidSyntaxHighlighter/_latestVersion)
  - [OrchidWritersBlocks    ](https://bintray.com/javaeden/Orchid/OrchidWritersBlocks/_latestVersion)
- #### Bundles
  - [OrchidAll         ](https://bintray.com/javaeden/Orchid/OrchidAll/_latestVersion)
  - [OrchidBlog        ](https://bintray.com/javaeden/Orchid/OrchidBlog/_latestVersion)
  - [OrchidLanguagePack](https://bintray.com/javaeden/Orchid/OrchidLanguagePack/_latestVersion)

## Contributors

This project exists thanks to all the people who contribute. 
<a href="https://github.com/JavaEden/Orchid/graphs/contributors"><img src="https://opencollective.com/orchidssg/contributors.svg?width=890&button=false" /></a>

## Backers

Thank you to all our backers! 🙏 [[Become a backer](https://opencollective.com/orchidssg#backer)]

<a href="https://opencollective.com/orchidssg#backers" target="_blank"><img src="https://opencollective.com/orchidssg/backers.svg?width=890"></a>


## Sponsors

Support this project by becoming a sponsor. Your logo will show up here with a link to your website. [[Become a sponsor](https://opencollective.com/orchidssg#sponsor)]

<a href="https://opencollective.com/orchidssg/sponsor/0/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/0/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/1/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/1/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/2/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/2/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/3/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/3/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/4/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/4/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/5/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/5/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/6/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/6/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/7/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/7/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/8/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/8/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/9/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/9/avatar.svg"></a>

## License

Orchid is open-source software licensed under the GNU Lesser General Public License (LGPL). Please see
[License.md](https://github.com/JavaEden/Orchid/blob/dev/License.md)

## Contributing

Please see [CONTRIBUTING.md](https://github.com/JavaEden/Orchid/blob/dev/.github/CONTRIBUTING.md)

## Contact

Orchid is being actively developed by Casey Brooks. Please open an issue here with questions or bug/feature requests, or
you can reach me directly at cjbrooks12@gmail.com.
