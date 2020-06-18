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
    
## Media files

For assets that are not attached to a page, such as images or downloadable files, you must tell Orchid which folders you
want copied over. By default, any file `assets/media/` will be copied over directly if it is a binary file format (such 
as image files or PDFs), or compiled if it is a known file type (such as SCSS). You can configure additional directories
in your `config.yml`.  

```yaml
# config.yml
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
# config.yml
services:
  compilers: 
    binaryExtensions: 
      - 'jpeg'
```

## Favicons

Orchid takes care of rendering favicons for you. If you do not provide one yourself, it will use the default Orchid 
logo. If you have your own favicon you'd like to use, simply drop it in your resources root named `favicon.ico` and 
Orchid will use that one instead.

## Additional Assets

On anything that adds assets to the Page, you can also define additional assets to be added alongside them. This is 
added as the `extraCss` or `extraJs` properties of the theme config in `config.yml`, a page's Front Matter, or in a 
component's config.

These assets take an arbitrary path to a resource, and will automatically compile it according to its file extension and
render it in the final site for you. 

### Extra Assets attached to a Theme

```yaml
# config.yml
theme:
  extraCss:
    - 'assets/css/custom.scss'
  extraJs:
    - 'assets/js/custom.js'
```

### Extra Assets attached to a Page

```yaml
# pages/page-one.md
extraCss:
- 'assets/css/custom.scss'
extraJs:
- 'assets/js/custom.js'
```

### Extra Assets attached to a Component in an Archetype

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

### Configuring Additional Assets

Depending on the use-case for adding additional assets, they may need some additional configuration. This can be done 
directly in the extra asset config:

#### Extra JS Configuration

```yaml
# config.yml
theme:
  extraJs:
    - asset: 'assets/js/custom.js'
      inlined: true
```

{% docs className='com.eden.orchid.api.theme.assets.ExtraJs' enabledTabs=['all_options'] %}

#### Extra CSS Configuration

```yaml
# config.yml
theme:
  extraCss:
    - asset: 'assets/css/custom.scss'
      download: false
```

{% docs className='com.eden.orchid.api.theme.assets.ExtraCss' enabledTabs=['all_options'] %}

## Asset Transformations

Orchid includes basic support for media management, including simple image manipulation. You can use the `asset()` 
template function to load an asset, and Orchid will make sure it ends up in your final site. 

```twig
# Any page or template
{% verbatim %}
{{ 'assets/media/pic01.jpg'|asset }}
{% endverbatim %}
```

![asset]({{ 'assets/media/pic01.jpg'|asset }})

### Rotate

Rotate an image asset. Rotation angle is expressed in degrees.

```twig
# Any page or template
{% verbatim %}
{{ 'assets/media/pic01.jpg'|asset|rotate(90) }}
{% endverbatim %}
```

![rotated asset]({{ 'assets/media/pic01.jpg'|asset|rotate(90) }})

### Scale

Scale an image asset by a constant factor.

```twig
# Any page or template
{% verbatim %}
{{ 'assets/media/pic01.jpg'|asset|scale(0.85) }}
{% endverbatim %}
```

![scaled asset]({{ 'assets/media/pic01.jpg'|asset|scale(0.85) }})

### Resize

Resize an image asset to specific dimensions. By default, image is resized maintaining its aspect ratio, and is reduced 
to the largest image that can fit entirely within the specified dimensions. Use the `mode` parameter to resize the
image to exactly the specified dimensions, or crop it to a specified edge.

```twig
# Any page or template
{% verbatim %}
{{ 'assets/media/pic01.jpg'|asset|resize(800, 600, "exact") }}
{% endverbatim %}
```

![resized asset]({{ 'assets/media/pic01.jpg'|asset|resize(400, 300, "fit") }})
![exact resized asset]({{ 'assets/media/pic01.jpg'|asset|resize(400, 300, "exact") }})
![resized cropped center-left asset]({{ 'assets/media/pic01.jpg'|asset|resize(400, 300, "cl") }})
![resized cropped center asset]({{ 'assets/media/pic01.jpg'|asset|resize(400, 300, "c") }})
![resized cropped center-right asset]({{ 'assets/media/pic01.jpg'|asset|resize(400, 300, "cr") }})

### Rename

As assets are transformed, they automatically get renamed to ensure unique asset files are generated. However, you may
wish to rename them youself, which can be done with the `rename()` filter, which accepts the standard permalink 
formatting string as an argument. If you provide a file extension in the permalink which does not match the original 
resource, it will be reformatted into the target file format if it is a valid image format.

```twig
# Any page or template
{% verbatim %}
{{ 'assets/media/pic01.jpg'|asset|rename("assets/media/hero.png") }}
{% endverbatim %}
```

### Chaining

Multiple transformations may be applied to a single asset. Simply use more than one of the above filters. You can use 
the same filter more than once, and they will be applied in turn from left-to-right. Assets are not rendered until the
end of the entire pipeline; intermediate assets are not created for each filter.

```twig
# Any page or template
{% verbatim %}
{{ 'assets/media/pic01.jpg'|asset|resize(800, 600, "exact")|rotate(45)|rotate(45) }}
{% endverbatim %}
```

![resized asset]({{ 'assets/media/pic01.jpg'|asset|resize(400, 300) }})
![resized asset]({{ 'assets/media/pic01.jpg'|asset|resize(400, 300)|rotate(45) }})
![resized asset]({{ 'assets/media/pic01.jpg'|asset|resize(400, 300)|rotate(45)|rotate(45) }})
