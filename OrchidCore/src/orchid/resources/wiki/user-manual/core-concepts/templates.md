---
description: 'Orchid uses Pebble templates and a unique layout composition system for fast and flexible layout design.'
---

## Overview

Templates in Orchid all live within your resources' `templates/` directory, regardless of whether it comes from local 
resources, plugins, or themes. Anytime a layout is requested or another template is included/extended from Pebble, the
path is referenced relative to `templates/`, even if it is not specified. It is not necessary to specify the extension
of template files, either, as Orchid will infer the exact extension of a template based on what language the theme 
prefers.

## Layouts

When a page is rendered, Orchid will embed it within a layout. The most basic layout looks like:

```twig
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

```twig
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

### Example: Layouts vs Page Templates

To demonstrate the differences between layouts and page templates, lets build a simple example. In this example, you
are building a site with a main landing page which should span full-width and contain custom markup throughout the 
entire content body. You also have static pages that are also full width with custom content, but the content includes 
some extra information for each page like the author and some additional links set in each page's front matter. You also
have a blog, where the pages each contain a sidebar with a listing of the tags on each post, and a wiki, where the 
sidebar contains the full menu for the wiki. 

A normal approach to setting these up would produce the following templates, each listed with the template they extend:

- `homepage.peb` extends `single.peb`
- `staticPage.peb` extends `single.peb`
- `post.peb` extends `sidebar.peb`
- `wiki.peb` extends `sidebar.peb`

The problem with templates set up in this manner is that it forces each "page type" to know about the layout is is 
contained in, when in reality the only thing that the template actually cares to customize is what's in the main content
body on the page. It also limits each page to _only_ being able to use that one type of layout, unless you were to 
copy the templates and make a new version for each page/layout combination:

- `homepage-single.peb` extends `single.peb`
- `homepage-sidebar.peb` extends `sidebar.peb`
- `staticPage-single.peb` extends `single.peb`
- `staticPage-sidebar.peb` extends `sidebar.peb`
- `post-single.peb` extends `single.peb`
- `post-sidebar.peb` extends `sidebar.peb`
- `wiki-single.peb` extends `single.peb`
- `wiki-sidebar.peb` extends `sidebar.peb`

Page Templates set up in this manner is very much how themes in Wordpress are set up, and it ends up giving you a large
number of theme files that become difficult to manage and refactor. And while you may be able to move the logic of the 
page "layout" into some conditional logic that sets up the actual layout to reduce the number of files needed, it still 
requires the content body of the page to know information about the rest of the page that it really doesn't care about. 
Contrast this the Orchid model of page templates and layouts:

- Layouts
    - `single.peb`
    - `sidebar.peb`
- Page Templates
    - `homepage.peb` doesn't extend anything
    - `staticPage.peb` doesn't extend anything
    - `post.peb` doesn't extend anything
    - `wiki.peb` doesn't extend anything

This now allows us to embed _any_ page template into _any_ other layout, and neither one needs to know anything about
the markup, blocks, or filenames names of any of the others. You can even add a third layout, and start using that one 
without needing to change anything about the page templates, and no matter how many layouts or page types you have, you 
will always be able to mix-and-match them as you please with no issues.

All this allows Layouts to truly just be that: a template which "lays out" the various site pieces into their location
within the page. The Page Template is then only concerned with its page-specific content and structure, and doesn't care
about the layout or even the theme, in which is is embedded.

## Component Templates

Component Templates work exactly the same as Page Templates, but for each Component on the Page. The Component type can
specify its own list of possible template patterns, which are expanded into a full list within the `components/` 
`{templateBase}` directory. The only difference here is that there is not "default" component template like there is 
with a Page Template, and it is expected that plugins provide a template for the most generic markup needed for the 
specific component to look good.

## Tag Templates

Contrary to the flexibility of Components and Page templates, Tags are quite strict in the templates they allow for 
themselves. This is intentional, as Pages and Components are by nature customized by the content of the page within 
which they reside, and what they output may look different based on the page using them. By contrast, Tags are intended
to only be concerned with its inputs and outputs, and not have a template that is contingent on anything other than what 
it itself says. To that end, all custom Tag templates are found as the tag name within the `tags/` directory, and that's
it.

{% verbatim %}

### Simple Tags

Some tags are simple and do not include a content block. Tags like `{% head %}` or `{% page %}` are exactly that and no
more. They may have some configuration values, but all content for these tags is up to the template used for these tags.

### Content Tags

Some tags are take a content block and embed it somewhere within the tag's template. Tags like `{% alert %}` must end 
with a closing tag like `{% endalert %}`, and everything between the start ane end tags will be embeded as the tag's 
content. 

