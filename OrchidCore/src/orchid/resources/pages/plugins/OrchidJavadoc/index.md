---
from: docs.plugin_index
description: Where Orchid began. Create beautiful Javadocs for your project within your Orchid site.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974952/plugins/javadoc.jpg
    alt: Javadoc
    caption: Photo by Brooke Lark on Unsplash
tags:
    - docs
---

## About

The OrchidJavadoc plugin integrates with the 
[Javadoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html) 
tool to embed class and package info from Java and Java source code directly in your Orchid site. Comment text is 
compiled as Markdown, and is also fully-searchable with the {{anchor('OrchidSearch plugin', 'Orchid Search') }}.

## Demo

- Try the [example app](https://github.com/JavaEden/OrchidTutorials/tree/master/java-site)
- Run [JavadocGeneratorTest](https://github.com/JavaEden/Orchid/blob/dev/plugins/OrchidJavadoc/src/test/kotlin/com/eden/orchid/javadoc/JavadocGeneratorTest.kt) for demo

## Usage

{% alert 'info' :: compileAs('md') %}
The article {{ anchor('How to Document a Kotlin Project') }} is the best way to get started using Orchid for code 
documentation, check it out for a beginning-to-end guide to using Orchid.

While this article is specific to the Kotlin language support in Orchid, working with Java in Orchid is almost 
identical, with the specifics outlined below.
{% endalert %}

### Basic Usage

Once the Javadoc plugin is added to your build, you need to tell Orchid where it can find your Java code. This is set in 
your `config.yml` as a list of file paths to the root package for your code. 

A typical use-case is to have Orchid be in a separate Gradle subproject than the code it's documenting. For example, 
using `docs` and `app` subprojects with the following standard Gradle/Maven project structure:

```text
. / (repo root)
├── app
|   └── src/main/java/ <-- this is the directory you need to reference
|       └── com/example/
|           └── Main.java
└── docs
    └── src/orchid/resources/ <-- these are your Orchid resources
        ├── homepage.md
        └── config.yml
```

Your `config.yml` can specify a relative path from your Orchid resources to your Java code source root:

```yaml
javadoc:
  sourceDirs:
    - './../../../../app/src/main/java'
```

Setting multiple `sourceDirs` will include them all in the generated documentation.

### Menu Items

OrchidJavadoc ships with several menu item types that are useful for creating docs with a similar feel to standard 
Javadoc sites. The `javadocClasses` and `javadocPackages` simply link to all generated class and package pages, like the 
sidebar frames on typical Javadoc pages. 

`javadocClassLinks` creates links to each individual field, constructor, and method documented in a class page, similar 
to the "summary" section of typical Javadoc pages. It can only be added to Javadoc class pages.

All three of these menu items are best added to the Javadoc page Archetypes in `config.yml`:

```yaml
javadoc:
  classPages: # <-- applied only to Javadoc class pages
    menu:
      - type: "javadocClassLinks"
  pages:  # <-- applied to Javadoc class and package pages
    menu:
      - type: "javadocClasses"
      - type: "javadocPackages"
```
