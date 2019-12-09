---
description: A Gitbook-like wiki for your Orchid site. 
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973072/plugins/wiki.jpg
    alt: Wiki
    caption: Photo by Patrick Tomasso on Unsplash
tags:
    - wiki
    - docs
    - content
---

## About

Publish searchable and easily-navigable wikis, inspired by the legacy 
[GitBook CLI](https://github.com/GitbookIO/gitbook), and generate PDFs for offline viewing.

## Demo

- Run [WikiGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidWiki/src/test/kotlin/com/eden/orchid/wiki/WikiGeneratorTest.kt) for demo

## Usage

### Basic Usage

An Orchid Wiki is a collection of pages structured in a freeform hierarchy. Pages in a wiki are all related and ordered, 
with one page linking directly to the next so a user can read the entire contents of the wiki from front-end simply by 
navigating the page links. Orchid Wikis also create Menu items which display the full wiki tree in an easily navigable
fashion.

Wikis in Orchid are inspired by GitBook and are set up in a similar manner. To start, add a file named `summary` (in 
your language of choice) to `wiki/`. This file becomes the landing page for the wiki and can contain any kind of 
content, which gets displayed exactly as written. But it also gets converted to HTML and all links in this file become a 
page in the wiki. 

For example: 

```html
### Wiki Heading

[Getting Started](getting-started.md)
[Basic Setup](setup/basic.md)
[Advanced Setup](setup/advanced.md)
```

corresponds to the following directory structure:

```text
. / (resources root)
├── homepage.md
├── config.yml
└── /wiki
    ├── summary.md
    ├── getting-started.md
    └── /setup
        ├── basic.md
        └── advanced.md
```

which produces the following output:

```text
. /wiki
├── /summary
|   └── index.html
├── /getting-started
|   └── index.html
└── /setup
    ├── /basic
    |   └── index.html
    └── /advanced
        └── index.html
```

The content of those wiki pages can be anything, and can be written in any language as long as there is a Compiler for 
it (just like the summary, and any other page). Orchid also creates a new menu item type which links to every page in 
the wiki and is displayed recursively in the same hierarchy as the pages themselves.

### Wiki Sections 

You can set up multiple wiki sections which each have their own `summary` file, pages, and menu items. The following 
snippet should go in your site's `config.yml`:

```yaml
wiki:
  sections:
    - 'userManual'  # (1)
    - 'developerGuide'  # (2)
```

1) Creates a wiki based on `{baseDir}/userManual/summary.md`, and lives at /wiki/userManual
2) Creates a wiki based on `{baseDir}/developerGuide/summary.md`, and lives at /wiki/developerGuide

You can configure all your sections at once by putting configuration values under the `defaultConfig` property.

```yaml
wiki:
  sections:
    - 'userManual'
    - 'developerGuide'
  defaultConfig: // applied to both userManual and developerGuide sections 
    createPdf: true
```

If you have more than one section in your site, a "section index" page will also be created, linking to each individual
section. This index file will live at `/wiki`.

### Offline Documentation

As of 0.13.0, the wiki plugin includes support for offline documentation. PDFs are an opt-in feature; to enable PDF 
generation, simply add `createPdf: true` to the section configuration for have it generate a PDF with all the content 
for that section of your wiki. Each section will now generate `book.pdf` in the section root.

```yaml
wiki: 
  sections:
    - key: userManual
      createPdf: true
    - key: developerGuide
      createPdf: true
```

The PDF starts with the section `summary.md` as a Table of Contents, and each page in the wiki starts after a page break
in the PDF, and the TOC links to each page. You may override `templates/wiki/book.peb` to customize your PDFs as needed.
PDFs are generated from HTML using the [OpenHTMLToPDF](https://github.com/danfickle/openhtmltopdf) library.

### Wiki Adapters

Orchid is able to connect to external Wiki services, which provide content to embed in an Orchid site. This allows you 
to effectively use these services as a headless CMS and let Orchid publish them as a full website alongside your other 
documentation and content, utilizing the other Orchid features you love like full-text search and PDF generation for
offline viewing.
 
The following plugins provide adapters to external Wikis:

- {{ anchor('OrchidGithub') }} - Connect to a repository's [GitHub Wiki](https://guides.github.com/features/wikis/)
- {{ anchor('OrchidGitlab') }} - Connect to a repository's [GitLab Wiki](https://docs.gitlab.com/ee/user/project/wiki/)

Adapters can be set individually for each Section in your wiki by declaring the intended adapter and its options in you
`config.yml`. See the plugin pages linked above for more info on configuring each wiki adapter.

```yaml
wiki: 
  sections:
    userManual:
      adapter: 
        type: "github"
        repo: "copper-leaf/wiki-with-sidebar"
    developerGuide:
      adapter: 
        type: "gitlab"
        repo: "cjbrooks12/wiki-without-sidebar"
```
