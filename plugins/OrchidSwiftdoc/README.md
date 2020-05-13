---
description: Create beautiful documentation for your Swift source code within Orchid.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1525466545/plugins/swiftdoc.jpg
    alt: Swiftdoc
    caption: Photo by OOI JIET on Unsplash
tags:
    - docs
sourceLanguage: 'swift'
---

## About

The OrchidSwiftdoc plugin integrates with the [SourceKitten](https://github.com/jpsim/SourceKitten) tool to embed class 
and source file info from Swift source code directly in your Orchid site.

{% alert 'info' %}
{% snippet 'sourcedocs_about' %}
{% endalert %}

{% alert 'danger' %}
{% snippet 'sourcedocs_legacy_warning' %}
{% endalert %}

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/swift-site)
- Run [SwiftdocGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidSwiftdoc/src/test/kotlin/com/eden/orchid/swiftdoc/NewSwiftdocGeneratorTest.kt) for demo

## Single-Module Usage

{% snippet 'sourcedocs_single_module_swift' raw=true %}

## Multi-Module Usage

{% snippet 'sourcedocs_multi_module_swift' raw=true %}
