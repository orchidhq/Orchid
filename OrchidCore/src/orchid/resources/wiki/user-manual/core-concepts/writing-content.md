---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
{% endblock %}

{% block sectionBody %}

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
- `.ad -> .html`: AsciiDoc (requires {{anchor('Orchid Asciidoc')}} plugin)
- `.uml -> .svg`: PlantUML (requires {{anchor('Orchid Diagrams')}} plugin)

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

{% highlight 'yaml' %}
# config.yml
...
services:
  compilers:
    ignoredOutputExtensions:
      - 'min'
      - 'debug'
{% endhighlight %}

{% endblock %}