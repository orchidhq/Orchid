---
description: 'Components are single, logical blocks of content within a Page.'
---

## Overview

Components are single, logical blocks of content within a Page. They typically represent the main content area of a 
Page, with many components being rendered sequentially into that one area. In fact, the Page Content you are currently 
reading is not a static part of the page layout, but is just a Component which renders the page content.

Components are also commonly used to implement sidebars, footers, and other "widget areas" within a layout, and as such
can be defined by the theme. Since 0.18.0, Orchid also supports "[meta components](#meta-components)" whose only purpose 
is to add scripts, stylesheets, and meta tags to a page's `<HEAD>`. There is no difference in whether a Component is 
attached to a Page or a Theme.

Components allow you to build a site with modular "content blocks" that are defined by the end-user, rather than forcing
the user to only use certain pre-defined templates as is normally the case with CMS and SSG themes. Each component is 
easy to set up and use with very little or no configuration required, but when combined with other components allows you
to build highly complex and beautifully unique sites with very little effort. 

## Using Components

### Basic Usage

Components are usually defined in a page's Front Matter as a list of objects, with a `type` property telling Orchid 
which component to use. All pages have a `components` property that holds the normal components for a page, but anything
that has an option of type `ComponentHolder` can declare and use components in the exact same way. 

```yaml

---
...
components:
  - type: 'pageContent'
  - type: 'form'
    form: 'contact'
---
```

Plugins and themes may provide their own component types, and any of them may be used anywhere in your site. Each 
component type typically defines its own options in addition to those outlined below, so be sure to check out the 
plugin's documentation to see what is available. Or better yet, visit your {{anchor('Admin Panel')}} to let Orchid
tell you exactly what components and options are available to you with the guarantee of always being completely 
up-to-date.

More commonly, components are added through {{ anchor('shared configurations', 'Shared Configuration') }}, to set up 
once and be applied to many pages. 

```yaml
# config.yml

# Add additional components to all pages generated from the `OrchidPages` plugin
pages:
  staticPages: 
    components:
      - type: 'pageContent'
      - type: 'form'
        form: 'contact'

# Different components added to all pages generated from the `OrchidWiki` plugin
wiki:
  wikiPages: 
    components:
      - type: 'pageContent'
      - type: 'prism'
```

### Component Ordering

These components are rendered on the page in the order they are declared in that list. You may also explicitly set the 
`order` in which you want your components rendered. If no `order` is manually set, it will be assigned an order of 10x
its position in the list. So the first item defaults to order 10, the second item has order 20, and so on. In the 
following example, the `form` component will be rendered before the `pageContent` component, because it has a lower
`order` value.

```yaml
...
components:
  - type: 'pageContent'
    order: 20
  - type: 'form'
    form: 'contact'
    order: 10

# `form` will be displayed before the `pageContent`
```

### Hiding Components

You may manually hide components by setting `hidden: true`. This has the effect of not rendering a template for that 
Component on the page, but will still include its CSS and JS assets. 

```yaml
...
components:
  - type: 'pageContent'
  - type: 'form'
    form: 'contact'
    hidden: true

# `form` will not be rendered as a template, but its assets will still be included
```

### Component Wrappers

Some themes will choose to wrap each component in additional markup, such as the "boxes" for the page content in the 
{{anchor('OrchidFutureImperfect')}} theme. But in some cases, you may wish that the doesn't 
doesn't apply these wrappers to a particular component, like if it provides its own container markup. For these 
situations, you may set `noWrapper: true` to render the component without a wrapper.

```yaml
...
components:
  - type: 'pageContent'
  - type: 'form'
    form: 'contact'
    noWrapper: true

# `form` will not be wrapped
```

### Custom Component Templates

