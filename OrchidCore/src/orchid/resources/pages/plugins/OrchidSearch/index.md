---
official: true
description: Add static full-text search with Lunr.js to any Orchid site
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973700/plugins/search.jpg
    alt: Search
    caption: Photo by Louis Blythe on Unsplash
menu:
  - type: 'page'
    itemId: 'Orchid Search'
  - type: 'pageChildren'
    itemId: 'Orchid Search'
    asSubmenu: true
    submenuTitle: Docs
---

### About Orchid Search

Orchid Search allows any Orchid site to have full-text search capabilities, without requiring a backend server or using
any 3rd-party search services! Using the wonderful Lunr.js library, Orchid is able to generate an index of all content 
on your site as static JSON files, and then search it from your browser.

### Using Orchid Search

This plugin adds a small Javascript file, along with Lunr.js, to your build, and from there it is up to the theme to 
actually include these scripts and provide a form to input a search query. All official themes support searching, but
non-official themes may not. 

But the Javascript alone isn't enough to do a search by itself, it needs a way to locate the content that can be 
searched, and so this plugin also adds a generator which generates a bunch of JSON files that contain the full content 
of all of your pages. "Content" in this case is the "intrinsic content" of each page, which is typically the contents of 
the file it comes from. Anything that gets "included" into the content body is also considered part of the page's 
content, but content that comes from other components attached to the page is not.
