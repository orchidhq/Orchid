---
---

An Orchid Wiki a collection of pages structured in a freeform hierarchy. Pages in a wiki are all related and ordered, 
with one page linking directly to the next so a user can read the entire contents of the wiki from front-end simply by 
navigating the page links. Orchid Wikis also create Menu items which display the full wiki tree in an easily navigable
fashion.

{% filter compileAs('md') | alert %}
  The OrchidWiki plugin must be installed in order to use Wikis. It can be included on its own, or added as part of the 
  following bundles:

  * OrchidAll
  * OrchidProduct
{% endfilter %}

Wikis in Orchid are inspired by GitBook and are set up in a similar manner. To start, add a file named `SUMMARY.md` to
`wiki/`. This file becomes the landing page for the wiki and can contain any kind of content, which gets displayed 
exactly as written. But it also gets converted to HTML and all links in this file become a page in the wiki. 

For example: 

{% filter highlightPrism('markdown') %}
### Wiki Heading

[Getting Started](getting-started.md)
[Basic Setup](setup/basic.md)
[Advanced Setup](setup/advanced.md)
{% endfilter %}

corresponds to the following directory structure:

{% filter highlightPrism('text') %}
/ (resources root)
|-- config.yml
|-- /wiki
    |-- SUMMARY.md
    |-- getting-started.md
    |-- /setup
        |-- basic.md
        |-- advanced.md
{% endfilter %}

which produces the following output:

{% filter highlightPrism('text') %}
/ (generated site root)
|-- /wiki
    |
    |-- /summary
    |   |-- index.html
    |
    |-- /getting-started
    |   |-- index.html
    |
    |-- /setup
        |-- /basic
        |   |-- index.html
        |
        |-- /advanced
            |-- index.html
{% endfilter %}

The content of those wiki pages can be anything, and can be written in any language as long as there is a Compiler for 
it (just like any other page). Orchid also creates a new menu item type which links to every page in the wiki and is 
displayed recursively in the same hierarchy as the pages themselves. 

You can also customize the source directory of the wiki, and even set up multiple wiki sections which each have their 
own `SUMMARY.md`, pages, and menu items. The following snippet should go in your site's `config.yml`:

{% filter highlightPrism('yaml', '1,3,4') %}
  baseDir: 'help'
  sections:
    - 'userManual'  
    - 'developerGuide'  
{% endfilter %}

- `1:` Looks for the Wiki in /help instead of /wiki
- `3:` Creates a wiki based on {baseDir}/userManual/SUMMARY.md
- `4:` Creates a wiki based on {baseDir}/developerGuide/SUMMARY.md