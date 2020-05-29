---
description: Where Orchid began. Create beautiful Javadocs for your project within your Orchid site.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974952/plugins/javadoc.jpg
    alt: Javadoc
    caption: Photo by Brooke Lark on Unsplash
tags:
    - docs
sourceLanguage: 'java'
---

## About

The OrchidJavadoc plugin integrates with the 
[Javadoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html) tool to embed class and package 
info from Java source code directly in your Orchid site.

{% alert 'info' %}
{% snippet 'sourcedocs_about' %}
{% endalert %}

{% alert 'danger' %}
{% snippet 'sourcedocs_legacy_warning' %}
{% endalert %}

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/java-site)
- Run [JavadocGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidJavadoc/src/test/kotlin/com/eden/orchid/javadoc/NewJavadocGeneratorTest.kt) for demo

## Single-Module Usage

{% snippet 'sourcedocs_single_module_java' raw=true %}

## Multi-Module Usage

{% snippet 'sourcedocs_multi_module_java' raw=true %}
