---
---

Assets are the CSS, Javascript, and images needed for your site. By default, only the assets declared by your themes, 
pages, or components are compiled into the final site. But there are several ways to add additional assets needed for 
specific circumstances, outlined below:

## Method One: Asset Directories

If you wish to copy images, just give the Assets Generator the paths you want copied over. This is done with the 
following snippet in your `config.yml`:

```yaml
assets:
  sourceDirs: 
    - 'assets/css'
    - 'assets/js'
```
{.line-numbers}

The `sourceDirs` key of the `assets` generator configuration block defines a list of directories to copy assets from 
into the output site. These directories are copyied recursively, and the assets are compiled against their file 
extension and given the same path as they exist on disk. 

This is useful for things like adding your own images and being able to host and reference them from the Orchid site. 
Images and other binary file types are copied directly without any modification, but any files that can be compiled will
be compiled before being copied to the output. Many binary file types are supported by default, but if you find a binary
asset is being corrupted in the process, you can add its extension to the following configuration snippet in 
`config.yml`:

```yaml
services:
  compilers: 
    binaryExtensions: 
      - 'jpeg'
```
{.line-numbers}

`binaryExtensions` should be an array with each item being the file extension which should be recognized as binary.

## Method Two: Extra CSS and JS

While Method One is best for adding things like images or other assets that may be referenced on any given page, it is
quite often the case that we want custom CSS or Javascript to be included on specific pages only. To accomplish this, 
you can add "extra CSS" and "extra JS" to any Page or Component definition. Any extra files added in this way will be 
looked up, compiled, and copied to the output site, in addition to being injected into the rendered page. 

For the following snippet can be included in a Page's front matter or within a component configuration, and will have 
the same effect, which is to find and compile the referenced assets and add it the containing page:

```yaml
extraCss:
  - 'assets/css/custom.scss'
extraJs:
  - 'assets/js/custom.js'
```
{.line-numbers}

Extra CSS and JS may also be added to the Theme configuration, with the effect of being added to all pages using that 
theme.