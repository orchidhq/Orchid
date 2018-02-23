---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
{% endblock %}

{% block sectionBody %}

## Pages
---

Pages are the most fundamental unit of content in an Orchid site. A Page represents a single HTML file in the output of 
your site, and the rest of Orchid revolves around the Page. The following outlines the key players in an Orchid site
and how they all interact:

- Orchid asks Generators to know which Pages they intend to produce.
- Orchid adds these Pages to a single Index.
- Orchid passes these pages back to their respective Generators to be rendered.
- During rendering, a Page asks the Theme how the final output should look. This involves the creation and display of 
Menus and Components. Pages, Themes, Generators, or anything else can request Menus or Components be displayed.
- Menus use the Index to decide which items end up in the menu, which is displayed within the Page.
- Components are small sections of the Page which are relatively isolated in scope, but may request data that has been 
Indexed, either as a requirement or for simplified usage. 

As an object in Orchid, the page is quite small, with not much to say about it. Conceptually, however, the Page is the 
largest and most important player in Orchid, and the following pages in this section of the Wiki will go through each of
the remaining players one-by-one, to show how they all interact.

## Themes
---

Themes in Orchid serve many functions. They provide layouts, define menus, pick the primary CSS and Javascript used on
a page, and in general provide a full webpage to hold Page content. 

Orchid themes are designed to be swapped out seamlessly, so that, in many cases, you don't need to change any 
configuration at all to give your site a completely different design. You can even use multiple themes, a new one for 
each Generator, allowing you to pick the best theme for each type of content.

Unlike many other static site generators, Orchid themes are not contained within your Orchid project, so at no point are 
you locked into using one theme forever, or requiring a ton of work to change themes.


## Generators
---

Generators are the work-horses of Orchid. They have two jobs: to decide what Pages should exist for a particular section
of the site, and how we need to process content to create that page. Generators work in two phases: the Indexing phase, 
and the Generation phase: 

### Indexing Phase

During the Indexing phase, Orchid asks each Generator, in turn, for a list of Pages it intends to produce. The methods
that each generator uses to determine these pages is irrelevant; all Orchid cares about is the end result, which is that
there are some Pages that need to be rendered into the output site. 

It is common for Generators to create Pages that correspond directly to a source file, such as with Blog posts, Static
Pages, or the Wiki you're currently reading. Another common occurrence is for some external program to parse a content
model from source code, which is then integrated into Orchid. Such is the case with the Orchid Javadoc and KSS plugins. 

Generators may also index content that is not intended to be a page in the output, but as the content for a component or 
menu. Such content is typically smaller in scope but used on many pages. An example would be the Orchid Forms and 
Presentations plugins. Each Form has a non-trivial configuration, and a single form may be used across many pages. By 
indexing the form definition in a Generator, we can be sure that all forms of a given type are always kept in sync
throughout the site, and are immune to changes not propagating.

### Generating Phase

After all Generators have finished indexing their content, Orchid does some processing on these output Pages before 
passing them back to their source Generator to be rendered. Pages can be rendered in several ways: into a Layout 
template resource, into a String layout, as raw content, and as a binary stream. 

**Rendering as Layout**

When rendering a page as a template, Orchid asks the Page what layout it should use. Orchid then attempts to locate that
layout in the theme or in the local resources directory, which takes precedence over the theme-defined layouts and allow
for themes to be customized without needing access to the theme files itself. 

**Rendering as String**

Rendering a stream is similar to rendering as a layout, except that the template definition comes as a static String 
rather than an override-able resource. This is typically more useful in testing, but may be used in specific situations
if the Generator calls for a very specific layout and doesn't want to allow it to be changed.

**Rendering Raw**

Many types of pages should not be rendered into a layout but should still have its content processed, such as compiling 
SCSS into CSS. The content of these Pages is loaded and processed as a String.

**Render Binary**

Other pages will become corrupted if the source content is loaded as a String, and must be treated as a stream of bytes,
such as images, videos, or PDF files. The content of these pages are not processed at all, and are simply copied 
directly from the source to the destination.

Pages may be set as a draft, which will skip the rendering of these pages. This is done behind the scenes and is not
a concern of the Generator. The Generator should simply render all pages passed to it as if it should be rendered.

Generators can also set a new theme to be used just for its own pages, which completely changes the layouts, CSS/JS, 
menus, etc. that are loaded for these pages.

## Menus
---

Menus are typically defined by the Theme and by the Page, and it is common for both kinds of menus to appear on a single
output Page's layout. Menus typically pull pages from the Index to dynamically generate the menu items, so all that
is required to keep a menu up-to-date is to choose the appropriate menu item types.

It is common for plugins to define their own menu item types, especially ones that correspond directly to the Pages a
Generator in the plugin creates. The exact methods of pulling indexed pages into a menu item are left up to the plugin, 
and may be as opinionated as showing the latest blog posts in a single category, or as generic as simply asking for a 
URL to link to.

## Components
---

Components are single, logical blocks of content within a Page. They typically represent the main content area of a 
Page, with many components being rendered sequentially into that one area. In fact, the Page Content you are currently 
reading is not a static part of the page layout, but is just a Component which renders the page content.

Components are also commonly used to implement sidebars, footers, and other "widget areas" within a layout, and as such
can be defined by the theme. There is no difference in whether a Component is attached to a Page or a Theme.

**A Word about Page Content**

Most pages include a Component that represents the intrinsic content of that Page. For example, a Blog post comes from a
single source file and produces a single Page whose "content" is simply the text content of that file. This component is 
added to Pages by default, unless the Generator or Page says otherwise. If you specify custom components to use on a 
Page and want to include the Page content, make sure to add the "pageContent" component.

When rendering Page Content, it is common for the pages produced by different Generators to have different requirements
in rendering. For example, a Page produces by the OrchidPages may want to show a list of tags and its author, while a
Page produced by the Wiki may want to highlight the Wiki section currently being read. To support this, Page Content
components load a "page template" in a similar way to how a page Layout template is chosen, but is just for that one, 
logical block of content within a page, rather than the entire page. 

This even allows plugins to create simple, yet semantic, HTML page templates that look good in any theme, but that 
Themes can then override to make much more custom or bring more fully in-line with its own styling. These page templates
then always look the same for a given page type, regardless of the layout chosen, which aids in maintainability of a 
theme.

## Collections
---

Collections take groups of pages or other indexed content and make it accessible. Whenever you want to create a link 
from one page to another, you will typically use one of the `anchor`, `link`, `find` or `findAll` Pebble functions to 
lookup the related content. All these functions implement a similar interface, so using one over another is simple and 
always familiar. 

Internally, these functions find the related content based on the Collections that a each generator has set up, 
delegating the hard work of determining how exactly to find the content to the plugin, while keeping a common linking 
strategy across all plugins, regardless of how simple or complex their lookups are.

{% endblock %}
