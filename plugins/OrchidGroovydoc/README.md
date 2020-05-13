---
description: Let Orchid generate documentation for Groovy or Groovy sources.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1550345984/plugins/groovydoc.jpg
    alt: Groovydoc
    caption: Photo by Vasilios Muselimis on Unsplash
tags:
    - docs
sourceLanguage: 'groovy'
---

## About

The OrchidGroovydoc plugin integrates with the 
[Groovydoc](https://docs.groovy-lang.org/latest/html/documentation/#_groovydoc_the_groovy_groovy_documentation_generator) 
tool to embed class and package info from Groovy and Java source code directly in your Orchid site.

{% alert 'info' %}
{% snippet 'sourcedocs_about' %}
{% endalert %}

{% alert 'danger' %}
{% snippet 'sourcedocs_legacy_warning' %}
{% endalert %}

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/groovy-site)
- Run [GroovydocGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidGroovydoc/src/test/kotlin/com/eden/orchid/groovydoc/NewGroovydocGeneratorTest.kt) for demo

## Single-Module Usage

{% snippet 'sourcedocs_single_module_groovy' raw=true %}

## Multi-Module Usage

{% snippet 'sourcedocs_multi_module_groovy' raw=true %}