The tag content is always formatted according to Pebble by default, which may be confusing if the tag is precompiled
in a Markdown page, where you'd expect the content to be formatted as Markdown. However, you can supply template filters
to Content Tags which are applied to the content before being embedded. For example, to compile a tag's content as 
Markdown:

```twig
{% alert :: compileAs('md') %}
## This will be a Markdown header

** this is bold**
{% endalert %}
```

### Tabbed Tags (Static)

Some tags, like `{% tabs %}` or `{% accordion %}` can contain _multiple_ content sections. 

For tabbed tags where all tabs are statically-defined, you set them up as children of the main tag where each child tag
is a custom-defined key, uniquely identifying each "tab".

```twig
{% tabs %}
    {% one %}Tab Content One{% endone %}
    {% two %}Tab Content Two{% endtwo %}
{% endtabs %}
```

Just like normal Content Tags, the content is processed as Pebble, but you can supply filters to the tag to compile it 
in another way. You can provide filters on each individual "tab", or on the parent tag to be applied to all child tabs:

```twig
{% tabs :: compileAs('md') %}
    {% one %}Tab Content One{% endone %}
    {% two %}Tab Content Two{% endtwo %}
{% endtabs %}
```

```twig
{% tabs %}
    {% one :: compileAs('md') %}Tab Content One{% endone %}
    {% two :: compileAs('md') %}Tab Content Two{% endtwo %}
{% endtabs %}
```

### Tabbed Tags (Dynamic)

In many cases, tabs are not defined statically, but rather are dynamically created from data. In these cases, you must
declare the tag as `dynamic`, so that the content can be evaluted with loops, `ifs`, etc. As the contents are 
_evaluated_, tabs are "pushed" into the parent, which then renders them just the same as if they were defined 
statically.

```twig
{% tabs dynamic :: compileAs('md') %}
    {% for i in range(1, 3) %}
        {% tab ''~i %}Tab Content {{i}}{% endtab %}
    {% endfor %}
{% endtabs %}
```

{% endverbatim %}


## Template Overrides

Orchid has a well-defined order in which resources of any type are identified. This ordering sets Orchid up such that
plugins provide basic templates which can always be customized later by the chosen Theme to match the markup of the 
entire site. But any resource defined by a theme or a plugin can always be overridden by your local site, so you never
have to edit core theme files to tweak the output of your site.

## Resource Lookup Order

The exact order in which all resources are located is shown in the diagram below. A resource can be anything, including 
your templates and your content files, and each plugin or theme provides the resources bundled in its JarFile. 

![Resource Lookup Order]({{'assets/media/resource-lookup-order.png'|asset}} "Resource Lookup Order")

### Local Resource Sources

Orchid ships with a single Local Resource Source, which locates resources as files within your `resourceDir`. This is 
typically `src/orchid/resources` within the normal Gradle project structure, but can be changed by setting the `srcDir`
in the `orchid` configuration block of your `build.gradle`.

Local resource sources are special in the fact that plugins typically index content based on local resources only.

While not currently implemented, Orchid supports having multiple Local resource sources in the case that you want to 
host local resources in multiple base directories or connect to a [Headless CMS](https://headlesscms.org/) which manages
your content remotely or add content from your own private APIs or CMS. See the Developer's Guide for more on 
implementing new resource sources.

### Theme Resource Sources

Themes are defined as being a Resource Source in themselves; that is, the Theme class directly provides Resources to 
Orchid from its JarFile. The resources that come from a theme are typically the final templates and assets that will be 
used in the rendered site, providing customization of those templates and assets from plugins which are usually fairly 
generic.

When resolving a resource that doesn't exist in a local source, only the current theme is considered. The "current" 
theme is usually the default theme set in `build.gradle` (in all cases except when rendering), or the theme that is set
as a custom theme for a generator in `config.yml`. 

All assets (CSS and JS only) provided by a theme are rendered in the output site with a "namespace" that allows multiple
instances of the same theme to each provide different versions of the same assets (e.g. stylesheets with different 
colors) without overwriting each other. This namespace is transparent to the theme picking which assets to add and which
ones are loaded on any given page, but the path of assets in the output site won't be the exact same as their input 
path. 

### Plugin Resource Sources

Plugins that contribute new things like Template Tags or Components must provide their own default templates so that 
they can always be dropped into any new theme and still work great. Components may also inject CSS or Javascript into a
page as well, which also comes from the plugin's resources. Keep in mind that _any_ resource provided by a plugin can
**_always_** be overridden by the theme or by your local resources, but without any customization plugins will still 
have everything they need to function perfectly. This makes it very easy to add new plugins to your site, as they 
usually require no setup at all to work.

## Debugging Templates

Having trouble figuring out which layout or page template is used to render a page? When running Orchid in "serve" mode, 
a comment section is added to the HEAD of each page with some useful information, such as the current template, the page
type, the list of possible page templates, and the resolved layout/page template.
