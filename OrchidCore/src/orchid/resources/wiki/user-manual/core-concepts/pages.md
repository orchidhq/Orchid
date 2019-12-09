---
description: 'Pages are the most basic unit of data in Orchid, representing one file in your final site. Get to know the basic parts of an Orchid page and how other Orchid components relate.'
---

## Overview 

Pages are the most basic unit of data in Orchid. Conceptually, a Page represents one file in your final site, whether 
its your homepage, a blog post, CSS or JS assets, images, or anything else.

Orchid uses a plugin system to index your _pages_ from a variety of different sources, such as Markdown files or code
comments, and then renders them to file embedded in a _theme_. The theme inserts the page content into a _layout_, 
usually with one or more _menus_. 

The _page content_ is composed of a list of _components_, with one of these typically embedding the Markdown file 
contents inside a _page template_. Pages may also have their own unique menus, which usually holds menu items related to 
that specific page or its related pages.

![Admin panel]({{ 'assets/media/page-structure.png'|asset }})

## Drafts

Drafts are pages that are not yet ready to be published or were intended to be accessible only for a limited time and
are now expired. Pages that are drafts will not be rendered or be discoverable by other plugins from the index. 

Any page can become a draft, and there are multiple ways to make a draft from any given page. The easiest way is just to
set the page as a draft in its Front Matter:

```yaml

---
...
draft: true
---
```


You can also set a `publishDate` or an `expiryDate` in the pages Front Matter, which both take a date in the ISO-8601 
date format (`YYYY-MM-DD`) or datetime format (`YYYY-MM-DDThh:mm:ss`):

```yaml

---
...
publishDate: '2018-03-01' # considered a draft until March 1st, 2018
expiryDate: '2018-04-01' # considered a draft after April 1st, 2018
---
```

Some plugins may set the page's publish or expiry date themselves based on some external criteria. An example is the 
Posts plugin, where posts include their publish date in the filename, instead of finding it in the post's Front Matter.

In some situations, you may wish to view your drafts during development. To do this, simply set the `includeDrafts` 
option to `true` on the `render` service in your `config.yml`

```yaml
...
services:
  render:
    includeDrafts: true
```
