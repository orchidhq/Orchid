---
description: 'How to configure Orchid and scale the configuration as your site grows'
---

A config file `config.yml` in the root of the resources directory serves as the root of all site options. The entire 
site can be fully described in this one file, but if you have lots of options and want to make it easier to manage it 
all, you may break it out into individual files in the `config/` directory. Orchid merges the options in `config/` with 
those in `config.yml`, which are then made available to all parts of the Orchid build. Configuration options are loaded
before every build when running in "serve" mode.
 
{% alert 'info' :: compileAs('md') %}
Note: Orchid supports many data languages, including TOML. You may name your config file `config.tml` to parse it as
TOML rather than YAML, or `config.json` to parse as JSON. The same goes for all files in `config/` and `data/`, and 
different files can use different formats as needed.
{% endalert %}

## Basic Site Config
***

```yaml
site:
  baseUrl: 'https://www.example.com/' (1)
  theme: 'Editorial' (2)

theme: # (3)
  menu: 
    - type: 'readme' 
    - type: 'license' 
wiki: # (4) 
  sections:
    - 'userManual'
    - 'developersGuide'
services: # (5)
  generators:
    disabled:
      - 'javadoc'
      - 'posts'
  
allPages: # (6)
  layout: single
```

1) Set the base URL to be prepended to all generated links. Plugins can provide various helpers for generating base 
    URLs, such as the {{ anchor('orchid-netlify-feature') }} plugin looking up the proper base URL for Netlify CI branch preview 
    or production builds.
2) Set the default theme to use for your site.
3) Theme options come from `theme` or from an object at the theme's `key`. When using multiple themes, you may want to 
    use individual theme keys to configure each theme independently, but `theme` is generally easier to quickly try out
    different themes.
4) Generator options come from an object at that plugin's key
5) Services are all scoped under the `services` object, and are used to configure the behavior of the Orchid framework.
6) In addition to the options defined in a page's FrontMatter, you may have a set of shared options that _all_ pages, or
    specific sub-sets of pages should have in common. This is an example of **archetypes**, learn more about them here.

For larger and more complex sites, a single `config.yml` file will get messy very quickly. You may break up your 
monolithic `config.yml` into as many smaller files as you need, simply by adding a file in the `config/` directory, 
whose filename corresponds to its options key. This process is recursive, and you can further break up _those_ files by 
creating directories within `config/`, and so on. You may also specify a filename _and_ and folder, and the two will be 
merged into one single options object, where the options in the file take precedence over the folder. 

For example, the following YAML configs are equivalent:

**Config in one single config.yml**

```yaml
# config.yml
theme:
  siteName: 'My Site'
  components:
    - type: pageContent
    - type: readme
    - type: license
  menu:
    - type: page
      itemId: 'About'
    - type: link
      title: 'Contact'
      url: '/contact'
```


**Config broken into several files**

```yaml
# config.yml (you could even omit config.yml if desired)
```

```yaml
# config/site.yml
baseUrl: 'https://www.example.com/'
theme: 'Editorial'
```

```yaml
# config/theme.yml
siteName: 'My Site'
```

```yaml
# config/theme/components.yml
- type: pageContent
- type: readme
- type: license
```

```yaml
# config/theme/menu.yml
- type: page
  itemId: 'About'
- type: link
  title: 'Contact'
  url: '/contact'
```
