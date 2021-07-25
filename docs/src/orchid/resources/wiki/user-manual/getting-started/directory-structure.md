---
description: Overview of Orchid's directory structure
---

A typical Orchid site looks like this:

```text
. / (resources root)
├── homepage.md
├── config.yml
├── config/
├── data.yml
├── data/
├── assets/
|   ├── css/
|   ├── js/
|   └── media/
└── templates/
    ├── layouts/
    ├── pages/
    ├── components/
    ├── tags/
    └── includes/
```

## Directory Structure Overview

None of the files listed above are required. Orchid will work just fine if you choose to omit any (or all) of them. 

The following is a high-level overview of the main directories and important files you'll need to know to customize your
site.

### config.yml

The `config.yml` file is the main configuration for everything. In here you can configure plugins, themes, and certain
core Orchid functionality. It doesn't have to be YAML either, can be set up in any format you choose, such as TOML or 
JSON. You can break up large `config.yml` files by moving parts of it into individual files in `config/`. Learn more 
about this here. 

### data.yml

These `data.yml` and `data/` files will be loaded in exactly the same way as `config.yml`, and then made available in 
your templates through the `data` variable. 

### homepage.md

This file is the landing page for your entire site. If it is not present, Orchid will create a basic landing page for 
you.

### assets/

Put all the CSS, JS, images, or other assets you need here. Typically, CSS and SCSS files go in `assets/css` and JS 
files go in `assets/js`, and these will only be coped to the built site if you actually use them. All files in 
`assets/media` are copied over by default, but you can set additional directories to always copy over if you need.

### templates/

Unlike many static site generators, Orchid distributes its themes as plugins which fully encapsulate their templates and
assets. This means you do not need to copy a bunch of files into your project to use a theme. However, you may want to
customize of the default templates. Any templates in the `templates/` directory will override those defined by the theme
or other plugins.

### Content

Content in Orchid comes from plugins, such as {{anchor('orchid-pages-feature')}}, {{anchor('orchid-posts-feature')}}, or 
{{anchor('orchid-wiki-feature')}}. Each plugin is free to use whatever directory structure it wants, so make sure to check out
the documentation for your plugins to know where they get their content from.
