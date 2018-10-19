---
official: true
description: Embed slide presentations in your site using Deck.js
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974588/plugins/presentations.jpg
    alt: Presentations
    caption: Photo by rawpixel.com on Unsplash
menu:
  - type: 'page'
    itemId: 'Orchid Presentations'
  - type: 'pageChildren'
    itemId: 'Orchid Presentations'
    asSubmenu: true
    submenuTitle: Docs
---

### Using Orchid Presentations

Presentations consist of a deck of slides in a subdirectory of `presentations/`. Each presentation should be a separate
directory in `presentations/`, and the content files within that presentation directory are the individual slides in the 
presentation deck.

These presentations can then be displayed in your Orchid site as a `presentation` component, which has a `presentation`
property that references the name of one of your presentations (the name of the directory that contains slides). The 
`presentation` component adds the CSS and Javascript necessary to use Deck.js to display the content of your slides.

The content files that make up the presentation are ordered according to their order on disk, and the filename becomes
the unique ID added to the slide so that Deck.js is able to move between the slides. If the filenames are not ordered
as you need them, you may manually set the ordering of slides by prefixing the filename with a number that is the slide
order, such as `01-slide-one.md` and `02-slide-two.md`, which is stripped away from the slide ID.

Note that a limitation of Deck.js is that only one presentation can be used on a page.