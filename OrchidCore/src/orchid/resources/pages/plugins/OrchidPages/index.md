---
official: true
description: Add static pages with rich taxonomy to your Orchid site.
images:
  - src: http://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973072/plugins/pages.jpg
    alt: Pages
    caption: Photo by Ilario Piatti on Unsplash
---

### Using Static Pages

Static pages allow you to add pages to your Orchid site in with ultimate flexibility. Simply drop content files in your 
`pages/` directory, and they will be rendered in your site at the same URL as they reside in that directory. Pages can 
contain any content, use any layout, and have no restriction on their URL path, because it is simply their location on
disk that determines their URL, which all gives you a ton of flexibility with static pages.

You may wish to change how static pages are rendered, for example to be rendered raw and the page to be the entire, 
fully-customized HTML of that page. To do this, you can set the `renderMode` to `raw` in a page's Front Matter to just 
render its contents directly and not use a layout at all. You may also set `usePrettyUrl` to determine whether to use
a "pretty" URL (like /contact) or not (like /contact.html).

### Page Groups

When placing pages into subdirectories, the specific subdirectory they are placed in typically defines a top-level 
categorization for those pages. In Orchid, the first directory a Static Page is placed in is known as its **group**. 
Pages can then be located from a Collection, filtering by their Group. You may also wish to combine page Groups with the
Taxonomies plugin, and generate archives highlighting all the different Groups and the pages in each Group.