---
components:
  - type: pageContent
  - type: prism
    theme: 'twilight'
    languages:
      - 'yaml'
      - 'java'
      - 'groovy'
      - 'markdown'
---

Orchid provides a powerful, yet simple and intuitive way to configure your site. It expects that large and complex sites
may have lots of options set in a many-layered hierarchy, but does not sacrifice simplicity for power with smaller 
sites. To fully understand all that can be configured with the site config, you should refer to _the Options section of
the developer guide_. A basic description follows.

## The Basics

All Orchid sites must have a config file in the root of the resources directory, called `config.yml`, which serves as 
the root of all site options. The entire site can be fully described in this one file, but if you have lots of options
and want to make it easier to manage it all, you may add any options files you want to `data/` (more on this below).
Orchid merges the options in `data/` with those in `config.yml`, which are then made available to all parts of the 
Orchid build.

In addition to `config` and `data` files, Orchid also supports setup via command-line flags. These are used for 
options that must be in place before we can load the `config` and `data` files, such as the directory these files reside
in. 

> NOTE: Orchid supports many data languages, including TOML. You may name your config file `config.tml` to parse it as
> TOML rather than YAML, or `config.json` to parse as JSON. The same goes for all files in `data/`. You may also 
> mix-and-match parser types as needed, or even add your own.

Orchid parses options before every build when running `watch` or `serve` tasks, and so support rapid changes in 
development and prototyping. In contrast, command-line flags are parsed once, before the main Orchid starts up, and 
cannot be changed during the Orchid runtime. Command-line flags are typically the kind of options that are needed to 
even parse `config.yml` or support other project-wide configuration, such as the input and output directories, or 
setting the default theme.   

## Example `config.yml`

```yaml
Editorial: # options passed to the Theme come from an object at that Theme's key
  menu: 
    - type: 'readme' 
    - type: 'license' 
wiki: # Each Generator receives options from the object at that Generators's key 
  sections:
    - 'userManual'
    - 'developersGuide'
services: # Services are all scoped under the `services` object, and are used to configure the larger Orchid behavior
  generators:
    disabled:
      - 'javadoc'
      - 'posts'
      - 'pages'
      - 'presentations'
      - 'styleguide'
```

> NOTE: By convention, Themes' keys are in `PascalCase` while Generators' keys are in `camelCase`. Options passed to 
> Services are all contained under the `services` object, each at their key within `services`. This all helps prevent 
> collisions between theme, generator, and service configuration options. 

> NOTE: Theme options may also be set at the 'theme' top-level object. If you are using multiple themes which are all
> expected to use the same configuration (a common occurrence), this is preferred over the theme-specific object. 
> However, if both the theme's key and `theme` are defined, the theme-specific object will take precedence.   

## `data/` Structure 

For larger and more complex sites, a single `config.yml` file will get messy very quickly. You may break up your 
monolithic `config.yml` into as many smaller files as you need, simply by adding a file in the `data/` directory, whose
filename corresponds to its options key. This process is recursive, and you can further break up _those_ files by 
creating directories within `data/`, and so on. You may also specify a filename _and_ and folder, and the two will be 
merged into one single options object, where the options in the file take precedence over the folder. 

For example, the following directory structure and YAML config are equivalent:

**Config in one single config.yml**

```yaml
theme:
    siteName: 'My Site'
    components:
        - type: pageContent
        - type: readme
        - type: license
    menu:
        - type: page
          page: 'home'
        - type: link
          title: 'Contact'
          url: '/contact'
```

**Config broken into several files**

```txt
/
|-- config.yml # main site configuration (still necessary)
|-- /data
    |-- theme.yml # contains siteName key
    |-- /theme
        |-- components.yml # contains an array of components
        |-- menu.yml # contains an array of menu items
```

## How these options get used

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
 switch to that theme, loading _that_ theme's options in the same manner as in step 3, before renering the pages from 
 that generator. 