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

The new system is currently experimental and is an opt-in feature for 0.18.x Orchid versions. It can be enabled now with 
the `--experimentalSourceDoc` CLI flag. The legacy behavior is scheduled for removal in version 0.19.0.
{% endalert %}

## Demo

- Try the [example app](https://github.com/orchidhq/OrchidTutorials/tree/master/swift-site)

## Usage

{% alert 'info' :: compileAs('md') %}
The article {{ anchor('How to Document a Kotlin Project') }} is the best way to get started using Orchid for code 
documentation, check it out for a beginning-to-end guide to using Orchid.

While this article is specific to the Kotlin language support in Orchid, working with Swift in Orchid is very similar, 
with the specifics outlined below.
{% endalert %}

{% alert 'danger' :: compileAs('md') %}
**The behavior of the OrchidSwiftdoc is changing**. Please see the {{ anchor('0.18.0 Migration Guide') }} for more details.

The docs below are for the **legacy** Swiftdoc plugin. For docs on using the **new SourceDoc system**, visit the 
{{ anchor('OrchidSourceDoc') }} plugin homepage.
{% endalert %}

### Basic Usage

Documenting Swift code within Orchid depends on the [SourceKitten](https://github.com/jpsim/SourceKitten) command line
tool, which itself requires the full XCode environment to be installed on your system (not just the xcode command-line 
tools). XCode can be installed from the AppStore, and SourceKitten can be installed though Homebrew:

```bash
brew install sourcekitten
```

Once the Swiftdoc plugin is added to your build and the dependencies are installed to your local Mac, you need to tell 
Orchid where it can find your Swift code. This is set in your `config.yml` as a list of file paths to the root package 
for your code. 

A typical use-case is to have Orchid be used from Gradle to document an external XCode project. For example, 
using `docs` and `app` subprojects with the following standard Gradle/Maven project structure:

```text
. / (repo root)
├── app <-- this is the directory you need to reference
|   └── Main.swift
└── docs <-- Gradle project root
    └── src/orchid/resources/ <-- these are your Orchid resources
        ├── homepage.md
        └── config.yml
```

Your `config.yml` can specify a relative path from your Orchid resources to your Swift code source root:

```yaml
swiftdoc:
  sourceDirs:
    - './../../../../app/'
```

Setting multiple `sourceDirs` will include them all in the generated documentation.

### Menu Items

OrchidSwiftdoc ships with a menu item that is useful for navigating all elements in a Swift application. The 
`swiftdocPages` menu item links to all generated top-level pages, and can be filitered to just classes, packages, enums, 
etc. 

This is best added to the Swiftdoc page Archetypes in `config.yml`:

```yaml
swiftdoc:
  pages:  # <-- applied to Swiftdoc class and source pages
    menu:
      - { type: "swiftdocPages", docType: "class"    }
      - { type: "swiftdocPages", docType: "struct"   }
      - { type: "swiftdocPages", docType: "enum"     }
      - { type: "swiftdocPages", docType: "protocol" }
      - { type: "swiftdocPages", docType: "global"   }
```
