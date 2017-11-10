---
---

Components are single, logical blocks of content within a Page. They typically represent the main content area of a 
Page, with many components being rendered sequentially into that one area. In fact, the Page Content you are currently 
reading is not a static part of the page layout, but is just a Component which renders the page content.

Components are also commonly used to implement sidebars, footers, and other "widget areas" within a layout, and as such
can be defined by the theme. There is no difference in whether a Component is attached to a Page or a Theme.

**A Word about Page Content**

Most pages include a Component that represents the intrinsic content of that Page. For example, a Blog post comes from a
single source file and produces a single Page whose "content" is simply the text content of that file. This component is 
added to Pages by default, unless the Generator or Page says otherwise.

When rendering Page Content, it is common for the pages produced by different Generators to have different requirements
in rendering. For example, a Page produces by the OrchidPages may want to show a list of tags and its author, while a
Page produced by the Wiki may want to highlight the Wiki section currently being read. To support this, Page Content
components load a "page template" in a similar way to how a page Layout template is chosen, but is just for that one, 
logical block of content within a page, rather than the entire page. 

This even allows plugins to create simple, yet semantic, HTML page templates that look good in any theme, but that 
Themes can then override to make much more custom or bring more fully in-line with its own styling. These page templates
then always look the same for a given page type, regardless of the layout chosen, which aids in maintainability of a 
theme.  
