---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
[Pebble](http://www.mitchellbosecke.com/pebble/documentation) is the main template language used by Orchid. It has a 
syntax very similar to other modern template languages like Liquid, Twig, or Jinja2, and supports template inheritance. 
It also happens to be [one of the fastest](https://github.com/mbosecke/template-benchmark) Java template engines, 
period. This all makes Pebble an ideal choice for building themes and helping you manage your content in Orchid.  
{% endblock %}

{% block sectionBody %}
## Pebble Overview
---

Pebble is a general-purpose template language, meaning it is designed to work with any text-based input and is not 
limited to HTML, unlike some other engines such as JSP and Thymeleaf. For this reason, Pebble is not only the language
of choice for theme's, but is also extremely useful to be used as the Precompiler language, as it can help add structure
and consistency to your content, while also making it easy to change later on if needed.

## Template Inheritance
---

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


You'll notice that this setup includes a fourth template, `bast.html`. This template is the only one that needs to know
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
[Official Pebble documentation](http://www.mitchellbosecke.com/pebble/documentation).

## Variables
---

## Control Flow
---

## Iteration
---

## Functions and Filters
---

## Custom Functions and Tags
---

{% endblock %}