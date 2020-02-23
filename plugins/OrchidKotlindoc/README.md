---
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
fully-searchable with the {{anchor('OrchidSearch plugin', 'OrchidSearch') }}.

{% alert 'danger' :: compileAs('md') %}
**The behavior of the OrchidKotlindoc is changing**. Please see the {{ anchor('0.18.0 Migration Guide') }} for more details.

This plugin is being deprecated in favor of a new, more unified, and more modular code-documentation plugin, 
{{ anchor('OrchidSourceDoc') }}. All configuration will be defined by that plugin, and this plugin will simply provide
Kotlin language support for that plugin.

The new system is currently experimental and is an opt-in feature for 0.18.x Orchid versions. It can be enabled now with 
the `--experimentalSourceDoc` CLI flag. The legacy behavior is scheduled for removal in version 0.19.0.
{% endalert %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/kotlin-site)
- Run [KotlindocGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidKotlindoc/src/test/kotlin/com/eden/orchid/kotlindoc/NewKotlindocGeneratorTest.kt) for demo

## Usage

{% alert 'info' :: compileAs('md') %}
The article {{ anchor('How to Document a Kotlin Project') }} is the best way to get started using Orchid for code 
documentation, check it out for a beginning-to-end guide to using Orchid.
{% endalert %}
