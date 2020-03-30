---
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
[Groovydoc](https://docs.groovy-lang.org/latest/html/documentation/#_groovydoc_the_groovy_java_documentation_generator) 
tool to embed class and package info from Groovy and Java source code directly in your Orchid site. Comment text is 
compiled as Markdown, and is also fully-searchable with the {{anchor('OrchidSearch plugin', 'OrchidSearch') }}.

{% alert 'danger' :: compileAs('md') %}
**The behavior of the OrchidGroovydoc is changing**. Please see the {{ anchor('0.18.0 Migration Guide') }} for more details.

This plugin is being deprecated in favor of a new, more unified, and more modular code-documentation plugin, 
{{ anchor('OrchidSourceDoc') }}. All configuration will be defined by that plugin, and this plugin will simply provide
Groovy language support for that plugin.

The new system is currently experimental and is an opt-in feature for 0.18.x Orchid versions. It can be enabled now with 
the `--experimentalSourceDoc` CLI flag. The legacy behavior is scheduled for removal in version 0.19.0.
{% endalert %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/groovy-site)
- Run [GroovydocGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidGroovydoc/src/test/kotlin/com/eden/orchid/groovydoc/NewGroovydocGeneratorTest.kt) for demo

## Usage

{% alert 'info' :: compileAs('md') %}
The article {{ anchor('How to Document a Kotlin Project') }} is the best way to get started using Orchid for code 
documentation, check it out for a beginning-to-end guide to using Orchid. 

While this article is specific to the Kotlin language support in Orchid, working with Groovy in Orchid is almost 
identical, with the specifics outlined in the example app.
{% endalert %}
