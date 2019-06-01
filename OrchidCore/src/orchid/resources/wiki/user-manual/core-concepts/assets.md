---
description: 'Orchid accumulates assets from multiple sources before they are added to the page, including themes, plugins, components, and Front Matter.'
---

## Asset Sources

Orchid accumulates assets from multiple sources before they are added to the page as `<script>` and `<style>` tags. In
particular, assets can be contributed to a page from:

- the page itself: a plugin may add assets to its own pages to ensure proper styling or functionality of that page's 
    content
- your selected theme: the theme's own styles and scripts are loaded and rendered once, but attached to all pages using
    that theme
- components: all components, whether they are the page's components or as widget areas of the theme, are attached to 
    the page and can add assets to that page.
    
## Additional Assets

On anything that adds assets to the Page, you can also define additional assets to be added alongside them. This is 
added as the `extraCss` or `extraJs` properties of the theme config in `config.yml`, a page's Front Matter, or in a 
component's config.

These assets take an arbitrary path to a resource, and will automatically compile it according to its file extension and
render it in the final site for you. 

**Extra Assets attached to a Theme**
```yaml
# config.yml
theme:
  extraCss:
    - 'assets/css/custom.scss'
  extraJs:
    - 'assets/js/custom.js'
```

**Extra Assets attached to a Page**
```yaml
# pages/page-one.md
extraCss:
- 'assets/css/custom.scss'
extraJs:
- 'assets/js/custom.js'
```

**Extra Assets attached to a Component in an Archetype**
```yaml
# config.yml
allPages:
  components:
    - type: 'pageContent'
      extraCss:
        - 'assets/css/custom.scss'
      extraJs:
        - 'assets/js/custom.js'
```

## Media files

For assets that are not attached to a page, such as images or downloadable files, you must tell Orchid which folders you
want copied over. By default, any file `assets/media/` will be copied over directly if it is a binary file format (such 
as image files or PDFs), or compiled if it is a known file type (such as SCSS). You can configure additional directories
in your `config.yml`.  

```yaml
assets:
  sourceDirs: 
    - 'assets/media'
    - 'assets/overrides/css'
    - 'assets/overrides/js'
```

If a file is not being copied over correctly, you may need to tell Orchid that it should be treated as a binary file 
stream instead of a character stream. You can set in your `config.yml` which file extensions should be treated as binary
content. The following file extensions are considered binary by default:  `jpg`, `jpeg`, `png`, `pdf`, `gif`, `svg`, 
`otf`, `eot`, `ttf`, `woff`, `woff2`

```yaml
services:
  compilers: 
    binaryExtensions: 
      - 'jpeg'
```
