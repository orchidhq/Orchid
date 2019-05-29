---
description: 'Shared Configurations give you the freedom to define your own Archetypes and apply them selectively.'
---

{{ anchor('Archetypes') }} are really powerful and provide a clean, opinionated way to structure the common 
configuration for many pages of your site, but you will likely have certain pages that are related but which do not 
share any common Archetypes. Shared Configuration is a special Archetype that gives you the freedom to define your own
option groups.

Let's start with an example. Say you have a site using Static Page Groups, and want to add an additional menu item to 
all the "index" pages for each group.

```text
. / (resources root)
├── homepage.md
├── config.yml
└── pages/
    ├── group-one/
    |   ├── index.md <-- group-one index page
    |   ├── 1-1.md
    |   └── 1-2.md
    ├── group-two/
    |   ├── index.md <-- group-two index page
    |   ├── 2-1.md
    |   └── 2-2.md
    └── group-three/
        ├── index.md <-- group-three index page
        ├── 3-1.md
        └── 3-2.md
```

For the directory structure above, you can see each of the 3 page groups has a similar structure, and each one includes
a file named `index.md`, which we want to link to each of the other pages within the group. But the Static Pages plugin 
does not provide an Archetype that selects these pages only. The normal solution is to simply copy the configuration you
need to each of those pages, but that can get very difficult to keep in sync amongst all pages over time. 

Instead of copying this configuration to each page, we can just add the `from` property to each page's Front Matter, 
which points to a location within our site config, and now we only need to update the one config to change all pages. 
Likewise, adding new pages requires adding only a single line to these new pages.

```markdown
// group-one/index.md
---
from: staticPages.groupIndex
---

...
```

```markdown
// group-two/index.md
---
from: staticPages.groupIndex
---

...
```

```markdown
// group-three/index.md
---
from: staticPages.groupIndex
---

...
```

All of these pages now include the configurations from `staticPages.groupIndex`, and we can add the configuration we 
want to share to that location in `config.yml`.

```yaml
# config.yml
staticPages:
  groupIndex: # <-- `from: staticPages.groupIndex` points here
    menu:
      - type: "pageChildren"
```

Now, with that set up in `config.yml`, the pages that include data `from: staticPages.groupIndex` will include the 
`pageChildren` menu item, in addition to the menu items defined in that page's own Front Matter and its other relevant
Archetypes.

Shared Configurations take precedence over normal Archetype data, such as `allPages`.

## Multiple Shared Configs

You can also set up more than one SharedConfiguration for a single page by passing a list of pointers rather than single
one. If you pass multiple pointers to different shared config locations, the data from pointers later in the list take
precedence over those defined earlier.

```markdown
// group-one/index.md
---
from: 
  - staticPages.groupIndex
  - staticPages.specialIndex # <-- options from here take precedence over those in `staticPages.groupIndex`
---

...
```
