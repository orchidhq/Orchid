---
description: Embed Kotlin and Java documentation in your Orchid site using Dokka.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_crop,g_north,h_1402,w_2666/c_scale,e_blur:150,w_300/v1550346159/plugins/kotlindoc.jpg
    alt: Javadoc
    caption: Photo by Federico Bottos on Unsplash
tags:
    - docs
sourceLanguage: 'kotlin'
---

## About

The OrchidKotlindoc plugin integrates with the [Dokka](https://github.com/Kotlin/dokka) CLI to embed class and package 
info and [KDoc](https://kotlinlang.org/docs/reference/kotlin-doc.html#kdoc-syntax) comments from Kotlin and Java source
code directly in your Orchid site.

{% alert 'info' %}
{% snippet 'sourcedocs_about' %}
{% endalert %}

{% alert 'danger' %}
{% snippet 'sourcedocs_legacy_warning' %}
{% endalert %}

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/kotlin-site)
- Run [KotlindocGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidKotlindoc/src/test/kotlin/com/eden/orchid/kotlindoc/NewKotlindocGeneratorTest.kt) for demo

## Single-Module Usage

{% snippet 'sourcedocs_single_module_kotlin' raw=true %}

## Multi-Module Usage

{% snippet 'sourcedocs_multi_module_kotlin' raw=true %}
