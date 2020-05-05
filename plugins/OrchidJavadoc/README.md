---
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
compiled as Markdown, and is also fully-searchable with the {{anchor('OrchidSearch plugin', 'OrchidSearch') }}.

{% alert 'danger' :: compileAs('md') %}
**The behavior of the OrchidJavadoc is changing**. Please see the {{ anchor('0.18.0 Migration Guide') }} for more details.

This plugin is being deprecated in favor of a new, more unified, and more modular code-documentation plugin, 
{{ anchor('OrchidSourceDoc') }}. All configuration will be defined by that plugin, and this plugin will simply provide
Java language support for that plugin.

The new system is now the default, but the old system can still be used for a short while under a feature flag until the
transition is complete. Legacy Javadocs can be enabled with the `--legacySourceDoc` CLI flag. The legacy behavior is
scheduled for removal in version 0.22.0.
{% endalert %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/java-site)
- Run [JavadocGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidJavadoc/src/test/kotlin/com/eden/orchid/javadoc/NewJavadocGeneratorTest.kt) for demo

## Usage

{% alert 'info' :: compileAs('md') %}
The article {{ anchor('How to Document a Kotlin Project') }} is the best way to get started using Orchid for code 
documentation, check it out for a beginning-to-end guide to using Orchid.

While this article is specific to the Kotlin language support in Orchid, working with Java in Orchid is almost 
identical, with the specifics outlined in the example app.
{% endalert %}
