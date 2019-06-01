---
description: 'Components are single, logical blocks of content within a Page.'
---

## About Components
---

Components are single, logical blocks of content within a Page. They typically represent the main content area of a 
Page, with many components being rendered sequentially into that one area. In fact, the Page Content you are currently 
reading is not a static part of the page layout, but is just a Component which renders the page content.

Components are also commonly used to implement sidebars, footers, and other "widget areas" within a layout, and as such
can be defined by the theme. There is no difference in whether a Component is attached to a Page or a Theme.

Components allow you to build a site with modular "content blocks" that are defined by the end-user, rather than forcing
the user to only use certain pre-defined templates as is normally the case with CMS and SSG themes. Each component is 
easy to set up and use with very little or no configuration required, but when combined with other components allows you
to build highly complex and beautifully unique sites with very little effort. 

## Using Components
---

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
{{anchor('Future Imperfect', 'Orchid Future Imperfect')}} theme. But in some cases, you may wish that the doesn't 
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
and homepage of the [Orchid starter repo](https://github.com/JavaEden/OrchidStarter). For these situations, you can 
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
---

Components do not need to be declared on a page directly, but they are attached to the page when it is rendered, and is 
able to provide any kind of scripts or styles to the page in which it is being used. 

Some components, like the {{anchor('Swagger Component', 'Orchid Swagger')}} add their own scripts and styles to the 
page. You do not need to do anything for this, the component will take care of telling Orchid to render its assets and
add them to the Page so they end up in the same blocks of scripts and styles that the Theme also provides.

You are also free to add your own assets to any given component. For example, if you wanted to use a `form` component
on all your blog posts, but the styling wasn't quite to your liking, you could make your own SCSS file, add the styling
customizations there, and then tell your `form` component to use your custom CSS. Then, every page that includes your
`form` component will also automatically compile and include your custom styles. The same can be done for extra JS 
assets as well.

```yaml
---
...
components:
  - type: 'pageContent'
  - type: 'form'
    form: 'contact'
    extraCSS:
      - 'assets/css/form-overrides.scss'
    extraCSS:
      - 'assets/js/form-ajax.js'
---
```

This is particularly useful when you are declaring your components in the {{anchor('Archetypes')}} of a page rather than
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
