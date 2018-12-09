---
description: 'Pages are the most basic unit of data in Orchid, representing one file in your final site. Get to know the basic parts of an Orchid page and how other Orchid components relate.'
---

Pages are the most basic unit of data in Orchid. Conceptually, a Page represents one file in your final site, whether 
its your homepage, a blog post, CSS or JS assets, images, or anything else.

Orchid uses a plugin system to index your _pages_ from a variety of different sources, such as Markdown files or code
comments, and then renders them to file embedded in a _theme_. The theme inserts the page content into a _layout_, 
usually with one or more _menus_. 

The _page content_ is composed of a list of _components_, with one of these typically embedding the Markdown file 
contents inside a _page template_. Pages may also have their own unique menus, which usually holds menu items related to 
that specific page or its related pages.

![Admin panel]({{site.baseUrl}}/assets/media/page-structure.png)
