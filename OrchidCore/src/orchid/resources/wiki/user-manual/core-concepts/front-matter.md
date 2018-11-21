---
customItems:
    - 'Item One'
    - 'Item Two'
    - 'Item Three'
---

{% extends '_wikiBase' %}

{% block sectionIntro %}

## Overview

Front Matter allows you to customize your Pages individually, adding components or menus to just that page, changing the
layout, or using configurations specific to the plugin it came from. The Front Matter is optional, but if used, it must
be the first thing in the file. Front Matter can use any of Orchid's parser languages, but YAML is the most common, and 
comes between pairs of triple dashed lines, which is removed from the actual page output. Here's an example, which is 
the actual Front Matter of this current page:

{% highlight 'yaml' %}
---
title: Custom Page Title
description: Page Descriptiption
---
{% endhighlight %}

## Front Matter Formats

Front Matter can use different languages in two ways: by specifying the language's extension after the first set of 
dashes, or by using "fences" for that languages. As an example, TOML uses `+++` instead of `---`, and JSON uses `;;;`. 
The following blocks of TOML Front Matter are equivalent to the YAML Front Matter block above:

{% highlight 'yaml' %}
---toml
title = "Page Configuration"
customItems = [
  "Item One",
  "Item Two",
  "Item Three"
]
---
{% endhighlight %}

{% highlight 'yaml' %}
+++
title = "Page Configuration"
customItems = [
  "Item One",
  "Item Two",
  "Item Three"
]
+++
{% endhighlight %}

## Custom Delimiters

Orchid allows you to define custom delimiters for extracting Front Matter, such as for compatibility purposes, or for 
using Front Matter as a comment when the normal syntax causes errors in your IDE.

### Example: CSS Comments

{% highlight 'yaml' %}
# config.yml
services:
  compilers:
    customDelimeters:
      - regex: '^/\\*\n(.*)\n\\*/\n'
        group: '1'
        parser: 'yml'
        fileExtensions:
          - 'css'
{% endhighlight %}

{% highlight 'css' %}
/*
title: "Page Configuration"
customItems: 
  - Item One
  - Item Two
  - Item Three
*/

.button {
  ...
}
{% endhighlight %}

### Example: JBake Compatibility

{% highlight 'yaml' %}
# config.yml
services:
  compilers:
    customDelimeters:
      - regex: '^(.*?)\n~~~~~~\n'
        group: '1'
        parser: 'properties'
        fileExtensions:
          - 'md'
{% endhighlight %}

{% highlight 'css' %}
title=Weekly Links #2
date=2013-02-01
type=post
tags=weekly links, java
status=published
~~~~~~

# Markdown content

...

{% endhighlight %}

{% endblock %}