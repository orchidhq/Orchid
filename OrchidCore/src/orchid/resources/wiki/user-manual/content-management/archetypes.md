---
description: 'Archetypes are a unique way to manage page configurations as your site grows larger, customizing many pages from one central location.'
---

Most pages have configurations that are intended to be the same across all pages of that type. Front Matter allows you 
to customize the data for that single page, and some other Static Site Generators offer capabilities for generating new
pages with that common format already set up, but ultimately they offer no guarantee that _all_ pages of a certain type
_always_ share the same options. This is where Orchid's Archetypes come in handy, and really begin to show the power and 
flexibility that Orchid offers with its options management. 

Archetypes inject data from a common source into each Page matching that Archetype, with that common source typically 
being your `config.yml`. These additional options are then merged with the page's Front Matter options (with options in
the Front Matter taking precedence over the Archetypal options). Certain data types may even have multiple Archetypes, 
giving you hierarchical control over your page configuration.

{% alert 'info' :: compileAs('md') %}
NOTE: Archetypes are completely configurable in code, and aren't just limited to Pages. Some plugins may include data
types which include their own Archetypes, and may even set up methods for pulling Archetypal data from sources other
than your `config.yml`. See your plugin's documentation to find out more, and consult the Developers Guide to learn 
about configuring your own Archetypes.
{% endalert %}

As an example, I have the following snippet in this site's `config.yml`:

```yaml
allPages:
  layout: single

wiki:
  wikiPages:
    layout: geopattern2
```

The resulting layout for this page is `{{layout}}`.

This page was created by the Wiki plugin, which decided that there exists a `wikiPages` archetype for all Wiki pages. So
I can set the layout for _every_ page in the Wiki from a single location in `config.yml`, instead of having to copy the
`layout` option into every Wiki source page. But the page also belongs to the `allPages` Archetype, and will inherit 
those properties as well. Archetypes are hierarchical, and in this case the `wikiPages` Archetype will take precedence 
over `allPages` in the case that both define the same property.
 
And best of all, since all pages read from one location, it makes it trivial to change the layout across the theme, 
since I only have to change it once to see it reflected everywhere. The same is true for configuring page menus, 
components, draft status, or any other option.

It is worth noting that there is fundamentally no difference with whether the data came from Front Matter or from the 
Archetype. When options are loaded into the Page, it is completely transparent from which source it came, so you can 
simply work with the data without worrying that you've missed the Archetypes. 
