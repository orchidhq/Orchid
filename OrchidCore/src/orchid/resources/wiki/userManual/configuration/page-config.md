---
customItems:
    - 'Item One'
    - 'Item Two'
    - 'Item Three'
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
Pages in Orchid are configured in a similar way to most other Static Site Generators, using Front Matter. Front Matter
is typically as a block of YAML between pairs of triple-dashed lines, but other configuration formats are also supported 
(described below). Orchid also extends page configuration with Archetypes, which allow common configurations for pages 
to be shared and controlled from a centralized location.
{% endblock %}

{% block sectionBody %}
## Front Matter
***

Front Matter allows you to customize your Pages individually, adding components or menus to just that page, changing the
layout, or using configurations specific to the plugin it came from. The Front Matter is optional, but if used, it must
be the first thing in the file. Front Matter can use any of Orchid's parser languages, but YAML is the most common, and 
comes between pairs of triple dashed lines, which is removed from the actual page output. Here's an example, which is 
the actual Front Matter of this current page:

```yaml
---
title: Page Config
customItems:
    - 'Item One'
    - 'Item Two'
    - 'Item Three'
---
```

Front Matter can use different languages in two ways: by specifying the language's extension after the first set of 
dashes, or by using "fences" for that languages. As an example, TOML uses `+++` instead of `---`, and JSON uses `;;;`. 
The following blocks of TOML front Matter are equivalent to the YAML Front Matter block above:

```toml
---toml
title = "Page Config"
customItems = [
  "Item One",
  "Item Two",
  "Item Three"
]
---
```

```toml
+++
title = "Page Config"
customItems = [
  "Item One",
  "Item Two",
  "Item Three"
]
+++
```

The data defined in Front Matter is then included into the page, and can be accessed as template variables anywhere on 
the page, including components, layouts, included templates, or pre-rendered into the page content. As an example, the 
following are all rendered dynamically based on this page's Front Matter, shown above:

**title**: `{{page.title}}`

**customItems**:
{% for item in customItems %}
* `{{item}}`
{% endfor %}

## Archetypes
*** 

Most pages have configurations that are intended to be the same across all pages of that type. Front Matter allows you 
to customize the data for that single page, and some other Static Site Generators offer capabilities for generating new
pages with that common format already set up, but ultimately they offer no guarantee that ALL pages of a certain type
ALL share the same options. This is where Orchid's Archetypes come in handy, and really begin to show the power and 
flexibility that Orchid offers with its options management. 

Archetypes inject data from a common source into each Page matching that Archetype, that common source typically being
your `config.yml`. These additional options are then merged with the page's Front Matter options (with options in the
Front Matter taking precedence over the Archetypal options). Certain data types may even have multiple Archetypes, 
giving you hierarchical control over your page configuration.

> NOTE: Archetypes are completely configurable in code, and aren't just limited to Pages. Some plugins may include data
> types which include their own Archetypes, and may even set up methods for pulling Archetypal data from sources other
> than your `config.yml`. See your plugin's documentation to find out more, and consult the Developers Guide to learn 
> about configuring your own Archetypes.

{.alert .alert-info}

As an example, I have the following snippet in this site's `config.yml`:

```yaml

allPages:
  layout: single

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
since I only have to change it once to see it reflected everywhere.

It is worth noting that there is fundamentally no difference with whether the data came from Front Matter or from the 
Archetype. When options are loaded into the Page, it is completely transparent from which source it came, so you can 
simply work with the data without worrying that you've missed the Archetypes. 

{% endblock %}