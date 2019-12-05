---
description: Add static pages with rich taxonomy to your Orchid site.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1537027621/plugins/pages.jpg
    alt: Pages
    caption: Photo by Annie Spratt on Unsplash
tags:
    - content
---

## About

Static pages allow you to add pages to your Orchid site in with ultimate flexibility. Simply drop content files in your 
`pages/` directory, and they will be rendered in your site at the same URL as they reside in that directory. Pages can 
contain any content, use any layout, and have no restriction on their URL path, because it is simply their location on
disk that determines their URL, which all gives you a ton of flexibility with static pages.

## Demo

- Try the [starter app](https://github.com/orchidhq/OrchidStarter)
- Run [PagesGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidPages/src/test/kotlin/com/eden/orchid/pages/PagesGeneratorTest.kt) for demo

## Usage

### Basic Usage

Any files in the `pages/` directory will be compiled according to their file extension to a page at the same path they 
are in the `pages/` directory. Pages with a filename of `index` are special, in that they will become the index page for 
the directory they are placed in, rather than living at a path ending in `index`: 

```text
. / (resources root)
├── homepage.md
├── config.yml
└── pages/
    ├── changelog.md <-- compiled as Markdown to /changelog
    └── features/
        ├── index.md <-- lives at /features
        ├── feature-one.md <-- lives at /features/feature-one
        └── feature-two.md <-- lives at /features/feature-two
```

### Page Groups

When placing pages into subdirectories, the specific subdirectory they are placed in typically defines a top-level 
categorization for those pages. In Orchid, the first directory a Static Page is placed in is known as its **group**. 
Pages can then be located from a Collection, filtering by their Group. 

Page groups are useful for configuring all the pages in a group from a single Archetype. They are also very useful in 
combination with the {{ anchor('Taxonomies plugin', 'OrchidTaxonomies') }} to generate archive listings for each page
group.

### Menu Items

The Pages plugin comes with two useful menu items: `pages`, and `pageIds`.

The `pages` menu item adds all static pages to a menu, optionally filtered by page group. 

The `pageIds` menu item will parse the page content, looking for header tags (`h1`, `h2`, etc.) with ID attributes 
(these are added in Markdown by default) and generate a menu with links to each header. You can customize the level of 
headers included in this menu with the `maxLevel` and `minLevel` options. By default, all items are displayed flat, but 
you can have them set up in a nested structure matching the hierarchy of header tags on the page by setting 
`structure: nested`.

### Render Modes

You may wish to change how static pages are rendered, for example to be rendered raw and the page to be the entire, 
fully-customized HTML of that page. To do this, you can set the `renderMode` to `raw` in a page's Front Matter to just 
render its contents directly and not use a layout at all. You may also set `usePrettyUrl` to determine whether to use
a "pretty" URL (like /contact) or not (like /contact.html).
