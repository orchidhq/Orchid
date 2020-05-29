---
description: Load the full text of Bible verses quickly and easily.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973072/plugins/bible.jpg
    alt: Bible
    caption: Photo by Priscilla Du Preez on Unsplash
tags:
    - markup
---

## About

OrchidBible makes it easy to embed Bible verses in your site content using 
[Faithlife Reftagger](https://faithlife.com/products/reftagger). Reftagger automatically finds Bible references on the
page, and hovering over them will show a tooltip with the verse text.

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Run [BibleTest](https://github.com/orchidhq/orchid/blob/dev/languageExtensions/OrchidBible/src/test/kotlin/com/eden/orchid/languages/bible/BibleTest.kt) for demo

## Usage

Add the `reftagger` meta-component to your theme to add the necessary scripts to your site. You can customize most of
the options on [this page](https://faithlife.com/products/reftagger/customize), but find it in your admin panel to see
all available options.

```yaml
# config.yml
theme:
    metaComponents:
        - type: reftagger
```
