---
title: 'Gaining Traction'
description: January 2020 Orchid News
tags: 
  - Orchid News
---

It's so incredible to see this project grow from a tiny seedling to the maturing sprout it is now. Especially since the
0.18.0 release, I've seen more people try out Orchid and adopt it for their documentation sites, and I'm so incredibly
grateful for each and every download. But this is only the beginning, Orchid still has lots of growing-up to do, so why
don't you come along for the ride and help shape the future of Orchid!

{% snippet 'newsPostIntro' %}

## On Github

Orchid has been growing so much since December's 0.18.0 release! Its now at 313 stars and version 0.18.2, with more 
downloads, new sites, and issues created than ever before! And it's all thanks to this wonderful community of 
individuals believing in a better way to do documentation, so thank you all!

The holidays were fairly quiet in terms of new releases and contributions, but [Tom Beadman](https://github.com/tomb50)
helped immensely with improving the asciidoctor integration. A small PR with huge impact for improving the 
quality-of-life for Asciidoctor fans, which now includes support for `include::[]` macros! 

I also merged work [Danilo Pianini](https://github.com/DanySK) had done for improving the YouTube tag to support 
aspect-ratios instead of fixed sized.

## What's New?

Now that 0.18.0 is released, I've been able to work on some smaller features I've been wanting to support for a while. I
had to restrain myself from working on them for 0.18.0 to prevent feature-creep, but it's been nice finally getting to
add the following features:

- The {{ anchor('OrchidBible') }} plugin has been broken for some time, as the APIs driving it had been decommissioned. 
    I have removed the previously-broken `bible()` function, which pre-rendered Bible verses, and replaced it with a new
    [Faithlife Reftagger](https://faithlife.com/products/reftagger) meta-component, which will automatically create 
    popups for all Bible verses it finds on the page!
- Support for [Mermaid JS](https://mermaid-js.github.io/mermaid) markup has been added to the 
    {{ anchor('OrchidDiagrams') }} plugin. As Mermaid is a javascript library, support is added through a meta-component
    instead of using pre-rendered markup like PlantUML.
- The `youtube` template tag in {{ anchor('OrchidWritersBlocks') }} can now display videos of a given aspect-ratio, 
    making them better-suited for responsive designs.
- {{ anchor('OrchidChangelog') }} now supports single-file changelogs, such as the 
    [Keep A Changelog format](https://keepachangelog.com/en/1.0.0/) format.
    
# In Progress

Supporting include macros for Asciidoctor was a huge step forward, but there's still work to be done to improve that 
integration, and you can expect more Asciidoctor improvements in the coming weeks, with `image::[]` being the next 
target.

In addition to a few more quality-of-life improvements planned for the near-future, I've begun work on a new 
`OrchidSnippets` plugin! More details will come in a future post, but I'd love to get your feedback and suggestions for
its implementation and usage in [this issue](https://github.com/orchidhq/Orchid/issues/293) on GitHub! 

---

{% snippet 'newsPostFooter' %}
