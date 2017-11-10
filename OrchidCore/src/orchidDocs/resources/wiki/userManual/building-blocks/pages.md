---
---

Pages are the most fundamental unit of content in an Orchid site. A Page represents a single HTML file in the output of 
your site, and the rest of Orchid revolves around the Page. The following outlines the key players in an Orchid site
and how they all interact:

- Orchid asks Generators to know which Pages they intend to produce.
- Orchid adds these Pages to a single Index.
- Orchid passes these pages back to their respective Generators to be rendered.
- During rendering, a Page asks the Theme how the final output should look. This involves the creation and display of 
Menus and Components. Pages, Themes, Generators, or anything else can request Menus or Components be displayed.
- Menus use the Index to decide which items end up in the menu, which is displayed within the Page.
- Components are small sections of the Page which are relatively isolated in scope, but may request data that has been 
Indexed, either as a requirement or for simplified usage. 

As an object in Orchid, the page is quite small, with not much to say about it. Conceptually, however, the Page is the 
largest and most important player in Orchid, and the following pages in this section of the Wiki will go through each of
the remaining players one-by-one, to show how they all interact. 