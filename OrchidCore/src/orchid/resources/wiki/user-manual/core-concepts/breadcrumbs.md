---
description: 'Orchid maintains a structured and flexible page hierarchy which allows for automatically-generated breadcrumbs.'
---

## Breadcrumbs

Breadcrumbs are a common feature of many websites, and help visitors know where they are in the logical hierarchy of the
site. Orchid lets you add breadcrumbs to your pages, automatically building the breadcrumb structure based on what 
Orchid or its plugins know about your site. 

### Rendering Breadcrumbs

Including breadcrumbs in your site is really easy, just add the following line to your template wherever you'd like to
place your breadcrumbs.

{% highlight 'jinja' %}
{% verbatim %}
{% breadcrumbs %}
{% endverbatim %}
{% endhighlight %}

This will tell Orchid to lookup the breadcrumbs for the current page, and display it at that location. By default, it 
uses Bootstrap 4 markup (which is fully-compatible with Bootstrap 3), but as breadcrumbs are rendered as a Template Tag, 
the styling can be overridden by your theme or by creating a custom `templates/tags/breadcrumbs.peb` template.

In many cases, your theme will automatically include breadcrumbs for you, and so everything is already done for you. If
your theme does not supply breadcrumbs, you are free to use the tag to render them wherever you wish. A good place do 
add this by creating a custom Layout, or by overridding the Page Template for the page types you wish to have 
breadcrumbs. 

### Customizing Breadcrumb Structure

By default, Orchid will use a page's `parent` to determine how to build the breadcrumb structure. It will navigate up
through the hierarchy of parent pages, stopping when a page has no parent. Many plugins, such as Wikis, will configure 
its own pages to have the parent page already set, and so rich breadcrumbs will come for free on these pages.

For more control over the breadcrumbs for a particular section of your site, you can use the {{anchor('Static Pages')}}
plugin and set a reference to each page's parent page in its Front Matter. You can use a `key=value` reference in the 
Front Matter to link to a page with a specific Front Matter value, or use the page title. 

The following example shows how to manually build breadcrumbs with pages from the Static Pages plugin.

{% highlight 'yaml' %}
# pages/locations/all.md
---
title: 'All Locations'
location: 'All'
---

{% verbatim %}
# {% breadcrumbs %} renders as 'All Locations' 
{% endverbatim %}
{% endhighlight %}

{% highlight 'yaml' %}
# pages/locations/all/usa.md
---
title: 'United States'
location: 'USA'
parent: 'location=All'
---

{% verbatim %}
# {% breadcrumbs %} renders as 'All Locations / United States' 
{% endverbatim %}
{% endhighlight %}

{% highlight 'yaml' %}
# pages/locations/all/usa/texas.md
---
title: 'Texas'
location: 'TX'
parent: 'location=USA'
---

{% verbatim %}
# {% breadcrumbs %} renders as 'All Locations / United States / Texas' 
{% endverbatim %}
{% endhighlight %}
