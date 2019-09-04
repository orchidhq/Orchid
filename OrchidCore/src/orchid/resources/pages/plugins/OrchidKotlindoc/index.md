---
from: docs.plugin_index
description: Embed Kotlin and Java documentation in your Orchid site using Dokka.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_crop,g_north,h_1402,w_2666/c_scale,e_blur:150,w_300/v1550346159/plugins/kotlindoc.jpg
    alt: Javadoc
    caption: Photo by Federico Bottos on Unsplash
tags:
    - docs
---

## About

The OrchidKotlindoc plugin integrates with the [Dokka](https://github.com/Kotlin/dokka) tool to embed class and package 
info from Kotlin and Java source code directly in your Orchid site. Comment text is compiled as Markdown, and is also 
fully-searchable with the {{anchor('OrchidSearch plugin', 'Orchid Search') }}.

## Demo

- Try the [example app](https://github.com/JavaEden/OrchidTutorials/tree/master/kotlin-site)
- Run [KotlindocGeneratorTest](https://github.com/JavaEden/Orchid/blob/dev/plugins/OrchidKotlindoc/src/test/kotlin/com/eden/orchid/kotlindoc/NewKotlindocGeneratorTest.kt) for demo

## Usage

{% alert 'info' :: compileAs('md') %}
The article {{ anchor('How to Document a Kotlin Project') }} is the best way to get started using Orchid for code 
documentation, check it out for a beginning-to-end guide to using Orchid.
{% endalert %}

### Basic Usage

Once the Kotlindoc plugin is added to your build, you need to tell Orchid where it can find your Kotlin/Java code. This 
is set in your `config.yml` as a list of file paths to the root package for your code. 

A typical use-case is to have Orchid be in a separate Gradle subproject than the code it's documenting. For example, 
using `docs` and `app` subprojects with the following standard Gradle/Maven project structure:

```text
. / (repo root)
├── app
|   └── src/main/kotlin/ <-- this is the directory you need to reference
|       └── com/example/
|           └── main.kt
└── docs
    └── src/orchid/resources/ <-- these are your Orchid resources
        ├── homepage.md
        └── config.yml
```

Your `config.yml` can specify a relative path from your Orchid resources to your Kotlin code source root:

```yaml
kotlindoc:
  sourceDirs:
    - './../../../../app/src/main/kotlin'
```

Setting multiple `sourceDirs` will include them all in the generated documentation, such as including both `kotlin` and 
`java` directories for a single module.

### Classpath Management

Dokka needs the classpath of your source code in order to document everything properly. Specifically, parameters with a 
type not in your sources and not part of the Kotlin stdlib will be displayed as `<ERROR CLASS>` without the full 
classpath provided.

You can set the classpath to be used by the Dokka instance running internally with the `--kotlindocClasspath` CLI flag.
The following snippet can be used to pass the classpath from Gradle to Orchid/Dokka:

```groovy
afterEvaluate {
    orchid {
        // use Gradle APIs to get the classpath to pass-through to Dokka
        args += ["--kotlindocClasspath", project(":app").sourceSets.main.runtimeClasspath.getAsPath()]
    }
}
```

See [issue #222](https://github.com/JavaEden/Orchid/issues/222) for more context on why this is necessary.

### Menu Items

OrchidKotlindoc ships with several menu item types that are useful for creating docs with a similar feel to standard 
Kotlindoc sites. The `kotlindocClasses` and `kotlindocPackages` simply link to all generated class and package pages, 
like the sidebar frames on typical Kotlindoc pages. 

`kotlindocClassLinks` creates links to each individual field, constructor, and method documented in a class page, 
similar to the "summary" section of typical Kotlindoc pages. It can only be added to Kotlindoc class pages.

All three of these menu items are best added to the Kotlindoc page Archetypes in `config.yml`:

```yaml
kotlindoc:
  classPages: # <-- applied only to Kotlindoc class pages
    menu:
      - type: "kotlindocClassLinks"
  pages:  # <-- applied to Kotlindoc class and package pages
    menu:
      - type: "kotlindocClasses"
      - type: "kotlindocPackages"
```
