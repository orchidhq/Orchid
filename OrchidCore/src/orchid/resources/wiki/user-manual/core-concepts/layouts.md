---
description: 'Orchid uses Pebble templates and a unique layout composition system for fast and flexible layout design.'
---

## Layouts

When a page is rendered, Orchid will embed it within a layout. The most basic layout looks like:

```jinja
{% verbatim %}
<!DOCTYPE HTML>
<html>
<head>
{% head %}
{% styles %}
</head>
<body>
{% page %}
{% scripts %}
</body>
</html>
{% endverbatim %}
```

Layouts typically come from the theme as a file in the `templates/layouts/` directory, or you may create your own layout
(which usually extends a layout from your theme). They utilize Pebble markup to create the structure for your webpage. 
You are free to use any of Pebble's core features for building flexible and reusable layouts, and Orchid adds a few tags 
of its own:

- `{% verbatim %}{% head %}{% endverbatim %}`: adds the standard meta and SEO tags to the HEAD of your webpage
- `{% verbatim %}{% styles %}{% endverbatim %}`: adds stylesheets from the theme, page, and all components to this page
- `{% verbatim %}{% scripts %}{% endverbatim %}`: adds scripts from the theme, page, and all components to this page
- `{% verbatim %}{% page %}{% endverbatim %}`: renders the main page content and page components

In addition to the above tags, Orchid offers many additional features for building layouts, such as menus, breadcrumbs, 
and additional component areas.

Layouts are chosen entirely by page configuration and are not dynamically chosen based on page type. You can change a 
page's layout with the `layout` property in a page's Front Matter. The default layout is `index`, which selects the 
`templates/layouts/index.peb` template. 

## Page Templates

The `{% verbatim %}{% page %}{% endverbatim %}` renders the page's main components, and one of these is typically the 
special `pageContent` component. This component picks a _page template_ and renders the page's own content into that 
template. 

The most basic page layout (and the default fallback for all pages) looks like:

```jinja
{% verbatim %}
{{ page.content | raw }}
{% endverbatim %}
```

Page templates are usually defined by plugins as a file in the `templates/pages/` directory. Alternatively, you may 
create your own page template. They are dynamically chosen based on the type of page being rendered. 

The page type is determined by the plugin that created the page, and it may be set up such that a series of page 
templates may be tried based on page data, in a manner similar to the 
[Wordpress template hierarchy](https://wphierarchy.com/). Of course, Orchid does 
not follow the same hierarchy, but it was the main inspiration for how Orchid chooses page templates and it may give
you a better intuition on how to set up your templates.

## Debugging Layouts

Having trouble figuring out which layout or page template is used to render a page? When running Orchid in "serve" mode, 
a comment section is added to the HEAD of each page with some useful information, such as the current template, the page
type, the list of possible page templates, and the resolved layout/page template.
