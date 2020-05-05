---
description: Create beautiful documentation for your Swift source code within Orchid.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1525466545/plugins/swiftdoc.jpg
    alt: Swiftdoc
    caption: Photo by OOI JIET on Unsplash
tags:
    - docs
---

## About

The OrchidSwiftdoc plugin integrates with the [SourceKitten](https://github.com/jpsim/SourceKitten) tool to embed class 
and package info from Swift source code directly in your Orchid site, producing documentation similar to Javadoc. 
Comment text is compiled as Markdown, and is also fully-searchable with the 
{{anchor('OrchidSearch plugin', 'OrchidSearch') }}.

{% alert 'danger' :: compileAs('md') %}
**The behavior of the OrchidSwiftdoc is changing**. Please see the {{ anchor('0.18.0 Migration Guide') }} for more details.

This plugin is being deprecated in favor of a new, more unified, and more modular code-documentation plugin, 
{{ anchor('OrchidSourceDoc') }}. All configuration will be defined by that plugin, and this plugin will simply provide
Swift language support for that plugin.

The new system is now the default, but the old system can still be used for a short while under a feature flag until the
transition is complete. Legacy Swiftdocs can be enabled with the `--legacySourceDoc` CLI flag. The legacy behavior is
scheduled for removal in version 0.22.0.
{% endalert %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/swift-site)

## Usage

{% alert 'info' :: compileAs('md') %}
The article {{ anchor('How to Document a Kotlin Project') }} is the best way to get started using Orchid for code 
documentation, check it out for a beginning-to-end guide to using Orchid.

While this article is specific to the Kotlin language support in Orchid, working with Swift in Orchid is almost 
identical, with the specifics outlined in the example app.
{% endalert %}
