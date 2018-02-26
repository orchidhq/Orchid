---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
{% endblock %}

{% block sectionBody %}

Orchid is designed to be used with Gradle, and it works best when integrated directly in your existing Java projects. 
The standard Orchid directory structure is just a normal Maven project, where the Orchid source content is in 
`src/orchid` instead of `src/main`. 

Within `src/main/resources` exists your content: your static pages, blog posts, wikis, site config, etc. It can also 
contain additional templates to use either in place of or alongside your theme. The Resources directoy is covered in 
more detail below.

In addition to providing resources, the `orchid` Gradle configuration can also supply code to the Orchid runtime. Any 
JVM-compatible (Java, Kotlin, Scala, etc.) code can be included, and Gradle will compile it and include it when running
Orchid. Internally, Orchid uses [Guice](https://github.com/google/guice) for dependency-injection, and there are 
numerous extension points to include your custom code. There is fundamentally no difference between code that is 
included from a plugin and code that is included locally, which removes the burden of requiring packaged plugins that is 
common for many frameworks, and allows you to create closed-source Orchid plugins for your private use. 

> Note that Orchid is licensed under the GNU Lesser General Public License v3 (LGPL-3.0), so any modifications to the
> core must be open-sourced under the LGPL. However, you are free to include Orchid as a library in your private or 
> proprietary projects or create closed-source Orchid plugins for private use or sale without having to disclose your 
> source code. Just don't do anything malicious with it, and please provide a link back to the Orchid source so your 
> team knows about it, and so Orchid is attributed appropriately.

{.alert .alert-info}

[TOC]

## Directory Overview
***

A typical Orchid resources directory is structured something like the following. Each item in the resources is described
briefly here, but is explained in much more detail in later pages of this User Manual.

{% filter compileAs('uml') %}
@startsalt
{ {T
+ / (resources root)
++ config.yml
++ homepage.md
++ assets/
+++ css/
+++ js/
+++ images/
++ data/
++ templates/
+++ layouts/
+++ pages/
+++ components/
+++ includes/
++++ meta.peb
} }
@endsalt
{% endfilter %}

* `config.yml` (required) - The main site configuration
* `homepage.md` - The content for your site's root page. If no homepage is found, one will be created with your 
    project's README and LICENSE automatically included.
* `assets/` - The typical location for user-provided assets. The exact directory can be changed in your `config.yml`, 
    and files in the directory will be compiled and copied over as raw static assets.
* `assets/css/` - When compiling SCSS to CSS, `@import` statements are all resolved local to this directory. It is not
    necessary to put all SCSS files within this one directory, but it is encouraged.
* `assets/js/` - The typical location for included JS.
* `assets/images/` - The typical location for uploaded images.
* `data/` - The data files in this location are merged into `config.yml` before being passed to the rest of the site
* `templates/` - Contains additional templates you define. Typically, your theme will provide a fairly robust set of 
    layouts, page templates, includes, etc, but you may want to make minor tweaks to make your site unique. Simply drop 
    a template in here with the same name and it will be chosen instead of the theme's template. You can even add 
    additional templates that your theme doesn't ship with to really make your site your own.
* `templates/layouts/` - Layouts are the root templates that get chosen when rendering a page with a template.
* `templates/pages/` - Page content is not an intrinsic part of the layout, but rather is included as a Component. Each 
    page type is capable of providing its own template to differentiate the different types of pages. You are free to 
    add additional page templates not normally supported by the plugin or theme, or override those that are.
* `templates/components/` - Pages typically have several Components, each with their own templates. You can override 
    Component templates here or add new ones for specific circumstances. 
* `templates/includes/` - Generic templates that are included elsewhere. 
* `templates/includes/meta.peb` - Inject tracking scripts or other meta information into the `head` of every page. Every
    theme should support this, and many themes also support other `meta`-like templates. All official Orchid themes also
    support `trackingHeadStart`, `trackingHeadEnd`, `trackingBodyStart`, and `trackingBodyEnd`. 

## Plugin Directory Structures
***

In addition to the directory structure above, individual plugins may add more content to your site based on other, more 
structured directories. The Orchid plugins that have some kind of special resource directory structure is described 
briefly below. All these plugins are optional, but are included in the `OrchidAll` bundle, and you should visit each
plugin's own documentation to see the full usage and discover all configuration options.

### Posts

Posts follow a structure similar to Jekyll, but altered slightly to better accommodate very large blogs. Posts from from
the `posts/` directory, and each file must have a filename format like `YYYY-MM-DD-post-slug`. However, you may group
posts into subdirectories representing the dates if it is more convenient (a post's full directory and filename just has
its path separators replaced with `-`, and that is checked against the above format).

Posts can also be grouped into categories, which must be listed in `config.yml`. These categories use the base directory
of `posts/:category`, from which individual post entries are found the same as before. Posts can also have any number of
tags, and paginated archives are generated for all categories and tags.

### Pages

Pages is more similar to a lightweight version of Hugo's "content" than Jekyll's static pages, in that is it expected 
that the files in Pages reflect their structure in the resulting site. All files in the `pages/` directory are compiled 
and copied over into a layout with the same folder structure with which they exist in the source directory. It is not 
required that any page has Front Matter in order to be compiled (like in Jekyll), and these pages are given a "pretty"
URL by default. 

### Wiki

The Wiki was inspired by Gitbook, and its files live in `wiki/`. Like Posts, Wikis can have multiple sections defined in
`config.yml` which sets the base directory to `wiki/:section`.

Each Wiki section (or the root `wiki/` directory if there are no sections) should have a `SUMMARY.md` file. This file 
can include whatever content you wish, but any links within the SUMMARY page are used to determine the entries and the
ordering of pages in the Wiki. 

### Forms

The Forms plugin offers a novel solution to accepting user input, inspired by OctoberCMS and Netlify's form handling 
service. Forms are defined in YAML using a structure closely resembling the admin forms in OctoberCMS (but much more 
limited). These form definitions come from the `forms/` directory, and can even be loaded from plugins, so you could 
create a third-party plugin with lots of common Form formats. Forms are then displayed on a page with a Form Component, 
which reference a filename in the `forms/` directory.

Forms can also be defined in the Front Matter of a content page (such as Markdown or Asciidoc). In this case, the form
definition should be in the `form` key of the page's Front Matter (instead of at the root), and the content of the page 
will be rendered to the final site as a confirmation page for the form. The target page's URL is set as the form 
`action` (if one isn't already supplied), which Netlify will understand as the confirmation page. The confirmation 
page's URL is also added to the form as a hidden field so any backend can redirect to that page after a successful 
submission. 

### Presentations

Presentations group multiple content files as individual slides for a Deck.js presentation. Slides are defined as a 
subfolder within the `presentations/` directory, and files within that subfolder are set as the slides of a presentation
in the order they are read from the file system. To manually order slides, you may prepend a number before a dash to the
filename, which will be removed from the slide ID as it is rendered into the presentation deck. 

### Changelog

The Changelog allows you to track changes across your application by simply dropping a file in the `changelog` 
directory. The filenames don't matter as they are just used to render a Changelog component, but the version should be
set within the Front Matter, and arbitrary content as the release notes. A JSON file is also rendered in the output site
containing the names and links to all project versions, so that version-pickers can be implemented in Javascript and 
users of older versions become aware of newer versions.

One helpful trick to ensuring all release versions are accounted for (used for the Orchid release notes), is to include 
a check in your CI process to ensure the existence of this file at the current release tag. If you force this file to 
exist before releasing any update, you can be sure that all releases have notes available immediately.

{% endblock %}