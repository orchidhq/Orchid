
# Orchid

[![Build Status](https://travis-ci.org/JavaEden/Orchid.svg?branch=master)](https://travis-ci.org/JavaEden/Orchid) (Linux and Mac)
[![Build status](https://ci.appveyor.com/api/projects/status/0358qdkmfhbqedo1/branch/master?svg=true)](https://ci.appveyor.com/project/cjbrooks12/orchid/branch/master) (Windows)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](http://www.gnu.org/licenses/lgpl-3.0)

> A beautiful and truly unique documentation engine and static site generator.

![Example Orchid site](http://i.imgur.com/Fs3YFyY.png)

Orchid is a general-purpose static site generator with a focus on extensibility, and aimed at developers looking to 
improve their technical documentation. Orchid was born out of a desire for better-looking Javadocs and frustration with 
how difficult is it to manage large Jekyll sites and keep it up-to-date with your code. 

Orchid supports a variety of plugins, including a wiki, static pages, blogs, and much more. It aims to have high 
compatibility with many of the existing static site generators, such as Jekyll, Gitbook, and Hugo, so that migration to 
Orchid is painless. And if you can't find a plugin to do what you need, Orchid provides an intuitive way to add your own 
private plugins and a rich API so you can make your site as beautiful and unique as an Orchid.

[View the full documentation](http://javaeden.github.io/orchid/latest/OrchidCore) or see the quick-start below.

## Table of Contents

1. [Installation](#installation)
1. [Configuration](#configuration)
1. [Development Progress](#development-progress)
    1. [Core Packages](#core-packages)
    1. [Themes](#themes)
    1. [Plugins](#plugins)
1. [License](#license)
1. [Contributing](#contributing)
1. [Contact](#contact)

## Installation

Orchid integrates with any new or existing Gradle project. To get started, pick a Bundle (OrchidAll, OrchidBlog, or 
OrchidProduct) or manually choose your desired Orchid plugins. You may pick a bundle to start with and add any number of 
plugins afterward, both official and unofficial. Then, setup your project's `build.gradle` file like so:

```groovy

plugins {
    // Add the official Orchid Gradle plugin so you can use Orchid with the custom DSL   
    id "com.eden.orchidPlugin" version "{version}"
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
    orchidDocsRuntime 'io.github.javaeden.orchid:OrchidAll:{version}'
}

orchid {
    // Version, theme, and baseUrl are required
    version = "${project.version}"
    theme   = "{theme class}"
    
    // Add this is you are developing a theme or plugin to include its own `main module` sources in the Orchid build
    includeMainConfiguration = true
    
    // The following properties are optional
    
    baseUrl = "{baseUrl}"                         // a baseUrl appended to all generated links. Defaults to '/'
    srcDir  = "path/to/new/source/directory"      // defaults to 'src/orchidDocs/resources'
    destDir = "path/to/new/destination/directory" // defaults to 'build/docs/javadoc'
    runTask = "build"                             // specify a task to run with 'gradle orchidRun'
}

```

You can now run Orchid in the following ways:

1) `./gradlew orchidRun` - Runs an Orchid task. The `runTask` should be specified in `build.gradle` or passed as a Gradle
    project property (`-PrunTask=build`). The task `listTasks` will show a list of all tasks that can be run given the 
    plugins currently installed. Similarly, `listOptions` will list all options that can be set through Gradle. 
2) `./gradlew orchidBuild` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in 
    `build/docs/javadoc` unless the output directory has been changed.
3) `./gradlew orchidServe` - Sets up a development server and watches files for changes. Must have the `OrchidServer` 
    plugin installed for this task to work, which is included in all the above bundles.
4) If you are developing a Java application, Orchid replaces the standard Javadoc task with its own `build` task. In 
    addition to running the standard Orchid build, when Orchid is run from Javadoc it will be able to create pages 
    for all your project's classes and packages, just like you'd expect from a normal Javadoc site, but embedded within
    your chosen Orchid theme. You must have the `OrchidJavadoc` plugin installed for this to work properly.
    
_On windows, all the above commands need to be run with `gradlew` instead of `./gradlew`._

The Orchid Gradle plugin adds a new configuration and content root to your project, in the `src/orchidDocs` directory 
(you may have to create this folder yourself). All your site content sits in `src/orchidDocs/resources`, and any 
additional classes you'd like to include as a private plugin can be placed in `src/orchidDocs/java`. 

## Configuration

You should create a `config.yml` file in your resources directory to customize your theme or configure your build. These
values can then be used in all templates and when preprocessing your content files under the `options` variable. 
Alternatively, you may create a file in `data/` with your data which will be accessible at `options.[filename]`.

## Development Progress

Orchid is still in the early stages of development. While much of the public API is solid and not likely to change, I 
make no guarantees that any particular API will not change or be removed. In addition, themes are likely to change 
significantly, so any custom templates may not behave as expected after an Orchid update.
 
The following table lists all Orchid packages currently in development:

#### Core Packages

| Name                 | Version |
| -------------------- | ------- |
| Orchid Core          | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidCore/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidCore/_latestVersion) |
| Orchid Gradle Plugin | [ ![Download](https://img.shields.io/badge/Gradle%20Plugin-v0.2.0-blue.svg) ](https://plugins.gradle.org/plugin/com.eden.orchidPlugin)  |

#### Themes

| Name              | Version |
| ----------------- | ------- |
| OrchidBsDoc       | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidBsDoc/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidBsDoc/_latestVersion) |
| OrchidEditorial   | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidEditorial/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidEditorial/_latestVersion) |
| OrchidMaterialize | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidMaterialize/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidMaterialize/_latestVersion) |

#### Plugins

| Name         | Version |
| ------------ | ------- |
| Changelog    | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidChangelog/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidChangelog/_latestVersion) |
| Javadoc      | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidJavadoc/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidJavadoc/_latestVersion) |
| LanguagePack | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidLanguagePack/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidLanguagePack/_latestVersion) |
| Pages        | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidPages/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidPages/_latestVersion) |
| Posts        | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidPosts/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidPosts/_latestVersion) |
| Server       | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidServer/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidServer/_latestVersion) |
| Wiki         | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidWiki/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidWiki/_latestVersion) |

## License

Orchid is open-source software licensed under the GNU Lesser General Public License (LGPL). See License.md for more 
information.

## Contributing

This repository is comprised of many individual projects, which are all listed above. You can build and run any project
with Gradle from the project root, such as `gradle :OrchidCore:assemble` or `gradle :plugins:OrchidServer:assemble`, or 
you may navigate to a particular project's subdirectory to run the Gradle commands directly. When contributing code, 
please indent using 4 spaces and keep braces on the same lines.

## Contact

Orchid is being actively developed by Casey Brooks. Please open an issue here with questions or bug/feature requests, or
you can reach me directly at cjbrooks12@gmail.com.