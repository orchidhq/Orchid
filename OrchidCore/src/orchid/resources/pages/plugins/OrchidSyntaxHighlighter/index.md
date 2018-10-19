---
official: true
noDocs: true
description: Add syntax highlighting with Pygments or PrismJS.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973700/plugins/syntaxhighlighter.jpg
    alt: Syntax Highlighter
    caption: Photo by William Iven on Unsplash
menu:
  - type: 'page'
    itemId: 'Orchid Syntax Highlighter'
  - type: 'pageChildren'
    itemId: 'Orchid Syntax Highlighter'
    asSubmenu: true
    submenuTitle: Docs
---

### About Orchid Writers' Blocks

This plugin adds a `highlight` Tag, which will highlight your code using Pygments. You must include 
`assets/css/pygments.scss` in your `extraCss` or include a custom Pygments theme for the highlighting to work. Pygments
supports [many languages](http://pygments.org/languages/) out of the box for you with no additional configuration.

{% highlight 'jinja' %}
{% verbatim %}
{% highlight 'yaml' %}
title: 'Page Title'
components:
  - type: pageContent
{% endhighlight %}
{% endverbatim %}
{% endhighlight %}

Alternatively, you may opt for a browser-based solution using PrismJS. This may offer greater flexibility and works with
the normal Markdown syntax, but requires Javascript to function. You will also need to manually add language definitions
for each language you intend to highlight, which may not be feasible for sites which need many languages highlighted.

{% highlight 'yaml' %}
title: 'Page Title'
components:
  - prism: 'pageContent'
    languages: 
      - 'java'
      - 'yaml'
{% endhighlight %}
