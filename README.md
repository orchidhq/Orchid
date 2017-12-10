
# Orchid

[![Build Status](https://travis-ci.org/JavaEden/Orchid.svg?branch=master)](https://travis-ci.org/JavaEden/Orchid) (Linux and Mac)
[![Build status](https://ci.appveyor.com/api/projects/status/0358qdkmfhbqedo1/branch/master?svg=true)](https://ci.appveyor.com/project/cjbrooks12/orchid/branch/master) (Windows)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](http://www.gnu.org/licenses/lgpl-3.0)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8bca7e84b6094c03ae1316278cf63ae1)](https://www.codacy.com/app/cjbrooks12/Orchid?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=JavaEden/Orchid&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/JavaEden/Orchid/branch/master/graph/badge.svg)](https://codecov.io/gh/JavaEden/Orchid)

> A beautiful and truly unique documentation engine and static site generator.

![Example Orchid site](http://i.imgur.com/Fs3YFyY.png)

Orchid is a general-purpose static site generator with a focus on extensibility, and aimed at developers looking to 
improve their technical documentation. Orchid was born out of a desire for better-looking Javadocs and frustration with 
how difficult is it to manage large Jekyll sites and keep it up-to-date with your code. 

Orchid supports a variety of plugins, including a wiki, static pages, blogs, and much more. It aims to have high 
compatibility with many of the existing static site generators, such as Jekyll, Gitbook, and Hugo, so that migration to 
Orchid is painless. And if you can't find a plugin to do what you need, Orchid provides an intuitive way to add your own 
private plugins and a rich API so you can make your site as beautiful and unique as an Orchid.

### Features

- Plugin-driven Architecture
- Multi-theme support
- Admin interface to manage your content and gain deep insights into your build
- Link to other Orchid sites
- Powerful and flexible indexing, unique to Orchid
- Fully replaces Jekyll, Gitbook, Javadocs, and more!

[View the full documentation](http://javaeden.github.io/orchid/latest/OrchidCore) or see the quick-start below.

## Table of Contents

1. [Installation](#installation)
1. [Site Configuration](#site-configuration)
1. [Page Configuration](#page-configuration)
1. [Components and menus](#components-and-menus)
1. [Theming](#theming)
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
    theme   = "{theme}"
    
    // The following properties are optional
    
    baseUrl = "{baseUrl}"                         // a baseUrl appended to all generated links. Defaults to '/'
    srcDir  = "path/to/new/source/directory"      // defaults to 'src/orchidDocs/resources'
    destDir = "path/to/new/destination/directory" // defaults to 'build/docs/javadoc'
    runTask = "build"                             // specify a task to run with 'gradle orchidRun'
}

```

You can now run Orchid in the following ways:

1) `./gradlew orchidRun` - Runs an Orchid task. The `runTask` should be specified in `build.gradle` or passed as a Gradle project property (`-PorchidRunTask=build`). The task `listTasks` will show a list of all tasks that can be run given the plugins currently installed.
2) `./gradlew orchidBuild` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in `build/docs/javadoc` unless the output directory has been changed. You can then view the site by starting any HTTP file server in the root of the output directory.
3) `./gradlew orchidWatch` - Runs the Orchid build task a single time, then begins watching the source directory for changes. Anytime a file is changes, the build will run again, and the resulting Orchid site will be in `build/docs/javadoc` unless the output directory has been changed.
4) `./gradlew orchidServe` - Sets up a development server and watches files for changes. The site can be viewed at `localhost:8080` (or the closest available port).
5) If you are developing a Java application, Orchid replaces the standard Javadoc task with its own `build` task. In addition to running the standard Orchid build, when Orchid is run from Javadoc it will be able to create pages for all your project's classes and packages, just like you'd expect from a normal Javadoc site, but embedded within your chosen Orchid theme. You must have the `OrchidJavadoc` plugin installed for this to work properly.
    
_On windows, all the above commands need to be run with `gradlew` instead of `./gradlew`._

The Orchid Gradle plugin adds a new configuration and content root to your project, in the `src/orchidDocs` directory 
(you may have to create this folder yourself). All your site content sits in `src/orchidDocs/resources`, and any 
additional classes you'd like to include as a private plugin can be placed in `src/orchidDocs/java`. 

## Site Configuration

You should create a `config.yml` file in your resources directory to customize your theme or configure your build. These values are then "injected" into various classes throughout the build, including themes, services, generators, pages, components, and menu items, and then are available for use simply as properties of the object they were injected into. A sample `config.yml` looks like:

```yaml
# options for the Theme
Editorial:
  extraCss: ['assets/css/customCss.scss'] # extra CSS or JS can be added to any Theme, Page, or Component
  menu: # the theme defined at menu at `menu`. Each object is injected into the menu item it creates. Pages may also define their own menus or component areas
    - type: 'link'
      title: 'Home'
      url: '/'
    - type: 'postCategories'
      category: 'personal'
      title: 'blog'
    - type: 'page'
      page: 'Contact'
  sidebar: # Similar to menus, the theme defined at sidebare to hold Components. Each object is injected into the object it creates.
    - type: recentPosts
      limit: 3
      category: programming
      templates: # any Component or Page can specify a list of templates, the first matching template will be used
          - 'includes/postPreview_mini'
    - type: recentPosts
      limit: 5
      category: personal
      templates:
          - 'includes/postPreview_list'

# Set the options for the Assets, Pages, and posts generators. 
assets: 
  sourceDirs: # set directories to copy all assets from
    - 'assets/images'

pages:
  layout: single # Generators can specify a default layout for the pages they generate, otherwise it is decided by the Page or falls back to 'index'

# Some plugins can be quite highly configurable, such as OrchidPosts
posts:
  layout: single
  permalink: ':category/:year/:month/:slug'
  disqusShortname: 'shortname'
  categories:
    - 'personal'
    - 'programming'
  authors:
    - name: 'Author'
      avatar: 'assets/images/avatar.jpg'
```

## Page Configuration

While Orchid does not mandate any folder structure (leaving it up to plugins to define), plugins that use files on disk can add options to the page with FrontMatter. FrontMatter is a block of YAML between a pair of three dashes `---` on their own line. When FrontMatter is given (even if nothing is between the pairs of dashes) the FrontMatter block will be removed and the rest of the file preprocessed with Pebble. A Page's typical FrontMatter may look like: 

```markdown
 ---
 layout: single # set the page's layout. Can be a full file name and path, such as 'layouts/single.peb' or just the filename for a file in the resource dir 'templates/layouts' folder
 components: # All Pages have a Component area at 'components', but may define additional areas. The same goes for Menus
  - type: recentPosts
    limit: 3
    category: ':any'
    templates:
        - 'includes/postPreview_large'
 ---

 Page Content Here
```

In addition to YAML, Orchid supports JSON for FrontMatter, by using `;;;` delimiters rather than `---`, or TOML (when OrchidLanguagePage plugin in use) with `+++`, or by adding the desired format extension directly after the opening delimiter:

```markdown
---toml
---
```

and

```markdown
+++
+++
```

are equivalent.

## Components and Menus

Pages and Themes may each, independently, define Menus and Component Areas. They are declared within the Java Class that defines the page or theme, and then rendered from within the Pebble templates. Just because a menu or component area is defined doesn't mean it is actually used: the Theme base class has a menu at `menu` but an individual theme may choose to ignore it and use multiple menus with more semantic names. Likewise, the Page base class has a menu at `menu` and a component area at `components`. 

Themes and menus are both set up with an array of objects, which are then lazily converted into Component and MenuItem objects. This ensures that the indexing process has completed by the time the menus and components are being created. Plugins may define their own components and menu item type, and several common ones are included in OrchidCore.

When Components are created, they are rendered with a template (typically from `templates/components` from top-to-bottom in the order defined in the array data creation, however, it is common for specific Page types or generators to add components themselves, which get added after the FrontMatter or config.yml-defined components. 

When Menus are created, rather than having a one-to-one correspondance of menu item config to menu item in the theme, Menus Items return a list of individual items. These items may be arranged in a recursive tree, but always wrap a Page object and generate a link with an absolute URL. 

## Theming

Orchid supports multiple themes in one build seamlessly. By default, Orchid will use the Theme specified on the command line or through the Gradle config, but themes may be applied to Generators, which will then be used for all pages rendered by that Generator.


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

| Name                  | Version |
| --------------------- | ------- |
| OrchidBsDoc           | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidBsDoc/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidBsDoc/_latestVersion) |
| OrchidEditorial       | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidEditorial/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidEditorial/_latestVersion) |
| OrchidFutureImperfect | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidFutureImperfect/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidFutureImperfect/_latestVersion) |
| OrchidMaterialize     | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidMaterialize/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidMaterialize/_latestVersion) |

#### Plugins

| Name           | Version |
| -------------- | ------- |
| Changelog      | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidChangelog/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidChangelog/_latestVersion) |
| Javadoc        | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidJavadoc/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidJavadoc/_latestVersion) |
| KSS Styleguide | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidKSS/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidKSS/_latestVersion) |
| LanguagePack   | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidLanguagePack/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidLanguagePack/_latestVersion) |
| Pages          | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidPages/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidPages/_latestVersion) |
| Posts          | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidPosts/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidPosts/_latestVersion) |
| Wiki           | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidWiki/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidWiki/_latestVersion) |

## License

Orchid is open-source software licensed under the GNU Lesser General Public License (LGPL). See License.md for more 
information.

## Contributing

This repository is comprised of many individual projects, which are all listed above. You can build and run any project
with Gradle from the project root, such as `gradle :OrchidCore:assemble` or `gradle :plugins:OrchidWiki:assemble`, or 
you may navigate to a particular project's subdirectory to run the Gradle commands directly. When contributing code, 
please indent using 4 spaces and keep braces on the same lines.

To release a new version of Orchid:

- Orchid Core, and all plugins and themes:
    - Start with clean Git index
    - Increment `version` in root `build.gradle`, according to [Semantic Versioning](http://semver.org/) and prefixed with `v`
    - Commit and push version change
    - `gradle clean build deploy -Penv=prod` (requires API keys and credentials for Github and Bintray)
- Orchid Gradle Plugin
    - Start with clean Git index
    - `cd buildSrc` (buildSrc is not recognized as a normal part of the Gradle project, but its is a project in its own local directory)
    - Increment `version` in `buildSrc/build.gradle`, according to [Semantic Versioning](http://semver.org/) and prefixed with `v`
    - `gradle publishPlugins`

## Contact

Orchid is being actively developed by Casey Brooks. Please open an issue here with questions or bug/feature requests, or
you can reach me directly at cjbrooks12@gmail.com.
