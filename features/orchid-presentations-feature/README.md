---
description: Embed slide presentations in your site using Deck.js
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974588/plugins/presentations.jpg
    alt: Presentations
    caption: Photo by rawpixel.com on Unsplash
tags:
    - components
---

## About

Create simple presentations in the browser using [Deck.js](http://imakewebthings.com/deck.js/) and all of Orchid's 
simple content management. 

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Run [PresentationsGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidPresentations/src/test/kotlin/com/eden/orchid/presentations/PresentationsGeneratorTest.kt) for demo

## Usage

### Basic Usage

Presentations consist of a deck of slides in a subdirectory of `presentations/`. Each presentation should be a separate
directory in `presentations/`, and the content files within that presentation directory are the individual slides in the 
presentation deck.

```text
. / (resources root)
├── homepage.md
├── config.yml
└── presentations/
    └── demo1/ <-- presentation key is "demo1"
        ├── slide-1.md
        ├── slide-2.md
        └── slide-3.md
```

These presentations can then be displayed in your Orchid site as a `presentation` component, which has a `presentation`
property that references the name of one of your presentations (the name of the directory that contains slides). The 
`presentation` component adds the CSS and Javascript necessary to use Deck.js to display the content of your slides. 
Note that Deck.js requires JQuery to work. If you're using a theme that doesn't already have JQuery added, you'll need
to add it yourself. 

```markdown
// pages/presentation.md
---
components:
  - type: 'presentation'
    presentation: 'demo1' <-- uses presentation slides from `presentations/demo1/` directory
---
```

Note that a limitation of Deck.js is that only one presentation can be used on a page at a time, so only a single
`presentation` should be added to each page.

### Slide Ordering 

The content files that make up the presentation are ordered according to their order on disk, and the filename becomes
the unique ID added to the slide so that Deck.js is able to move between the slides. If the filenames are not ordered
as you need them, you may manually set the ordering of slides by prefixing the filename with a number that is the slide
order, such as `01-slide-one.md` and `02-slide-two.md`, which is stripped away from the slide ID.
