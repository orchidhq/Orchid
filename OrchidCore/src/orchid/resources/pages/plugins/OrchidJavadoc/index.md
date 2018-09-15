---
official: true
description: Where Orchid began. Create beautiful Javadocs for your project within your Orchid site.
images:
  - src: http://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974952/plugins/javadoc.jpg
    alt: Javadoc
    caption: Photo by Brooke Lark on Unsplash
---

### Using Orchid with Javadoc

The Orchid Javadoc plugin integrates with the Javadoc tool to embed class and package info directly in your Orchid site.
Orchid will even compile your Javadoc comments as Markdown!

### Configuring Javadoc pages

Since pages for Javadoc classes and packages don't have a file on disk to write content or add Front Matter to, Orchid
considers the Javadoc comment defined on the `class` or in the `package-info.java` files to be the pages "intrinsic 
content". The text of the comment is compiled as Markdown and rendered in the page, and the block-level Javadoc comment
tags are used as the "Front Matter" configuration. While this is not as powerful as traditional Front Matter (because 
comment tags cannot be assigned as nested maps), it does offer a good solution for simple configuration. Alternatively, 
you may use Archetypes to configure all Class pages or all Package pages at once.
