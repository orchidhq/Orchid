---
description: 'Pages with a Front Matter header will first be precompiled as using Pebble.'
customItems:
    - 'Item One'
    - 'Item Two'
    - 'Item Three'
---

Typically files are processed as a compiler of their file extension. For example, a file with the `.md` extension will
be processed as Markdown.

But before it is processed as Markdown, if it has a Front Matter header, it will be precompiled as Pebble. You can use
the full power of Pebble templates to add additional dynamic content to otherwise static content.

{% highlight 'yaml' %}
{% verbatim %}
---
---

# {{ page.title }} --> # Custom Page Title
{% endverbatim %}
{% endhighlight %}

Any variables set in the Front Matter may be accessed as variables in Pebble.

{% highlight 'yaml' %}
{% verbatim %}
---
title: Custom Page Title
foo: 'bar'
---

# {{ foo }} --> # bar
{% endverbatim %}
{% endhighlight %}

There are a number of predefined variables that are available on every page:

- `page`: refers to the current Page object
- `theme`: refers to the theme rendering the current page
- `site`: refers contains basic info about your Orchid site
- `data`: all custom data loaded from `data.yml` and `data/` files

If you need to use a Front Matter header but don't want to precompile that page, you can set `precompile: false` in the
Front Matter:

{% highlight 'yaml' %}
{% verbatim %}
---
precompile: false
---

# {{ page.title }} --> # {{ page.title }}
{% endverbatim %}
{% endhighlight %}

Alternatively, you may choose to use another language as your precompiler language of choice. This may be done 
individually by a single page by setting `precompileAs` to the desired language extension in its Front Matter, or for 
all pages by setting the `defaultPrecompilerExtension` option in your `config.yml`:

{% highlight 'yaml' %}
{% verbatim %}
---
precompileAs: 'html'
---

# {{ page.title }} --> # {{ page.title }}
{% endverbatim %}
{% endhighlight %}

{% highlight 'yaml' %}
# config.yml
...
services:
  compilers:
    defaultPrecompilerExtension: 'html'
{% endhighlight %}
