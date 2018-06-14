---
official: true
description: Generate a living styleguide from annotated CSS, Sass, Scss, or LESS
images:
  - src: http://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974377/plugins/styleguide.jpg
    alt: KSS Styleguide
    caption: Photo by Jan Losert on Unsplash
---

### Using Orchid KSS

[KSS](http://warpspire.com/kss/syntax/) is a methodology for documenting your CSS and generating a styleguide which
shows example usage of your stylesheets. This plugin implements a basic KSS parser and renders KSS blocks as Orchid 
pages. KSS are block comments placed within your stylesheets, an example is given below:

{% highlight 'css' %}
/*
A button suitable for giving stars to someone.

:hover             - Subtle hover highlight.
.stars-given       - A highlight indicating you’ve already given a star.
.stars-given:hover - Subtle hover highlight on top of stars-given styling.
.disabled          - Dims the button to indicate it cannot be used.

markup:
<li>
  <a class="-modifierClass">Stars</a>
</li>

wrapper:
<ul>
  -markup
</ul>

Styleguide 2.1.3.
*/
a.button.star {
  
}
a.button.star.star-given {
  
}
a.button.star.disabled {
  
}
{% endhighlight %}

These blocks are extracted from your stylesheets, and a hierarchy is built from their Styleguide References 
(`Styleguide 2.1.3.` in the block above). Each section of the Styleguide gets its own page, which documents that section
and also all the sections of its immediate children. 

The comment block contains several types of information that are inportant: the `name`, a `description`, `modifiers`, 
and their descriptions, `tags` and their values, `markup` and `wrapper` templates, and a `styleguide reference`. 

#### Styleguide Name

The first line in a KSS block should be the name of that styleguide section. 

#### Styleguide Description

Any remaining lines in a KSS block that are not modifiers, tags, or the stylguide section are part of the section 
description. They do not need to be consecutive. Each Section becomes a Page, and the description becomes the "intrinsic 
content" of the page.

#### Modifiers

Modifiers are the specific CSS classes that give different styling to an element. Modifiers are a CSS selector on a line
by itself, with a short description which is separated from the selector by a `-`. 

#### Tags

Tags are arbitrary data that can be used to configure the Section. Each Section becomes a Page, and tags become the 
"Front Matter" of the page. Tags are a key on a line by itself ending with a `:`, everything that follows is the value.

#### Markup

Markup is just a Tag with a key of `markup`, whose value is HTML markup used to demonstrate usage of the element. This
markup is repeated in the Styleguide Section Page for each Modifier, and a placeholder of `-modifierClass` within the 
markup is replaced with the modifier class.

#### Markup Wrappers

A Markup Wrapper extends the markup model for each child stylguide section. When creating the markup for a given 
modifier, that section's `markup` is injected into its `wrapper` at the `-markup` placeholder. In addition, if the 
section has a parent section, this markup is then injected into the parent section's markup at _its_ `-markup` 
placeholder, and this is done recursively for all parent sections. This helps to define logical hierarchies of markup
within your styleguide. 

#### Styleguide Reference

Every KSS block describes a new section within your styleguide. These sections are intended to be described in a 
hierarchy, and the Styleguide Reference is what builds that hierarchy. The sections are separated by `.` in the 
reference, and this is then split up and built into the full sections hierarchy. These sections are commonly used just 
as numbers to maintain a strict ordering, but they can be just normal names separated by `.` as well, such as 
`Styleguide UI.Buttons.Variants.` for more logical groupings.