Sometimes, the template that a plugin provides for a component doesn't look the greatest in your theme, or maybe you 
want to use the same component in multiple places, but with different templates in each, as in the case of the sidebar
and homepage of the [Orchid starter repo](https://github.com/orchidhq/OrchidStarter). For these situations, you can 
provide a custom template to render instead of the default given by the plugin ot theme. The `templates` property 
accepts either a single String or an array of Strings, the first of which that is found will be used.

```yaml
...
components:
  - type: 'pageContent'
  - type: 'form'
    form: 'contact'
    template: 'customForm' 

# `form` will now prefer the `components/customForm.peb` template over `components/form.peb`  
```

### Page Content Component

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

## Component Assets

Components do not need to be declared on a page directly, but they are attached to the page when it is rendered, and is 
able to provide any kind of scripts or styles to the page in which it is being used. 

Some components, like the {{anchor('Swagger Component', 'OrchidSwagger')}} add their own scripts and styles to the 
page. You do not need to do anything for this, the component will take care of telling Orchid to render its assets and
add them to the Page so they end up in the same blocks of scripts and styles that the Theme also provides.

You are also free to add your own assets to any given component. For example, if you wanted to use a `form` component
on all your blog posts, but the styling wasn't quite to your liking, you could make your own SCSS file, add the styling
customizations there, and then tell your `form` component to use your custom CSS. Then, every page that includes your
`form` component will also automatically compile and include your custom styles. The same can be done for extra JS 
assets as well.

```yaml
...
components:
  - type: 'pageContent'
  - type: 'form'
    form: 'contact'
    extraCSS:
      - 'assets/css/form-overrides.scss'
    extraJs:
      - 'assets/js/form-ajax.js'
```

This is particularly useful when you are declaring your components in the {{anchor('Archetypes', 'Shared Configuration')}} of a page rather than
in the page's own Front Matter, as it allows you to add extra assets to numerous pages with a single declaration.

```yaml
# page's Front Matter
components:
  - type: 'form'
    form: 'contact'
    order: 100
      
# config.yml
posts:
  postPages:
    components:
      - type: 'pageContent'
        order: 10
      - type: 'form'
        form: 'comment'
        order: 50
        extraCSS:
          - 'assets/css/form-overrides.scss' 
``` 

## Meta Components

While many components add blocks of content to the page, another common use-case is attaching global assets to pages 
without rendering any content. A good example is the `prism` component from the {{ anchor('OrchidSyntaxHighlighter') }}
plugin, which configures and adds [Prism.js](https://prismjs.com/) to your site for browser-based syntax highlighting.

Normally, when adding this component to a page you would have to also add the `pageContent` component, since it is not
added by default when other components are set. The typical usage would be like the following:

```yaml
components:
  - type: 'pageContent'
  - type: 'prism'
```

This is problematic for several reasons:

1) If multiple archetypes are trying to add similar components in this way, how do you decide which one gets to add 
    `pageContent` and which ones don't? This is especially problematic if these components aren't added globally, but
    only to a subset of you site, especially where those subsets overlap slightly. Without careful planning, you might 
    run into a situation where `pageContent` is added to a page multiple times!
1) Since components are primarily attached to pages, the same component must be configured individually for each page.
    This is inefficient, since ideally the component is a global thing, and probably makes more sense to be attached to
    the theme and only configured once.

To address this, Orchid 0.18.0 introduces the concept of "Meta Components", which are simply a specialization of normal
Components. They can be attached to either the Theme (which is new), or to pages (which is the same as before). For 
backward-compatibility, meta components can still be added to the normal component areas, but only components marked as
such can be added to meta-component areas.

Configuring them is just like regular components, but at the `metaComponents` key:

```markdown
# pages/page-one.md
---
components: 
  - ...
metaComponents:   # Meta components attached to a single page in its Front Matter
  - type: 'prism'
---
```

```yaml
# config.yml

theme:
  metaComponents:   # Meta components attached to the Theme, added to any page using that theme 
    - type: 'prism'

wiki:
  wikiPages:
    metaComponents:   # Meta components attached to all pages in a specific Archetype. No need to specify `pageContent` anymore
      - type: 'prism' 
```
