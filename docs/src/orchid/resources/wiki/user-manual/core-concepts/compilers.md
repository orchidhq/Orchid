---
description: 'Orchid includes native support for compiling Markdown, Asciidoc, Pebble, SCSS, PlantUML diagrams, and more.'
customItems:
    - 'Item One'
    - 'Item Two'
    - 'Item Three'
---

Most content in Orchid comes from a file in your local filesystem. While each plugin is free to do whatever they want to
generate their pages, it is common for plugins to base their internal content models around the filesystem, and as such, 
the content from most plugins is written in a very similar way because the content for each entry works the same.

## Overview 

Generally speaking, the file's extension is used to determine how a file is processed, and the compiler processing the 
file determines the output file extension. Here are some examples:

- `.md -> .html`: Markdown 
- `.peb -> .html`: Pebble 
- `.scss -> .css`: Sass, (SCSS syntax)
- `.sass -> .css`: Sass, (Sass syntax)
- `.ad -> .html`: AsciiDoc (requires {{anchor('orchid-asciidoc-feature')}} plugin)
- `.uml -> .svg`: PlantUML (requires {{anchor('orchid-diagrams-feature')}} plugin)

Orchid does not make a distinction between 'template' languages (like Pebble) or 'content' languages (like Markdown), As
a result, the content for your pages can be written in any language you like.

## Custom Output Extension

The file itself is able to override the default output extension by having a filename in a format like 
`filename.output.input`, such as `contact.php.peb`. This tells Orchid to use the `.php` extension instead of the normal
`.html` extension from the Pebble compiler. The result is `index.php`.

## Ignored Output Extensions

There are some filenames which adhere to the above format that should not be used as the output extension. This is 
common for compiled and minified CSS or Javascript, which is often named something like `styles.min.css`. 

You can set certain extensions to be ignored when the filename is in that format. By default, `min` is already ignored, 
but you may add your own ignored extensions as an array in the `ignoredOutputExtensions` option in `config.yml`:

```yaml
# config.yml
...
services:
  compilers:
    ignoredOutputExtensions:
      - 'min'
      - 'debug'
```

## Precompilers

Typically files are processed as a compiler of their file extension. For example, a file with the `.md` extension will
be processed as Markdown.

But before it is processed as Markdown, if it has a Front Matter header, it will be precompiled as Pebble. You can use
the full power of Pebble templates to add additional dynamic content to otherwise static content.

```yaml
{% verbatim %}
---
---

# {{ page.title }} --> # Custom Page Title
{% endverbatim %}
```

Any variables set in the Front Matter may be accessed as variables in Pebble.

```yaml
{% verbatim %}
---
title: Custom Page Title
foo: 'bar'
---

# {{ foo }} --> # bar
{% endverbatim %}
```

There are a number of predefined variables that are available on every page:

- `page`: refers to the current Page object
- `theme`: refers to the theme rendering the current page
- `site`: refers contains basic info about your Orchid site
- `data`: all custom data loaded from `data.yml` and `data/` files

If you need to use a Front Matter header but don't want to precompile that page, you can set `precompile: false` in the
Front Matter:

```yaml
{% verbatim %}
---
precompile: false
---

# {{ page.title }} --> # {{ page.title }}
{% endverbatim %}
```

Alternatively, you may choose to use another language as your precompiler language of choice. This may be done 
individually by a single page by setting `precompileAs` to the desired language extension in its Front Matter, or for 
all pages by setting the `defaultPrecompilerExtension` option in your `config.yml`:

```yaml
{% verbatim %}
---
precompileAs: 'html'
---

# {{ page.title }} --> # {{ page.title }}
{% endverbatim %}
```

```yaml
# config.yml
...
services:
  compilers:
    defaultPrecompilerExtension: 'html'
```
