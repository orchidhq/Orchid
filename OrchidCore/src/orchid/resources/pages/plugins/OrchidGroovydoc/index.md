---
from: docs.plugin_index
description: Let Orchid generate documentation for Groovy or Java sources.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1550345984/plugins/groovydoc.jpg
    alt: Groovydoc
    caption: Photo by Vasilios Muselimis on Unsplash
tags:
    - docs
---

## About

The OrchidGroovydoc plugin integrates with the 
[Groovydoc](http://docs.groovy-lang.org/latest/html/documentation/#_groovydoc_the_groovy_java_documentation_generator) 
tool to embed class and package info from Groovy and Java source code directly in your Orchid site. Comment text is 
compiled as Markdown, and is also fully-searchable with the {{anchor('OrchidSearch plugin', 'Orchid Search') }}.

## Demo

- Try the [example app](https://github.com/JavaEden/OrchidTutorials/tree/master/groovy-site)
- Run [GroovydocGeneratorTest](https://github.com/JavaEden/Orchid/blob/dev/plugins/OrchidGroovydoc/src/test/kotlin/com/eden/orchid/groovydoc/NewGroovydocGeneratorTest.kt) for demo

## Usage

{% alert 'info' :: compileAs('md') %}
The article {{ anchor('How to Document a Kotlin Project') }} is the best way to get started using Orchid for code 
documentation, check it out for a beginning-to-end guide to using Orchid. 

While this article is specific to the Kotlin language support in Orchid, working with Groovy in Orchid is almost 
identical, with the specifics outlined below.
{% endalert %}

### Basic Usage

Once the Groovydoc plugin is added to your build, you need to tell Orchid where it can find your Groovy/Java code. This 
is set in your `config.yml` as a list of file paths to the root package for your code. 

A typical use-case is to have Orchid be in a separate Gradle subproject than the code it's documenting. For example, 
using `docs` and `app` subprojects with the following standard Gradle/Maven project structure:

```text
. / (repo root)
├── app
|   └── src/main/groovy/ <-- this is the directory you need to reference
|       └── com/example/
|           └── Main.groovy
└── docs
    └── src/orchid/resources/ <-- these are your Orchid resources
        ├── homepage.md
        └── config.yml
```

Your `config.yml` can specify a relative path from your Orchid resources to your Groovy code source root:

```yaml
groovydoc:
  sourceDirs:
    - './../../../../app/src/main/groovy'
```

Setting multiple `sourceDirs` will include them all in the generated documentation, such as including both `groovy` and 
`java` directories for a single module.

### Menu Items

OrchidGroovydoc ships with several menu item types that are useful for creating docs with a similar feel to standard 
Groovydoc sites. The `groovydocClasses` and `groovydocPackages` simply link to all generated class and package pages, 
like the sidebar frames on typical Groovydoc pages. 

`groovydocClassLinks` creates links to each individual field, constructor, and method documented in a class page, 
similar to the "summary" section of typical Groovydoc pages. It can only be added to Groovydoc class pages.

All three of these menu items are best added to the Groovydoc page Archetypes in `config.yml`:

```yaml
groovydoc:
  classPages: # <-- applied only to Groovydoc class pages
    menu:
      - type: "groovydocClassLinks"
  pages:  # <-- applied to Groovydoc class and package pages
    menu:
      - type: "groovydocClasses"
      - type: "groovydocPackages"
```
