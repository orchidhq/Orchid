---
description: 'Pebble is the fastest template engine available for Java, and offers easy-to-read syntax and template inheritance to keep your templates clean and themes easy to develop.'
pebbleUrl: 'https://pebbletemplates.io/'
---

[Pebble]({{pebbleUrl}}) is the main template language used by Orchid. It has a 
syntax very similar to other modern template languages like Liquid, Twig, or Jinja2, and supports template inheritance. 
It also happens to be [one of the fastest](https://github.com/PebbleTemplates/pebble-performance-test) Java template engines, 
period. This all makes Pebble an ideal choice for building themes and helping you manage your content in Orchid. Not to
mention, [https://pebbletemplates.io/](https://pebbletemplates.io/) is built with Orchid!

## Pebble Overview
---

Pebble is a general-purpose template language, meaning it is designed to work with any text-based input and is not 
limited to HTML, unlike some other engines such as JSP and Thymeleaf. For this reason, Pebble is not only the language
of choice for themes, but is also extremely useful to be used as the Precompiler language, as it can help add structure
and consistency to your content, while also making it easy to change later on if needed.

## Template Inheritance
---

[Pebble Source Documentation (`extends`)]({{pebbleUrl}}/tag/extends)
[Pebble Source Documentation (`block`)]({{pebbleUrl}}/tag/block)

Template Inheritance is one of the most powerful features of Pebble. It allows you to treat your entire template not 
simply as a template with a lot of includes for each site component, but rather as a tree comprised of individual 
blocks that can be overridden at any point if needed, and are otherwise left unknown. Essentially, it allows you to 
think of a template more like a Class, which `extends` a parent template and can optionally override its `blocks` just 
as a Class can override it's parent's methods. 

This may be a bit tricky to understand at first, especially if you are not a programmer, so let's just look at a simple
example, and compare it to how a more traditional template without inheritance may look. Lets say that every page on our 
site has a `header`, a `sidebar`, a `body`, and a `footer`. Some of the pages on our site are blog pages, where the body
contains the post content and a comments area. Static pages just contain post content, and the homepage is a fully 
customized HTML landing page. A normal way to set up these templates (in pseudocode) looks like the following:

### Traditional Template Example

{% highlight 'jinja' %}
{% verbatim %}
<!-- homepage.html -->
{% include "header.html" %}
{% include "sidebar.html" %}
<div>Custom Homepage Content</div>
{% include "footer.html" %}
{% endverbatim %}
{% endhighlight %}

{% highlight 'jinja' %}
{% verbatim %}
<!-- post.html -->
{% include "header.html" %}
{% include "sidebar.html" %}
<div>Post Content</div>
<div>Post Comments</div>
{% include "footer.html" %}
{% endverbatim %}
{% endhighlight %}

{% highlight 'jinja' %}
{% verbatim %}
<!-- post.html -->
{% include "header.html" %}
{% include "sidebar.html" %}
<div>Page Content</div>
{% include "footer.html" %}
{% endverbatim %}
{% endhighlight %}


While this approach does work to some extent, it has some problems. 

First, it forces every template type to know about every part of the page, which leads to a lot of repeated code and 
maintainability issues. If you want to add a new template part between the main content and the footer of every page, 
you'll have to either update each top-level template with this new included template, or you'll have to include it 
directly in the footer template, which is semantically incorrect.

The second issue with this is that it leads to a huge number of templates required to create your theme. When every 
piece of your theme that is intended to be reused must be included like this, you will inevitably end up with lots of
small templates which are included or excluded in complicated control patterns that are really difficult to reason about
and maintain. As an example, even "simple" Wordpress themes (which are created very much in this manner) can easily have
dozens of PHP files necessary to build just a small handful of page templates.

Now let's look at the same theme structure, built using blocks and template inheritance (also in pseudocode):

{% highlight 'jinja' %}
{% verbatim %}
<!-- base.peb -->
{% block header %}{% endblock %}
{% block sidebar %}{% endblock %}
{% block content %}{% endblock %}
{% block footer %}{% endblock %}
{% endverbatim %}
{% endhighlight %}

{% highlight 'jinja' %}
{% verbatim %}
<!-- homepage.peb -->
{% extends 'base.peb' %}
{% block content %}
<div>Custom Homepage Content</div>
{% endblock %}
{% endverbatim %}
{% endhighlight %}

{% highlight 'jinja' %}
{% verbatim %}
<!-- page.peb -->
{% extends 'base.peb' %}
{% block content %}
<div>Page Content Content</div>
{% endblock %}
{% endverbatim %}
{% endhighlight %}

{% highlight 'jinja' %}
{% verbatim %}
<!-- page.peb -->
{% extends 'page.peb' %}
{% block content %}
{{ parent() }}
<div>Post Comments</div>
{% endblock %}
{% endverbatim %}
{% endhighlight %}


You'll notice that this setup includes a fourth template, `base.html`. This template is the only one that needs to know
anything about the page's header, sidebar, and footer. The rest of the pages don't care about those page elements, and 
it shows because their templates literally don't even need to know that they exist. They are only concerned with what is
in the `content` block on the page, and when it is their turn to customize the content, they only need to override that
one block. 

The base template can then add or change the blocks as needed, knowing that the changes will be reflected in all other 
templates that inherit from it. And sub-templates can themselves be inherited from, so we can think of a `post` template
as simply a `page` template with a comment section at the end of its content body. You can also see how this reduces the
number of small templates needed in general, because each `block` performs much the same function of an included 
template, but is embedded within the base template itself.

In all, template inheritance offers a much cleaner and easier-to-maintain solution for building themes. The snippets 
shown above are all valid Pebble markup, so you can use those as your base, or you can learn more at the  
[Official Pebble documentation]({{pebbleUrl}}).

## Variables
---

[Pebble Source Documentation]({{pebbleUrl}}/guide/basic-usage#variables)

When rendering a template or precompiling page content, Orchid passes a number of variables that can be used for logic
or for printing directly to the screen. Generally speaking, every time you render with Pebble, you will have the 
following variables available to work with:

| Variable Name | Description | 
| ------------- | ----------- | 
| `page`        | The current page | 
| `site`        | General data for your entire site | 
| `index`       | Your site's entire index, allowing you to find specific groups of pages from it. It is usually better to create a TemplateTag or Component that accesses the index, but you can use it directly if you want. | 
| `config`      | The map of all options set in your `config.yml` and `config/` files. | 
| `data`        | The map of all options set in your `data.yml` and `data/` files. | 

{.table .table-striped}

In addition to the above "template globals", some plugins may add their own global variables. Also, all the variables 
from the Page's Front Matter are added directly to the template as well for easy access.

These variables typically have many other properties that can be accessed from them, check out their Javadocs to find 
out all that you can do with them. They can then be used for iteration or control flow, which is explained later in this
article, or they can be printed directly to the page. 

To evaluate the value of a variable and output it to the page, use the variable's name within pairs of double curly-
braces, like so:

{% highlight 'jinja' %}
{% verbatim %}{{ site.about.siteName }}{% endverbatim %} 
Result: {{ site.about.siteName }} 
{% endhighlight %}

Before being printed out, you may pass the variable expression through a series of `filters`, which change the output
before being passed to the next filter:

{% highlight 'jinja' %}
{% verbatim %}{{ ['Hello', 'World'] | join(' ') | upper }}{% endverbatim %} 
Result: {{ ['Hello', 'World'] | join(' ') | upper }} 
{% endhighlight %}

Note that all content is HTML-escaped before being printed, by default, making it safe to print raw HTML strings out 
without affecting your page structure, and also protects you against certain security vulnerabilities like Cross-Site
Scripting (if you're rendering user-generated content). You may prevent this if you need to with the `raw` filter. In 
addition, literal strings as the only input to an expression are not escaped, as they are assumed to be safe.

{% highlight 'jinja' %}
{% verbatim %}
{% set danger = "<b>Bold Content</b>" %}
{{ danger }}
{{ danger | raw }}
{{ "<b>Bold Content</b>" }}
{% endverbatim %} 
{% endhighlight %}

{% set danger = "<b>Bold Content</b>" %}

- Result (escaped): {{ danger }}

- Result (raw): {{ danger | raw }}

- Result (literal string): {{ "<b>Bold Content</b>" }}

## Control Flow
---

[Pebble Source Documentation]({{pebbleUrl}}/tag/if)

You can use any variable on the page as part of a conditional block of content. 

{% highlight 'jinja' %}
{% verbatim %}
{% if users is empty %}
    There are no users.
{% elseif users.length == 1 %}
    There is only one user.
{% else %}
    There are many users.
{% endif %} 
{% endverbatim %} 
{% endhighlight %}

## Iteration
---

[Pebble Source Documentation]({{pebbleUrl}}/tag/for)

Any `java.lang.Iterable` object (such as `Sets`, `Lists`) can be iterated across, in addition to `Map` entries. And 
unlike traditional iteration in Java, you may include an `else` block within the loop, which is used in the case that 
the input is empty, so you don't have to manually check the input's size first.

{% highlight 'jinja' %}
{% verbatim %}
{% for user in users %}
    {{ loop.index }} - {{ user.id }}
{% else %}
    There are no users to display.
{% endfor %}
{% endverbatim %} 
{% endhighlight %}

{% highlight 'jinja' %}
{% verbatim %}
{% for entry in map %}
    {{ entry.key }} - {{ entry.value }}
{% endfor %}
{% endverbatim %} 
{% endhighlight %}

As a helper within the `for` block, you may access the `loop` variable, which has the following properties:

- `loop.index` - The current iteration within the block (zero-indexed)
- `loop.revindex` - How many iterations remain in the loop, after the current iteration
- `loop.length` - The total size of the object we are iterating over
- `loop.first` - `true` if this is the first iteration of the loop, `false` otherwise
- `loop.last` - `true` if this is the last iteration of the loop, `false` otherwise

## Custom Functions and Tags
---

Pebble ships with a number of tags and filters that are appropriate for most basic usage of a template engine, but 
Orchid extends this with a number of its own tags and filters. Plugins can also contribute new filters or tags, which
means you can also add these as a local plugin, which can be very powerful for your personal workflow. You can learn 
more about how to add your own tags and filters in the Developers's Guide, but here is a list of some that are added by
Orchid:

### Filters

Orchid allows for custom functions and filters to be registered. While Pebble makes a distinction between filters and 
functions (filters designed to mutate data, while functions produce data), Orchid treats them the same. Custom Filters 
simply move the first parameter from inside the function arguments to being the variable that is filtered. So the 
following two snippets are the same. 

{% highlight 'jinja' %}
{% verbatim %}
{{ link('itemId') }}
{{ anchor('title', 'itemId') }}
{% endverbatim %} 
{% endhighlight %}

{% highlight 'jinja' %}
{% verbatim %}
{{ 'itemId' | link }}
{{ 'title' | anchor('itemId') }}
{% endverbatim %} 
{% endhighlight %}

An Orchid function, once registered, can be wrapped and enabled for any template language, not just Pebble, allowing you
to use the same set of tools across many languages if the language is set up to handle it.

### Tags

Tags added by Orchid generally come in two flavors: ones that have a closing tag, and ones that do not. Tags that have
a closing tag consider everything within the opening and closing tags to be arbitrary content, and can be filtered if 
desired, by adding the desired filters after `::` at the end of the opening tag. All custom Orchid tags may have a 
number of possible arguments available, which may be passed sequentially or as named parameters. See the examples below:

**Highlight tag available in the {{ anchor('Orchid Syntax Highlighter') }} plugin**

{% highlight 'jinja' %}
{% verbatim %}
{% highlight 'java' %}
public static void main(String... args) {}
{% endhighlight %}

{% highlight language='java' %}
public static void main(String... args) {}
{% endhighlight %}

{% highlight 'java' :: upper %}
public static void main(String... args) {}
{% endhighlight %}
{% endverbatim %} 
{% endhighlight %}

**Twitter tag available in the {{ anchor('Orchid Writers Blocks') }} plugin**

{% highlight 'jinja' %}
{% verbatim %}
{% twitter "BigBendNPS" "957346111303376897" %}

{% twitter user="BigBendNPS" id="957346111303376897" %}
{% endverbatim %} 
{% endhighlight %}

{% alert 'info' 'Tip' :: compileAs('md') %}
To make it easier to find and use these custom Tags, the {{ anchor('Orchid Netlify CMS') }} plugin
adds all these tags as custom fields within its WYSIWYG editor.
{% endalert %}
