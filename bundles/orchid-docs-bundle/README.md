---
included:
  - pluginType: themes
    pluginName: OrchidEditorial
  - pluginType: plugins
    pluginName: OrchidPages
  - pluginType: plugins
    pluginName: OrchidWiki
  - pluginType: plugins
    pluginName: OrchidForms
  - pluginType: plugins
    pluginName: OrchidChangelog
  - pluginType: plugins
    pluginName: OrchidSearch
  - pluginType: plugins
    pluginName: OrchidDiagrams
  - pluginType: plugins
    pluginName: OrchidSyntaxHighlighter
---

## About

The Orchid Docs bundle is your one-stop-shop to get started documenting your projects with Orchid! It includes the most
common plugins needed to create landing pages and wikis with rich content, perfect for diagramming and documenting your
code projects. It also comes with the Editorial theme so you can jump right in and just start documenting without 
worrying about picking or customizing theme templates. 

This bundle also works well when combined with any of the {{ anchor('OrchidJavadoc') }}, 
{{ anchor('OrchidKotlindoc') }}, or {{ anchor('OrchidSwiftdoc') }} plugins, or all three!

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Included In This Bundle

{% include 'includes/bundleItems.peb' with {'included': page.included} %}
