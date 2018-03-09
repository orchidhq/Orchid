---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
{% endblock %}

{% block sectionBody %}
Most content in Orchid comes from a file in your local filesystem. While each plugin is free to do whatever they want to
generate their pages, it is common for plugins to base their internal content models around the filesystem, and as such, 
the content from most plugins is written in a very similar way because the content for each entry works the same.

Page content in Orchid usually consists of two major sections: Front Matter, and Page Content.

## Front Matter

The full details of a page's Front Matter are discussed in the [Page Content]({{ link('Page Configuration', 'wiki') }}) 
page, but in essence, Front Matter is a block of YAML between triple-dashed lines at the top of the file. The Front
Matter can provide a lot of useful information to Orchid, such as:
 
- The page's title
- Whether the page is a draft
- and much, much more
- The pages's components and menus
- Whether to precompile the content or not
- extra variables to make available to the precompiler and templates
- and much, much more 
 
## Page Content

The content that can be used for a page in Orchid is very flexible. Generally speaking, Orchid does not make a 
distinction between 'template' languages (like Pebble) or 'content' languages (like Markdown), and as a result, the 
content for any of these pages can be written in any language that Orchid can compile, whether it's Markdown, Pebble, 
Asciidoc, or raw HTML, giving you complete control and incredible flexibility when creating your pages.

Orchid further extends the powerful content compiling by first "precompiling" your content, typically with Pebble. While
languages like Markdown are pretty limited, Orchid's precompiler allows logic and structure to be added to your content, 
which makes it easier to connect your content to the rest of your site, and also improves the overall consistency of the
content while making it significantly easier to keep everything up-to-date.

### Compiling

The language which is used to compile your content is determined the by the file's extension. For example, a file in the
named `contact.md` will be treated as a Markdown file. Likewise, you could call it `contact.ad` to compile it as 
Asciidoc (if you have the plugin installed) or `contact.peb` to treat it as HTML with Pebble templates inside. 

The file will then be rendered as `contact/index.html`, while links to it will look like `{baseUrl}/contact`. This is 
the 'pretty' form of the URL, and all links automatically include the base URL so you never need to worry about making
sure it is set in your templates or links. 

The output extension for the rendered file is typically determined by the compiler. Most compilers output HTML, and so 
result in a file with the `.html` extension. However, some compilers, such as the Sass compiler, convert the `scss` and 
`sass` input extensions to CSS with the `.css` output file extension.

The file itself is able to override the default output extension by having a filename in a format like 
`filename.output.input`, such as `contact.php.peb`. This tells Orchid to use the `.php` extension instead of the normal
`.html` extension from the Pebble compiler. The result is `contact/index.php`.

But there are some filenames which adhere to the above format that should not be used as the output extension. This is 
common for compiled and minified CSS or Javascript, which is often named something like `styles.min.css`. While this 
format should generally be avoided, it may be unfeasible to rename all your assets, and so you can set certain 
extensions to be ignored when the filename is in that format. By default, `min` is already ignored, but you may add your
own ignored extensions as an array in the `ignoredOutputExtensions` option of the `compiler` service:

{% highlight 'yaml' %}
# config.yml
...
services:
  compilers:
    ignoredOutputExtensions:
      - 'min'
      - 'debug'
{% endhighlight %}


### Precompiling

Any file that has Front Matter will be "precompiled" before the main compiler for that file gets run. This allows 
additional features and dynamic variables to be added to languages that normally do not support it. By default, Orchid
uses Pebble as the precompiler language, and gives you the full power to use all Pebble tags and functions in your 
content. You can even `extend` a base content file and override its `blocks` to improve the consistency of your page
structure throughout your site. 

In the case that you want to use Front Matter but do not want your content to be precompiled (perhaps something in the
content causes an error in the precompiler language), you can set `precompile` to `false` in the Front Matter:

{% highlight 'yaml' %}
---
# Front Matter
...
precompile: false
---
{% endhighlight %}


Alternatively, you may choose to use another language as your precompiler language of choice. This may be done 
individually by a single page by setting `precompileAs` to the desired language extension in its Front Matter, or by
setting the `defaultPrecompilerExtension` option in the `compiler` service your `config.yml` for all pages:

{% highlight 'yaml' %}
---
# Front Matter
...
precompileAs: 'html'
---
{% endhighlight %}


{% highlight 'yaml' %}
# config.yml
...
services:
  compilers:
    defaultPrecompilerExtension: 'html'
{% endhighlight %}


## Other

While the examples here all assume the content comes from a file on disk, the nature of Orchid's architecture allows 
content to come from other sources as well. For example, the Javadoc plugin considers the Javadoc block on the Class to
be the page content, and the block-level tags are the page's configuration. In this situation, there is no "Front 
Matter" in the sense of a block of YAML at the start of the content, but the content and configuration work in the exact
same way. 
{% endblock %}