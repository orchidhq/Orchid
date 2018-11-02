---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}

All Orchid sites must have a config file in the root of the resources directory, called `config.yml`, which serves as 
the root of all site options. The entire site can be fully described in this one file, but if you have lots of options
and want to make it easier to manage it all, you may add any options files you want to `config/` (more on this below).
Orchid merges the options in `config/` with those in `config.yml`, which are then made available to all parts of the 
Orchid build.

In addition to `config` and `data` files, Orchid also supports setup via command-line flags. These are used for 
options that must be in place before we can load the `config` and `data` files, such as the directory these files reside
in. 

{% alert 'info' :: compileAs('md') %}
Note: Orchid supports many data languages, including TOML. You may name your config file `config.tml` to parse it as
TOML rather than YAML, or `config.json` to parse as JSON. The same goes for all files in `config/` and `data/`. You may 
also mix-and-match parser types as needed, or even add your own.
{% endalert %}

Orchid parses options before every build when running `watch` or `serve` tasks, and so support rapid changes in 
development and prototyping. In contrast, command-line flags are parsed once, before the main Orchid starts up, and 
cannot be changed during the Orchid runtime. Command-line flags are typically the kind of options that are needed to 
even parse `config.yml` or support other project-wide configuration, such as the input and output directories, or 
setting the default theme.   

## Basic Site Config
***

{% highlight 'yaml' %}
theme: # (1)
  menu: 
    - type: 'readme' 
    - type: 'license' 
wiki: # (2) 
  sections:
    - 'userManual'
    - 'developersGuide'
services: # (3)
  generators:
    disabled:
      - 'javadoc'
      - 'posts'
  
allPages: # (4)
  layout: single
{% endhighlight %}


1) Theme options come from `theme` or from an object at the theme's `key`. When using multiple themes, you may want to 
    use individual theme keys to configure each theme independently, but `theme` is generally easier to quickly try out
    different themes.
2) Generator options come from an object at that Generators's key
3) Services are all scoped under the `services` object, and are used to configure the behavior of the Orchid framework.
4) In addition to the options defined in a page's FrontMatter, you may have a set of shared options that _all_ pages, or
    specific sub-sets of pages should have in common. Each plugin declared these archetypal options in their own way, 
    so consult each plugin's documentation for full site configuration.

{% alert 'warning' :: compileAs('md') %}
NOTE: By convention, Themes' keys are in `PascalCase` while Generators' keys are in `camelCase`. Options passed to 
Services are all contained under the `services` object, each at their key within `services`. This all helps prevent 
collisions between theme, generator, and service configuration options.
{% endalert %}
 
{% alert 'info' :: compileAs('md') %}
NOTE: Theme options may be set at the 'theme' top-level object, or at its own key. If you are using multiple themes 
which are all expected to use the same configuration (a common occurrence), the `theme` property is preferred over the 
theme-specific object. However, if both the theme's key and `theme` are defined, the theme-specific object will take 
precedence. You may also set the options for a theme directly on the Generator using that theme, to completely override 
all theme options for those pages only, so you could use different options for the same theme.
{% endalert %}

## Advanced Site Config
***

For larger and more complex sites, a single `config.yml` file will get messy very quickly. You may break up your 
monolithic `config.yml` into as many smaller files as you need, simply by adding a file in the `data/` directory, whose
filename corresponds to its options key. This process is recursive, and you can further break up _those_ files by 
creating directories within `data/`, and so on. You may also specify a filename _and_ and folder, and the two will be 
merged into one single options object, where the options in the file take precedence over the folder. 

For example, the following YAML configs are equivalent:

**Config in one single config.yml**

{% highlight 'yaml' %}
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
{% endhighlight %}


**Config broken into several files**

{% highlight 'yaml' %}
# config.yml (you could even omit config.yml if desired)
{% endhighlight %}

{% highlight 'yaml' %}
# config/theme.yml
siteName: 'My Site'
{% endhighlight %}

{% highlight 'yaml' %}
# config/theme/components.yml
- type: pageContent
- type: readme
- type: license
{% endhighlight %}

{% highlight 'yaml' %}
# config/theme/menu.yml
- type: page
  itemId: 'About'
- type: link
  title: 'Contact'
  url: '/contact'
{% endhighlight %}


## How these options get used
***

Now that all our options have been loaded, Orchid will start distributing them to any parts of the build that need them. 
The basic process goes like this:

1) Build starts
2) Load all options
3) Pass appropriate options to the Theme in the following order, falling to the next if that object doesn't exist:
    * `config.{theme key}` (in PascalCase)
    * `config.theme` 
    * `config` 
4) Pass appropriate options from the `config.services.{service key}` object to internal services (compiler service, 
    render service, etc.). This is how the behavior of the full Orchid program is configured.
5) Index each generator, passing appropriate options from the `config.{generator key}` (in camelCase)
6) Generate pages each generator. If the Generator specified a theme as one of its options during the Indexing step, 
    switch to that theme, loading _that_ theme's options in the same manner as in step 3, before rendering the pages 
    from that generator. 
 
{% endblock %}