---
description: Learn the unique way that Orchid enables a site to grow and adapt to large sites.
---

## Introduction

While Orchid can be used for smaller sites, it really starts to shine when you start using Archetypes to easily 
configure many pages at once. This tutorial will introduce you to this concept.

Before continuing, make sure you have followed along with the previous tutorial and have started your local Orchid 
server with `gradle orchidServe`. We will be building on that example in this tutorial.

You can follow along with this tutorial on your own, or find the source for this in the 
[OrchidTutorials repository](https://github.com/orchidhq/OrchidTutorials/tree/master/06).

## The Problem With Large Sites

There are literally hundreds of static site generators out there, but nearly all of them are affected with the same 
issue: how do you easily manage a site which may contain hundreds, or even _thousands_, of pages? Most sites will 
contain large collections of pages which all need similar configurations, but most generators do not offer any way to 
keep the configurations for all these pages in sync. Scaffolding pages with common configurations built-in only works 
for new pages, but doesn't help at all when you are making large changes to the site. There must be a better way!

Orchid was created to solve exactly these kinds of issues, of building and maintaining sites at large scale over a long
period of time and many iterations. It principally uses a technique called **Archetypes** to solve this issue, but 
before we get too deep into the solution, let's make sure we fully understand the problem. 

### Repeated Configuration

In our example of building a website for a small business with multiple locations, we ended up creating one page for 
each location. The final site has pages at `/locations/houston`, `/locations/dallas` and `/locations/austin`, with 
corresponding Markdown files within the `pages` directory of our site resources directory. These files each contain a 
lot of information in their Front Matter that is unique to that one location, such as its address, phone number, or 
business hours, but also contains quite a lot of information that is intended to be the same across _all_ of the 
locations. An example of the full Front Matter for one of our locations pages follows:

```yaml
---
city: 'Houston'
state: 'TX'
postal_code: '12345'
address: '1234, Example Dr.'
phone: '(123) 456-7890'
business_hours:
  - 'M-F: 6am - 9pm'
  - 'Sa: 6am - 10pm'
  - 'Su: Closed'
template: 'location' 
menu:
  - type: pageIds
    structure: nested
  - type: separator
    title: 'All Locations'
  - type: pages
    group: 'locations'
---
```

We can break this one Front Matter heading into two sections: one section for the configuration values common to all 
locations, and one unique to this location:

```yaml
# unique to Houston location
city: 'Houston'
state: 'TX'
postal_code: '12345'
address: '1234, Example Dr.'
phone: '(123) 456-7890'
business_hours:
  - 'M-F: 6am - 9pm'
  - 'Sa: 6am - 10pm'
  - 'Su: Closed'
  
# common to all locations
template: 'location' 
menu:
  - type: pageIds
    structure: nested
  - type: separator
    title: 'All Locations'
  - type: pages
    group: 'locations'
---
```

While we could keep copying this common configuration to all new locations, it definitely isn't ideal. It requires us to 
make sure that all new locations have properly copied over this new configuration, or else setting up a scaffold to do 
this for us that will surely get out of date at some point in the future. 

### Inflexible Configuration

Another problem with managing the configurations for many pages manually or via scaffolding is that it makes it 
incredibly difficult to play around with the settings to find what works best for all pages. If you were to set up a few
pages, or a few hundred pages, using the `location` page template as shown earlier, and then later decided to change it, 
you would have a long and difficult road ahead of you. Using static configuration and scaffolding just doesn't work for
long-term site maintenance or iterative development. 

## Orchid Archetypes

Orchid has a unique way of addressing this problem, using a technique called _Archetypes_. The main idea is very 
straightforward: instead of using the same configurations in the Front Matter of many pages, you can just put it in 
`config.yml` instead, and Orchid will treat it as though it were in the Front Matter! 

### "All Pages" Archetype

Let's look at an example; add the following snippet to your `config.yml`, and remove the common configuration from all 
your locations pages:

```yaml
allPages:
  template: 'location' 
  menu:
    - type: pageIds
      structure: nested
    - type: separator
      title: 'All Locations'
    - type: pages
      group: 'locations'
```

Look at that, your site looks the exact same! Only now, you don't have a bunch of configuration copied amongst your 
locations files! By moving the common configuration to the `allPages` property in `config.yml`, you've instructed Orchid
to use that configuration data _in addition_ to the config present in each page's Front Matter. 

### "Page Group" Archetype

But this solution isn't perfect yet. The `allPages` Archetype data will be used on _every_ page that Orchid generates, 
which means all our services and staff pages will also start using the page template and menu we've set up. We only want
our locations pages to have the configuration. Fortunately, Orchid comes with many different archetypes, which are 
more scoped to just the pages you want them on. Let's look at another one that will work much better:

```yaml
pages:
  locations:
    template: 'location' 
    menu:
      - type: pageIds
        structure: nested
      - type: separator
        title: 'All Locations'
      - type: pages
        group: 'locations'
```

This time, instead of putting the archetype data at `allPages` in `config.yml`, we put it at `pages.locations`. Now, 
when the site rebuilds, this configuration data will only be pulled into our locations pages but not the services or 
staff pages, just like we want!

### What's Going On Here?

You'll notice that `pages` is the name of the Pages plugin which creates our locations pages, and that our locations are
all in the `locations/` top-level subdirectory, which gives them a "group" of `locations`. The "static pages" plugin is 
opinionated in this way, and expects that pages in subdirectories are related, and so provides its own custom Archetype
to help with the configuration of that group. You can set up dedicated "page group" configurations for the other groups
as well, and they will automatically adapt. 

Other plugins each have their own opinionated conventions for Archetypes as well. For example, the "Posts" plugin allows
similar configuration to be pulled from `config.yml` for each post's category, and the "Wiki" plugin does the same for 
each section. 

There are many different types of Archetypes. Most will pull data from `config.yml`, some work in completely different 
ways, but they are all working together to fetch the configuration values you need without having to specify them all
directly in a page's Front Matter. 

With so many different ways to configure each page it can be a bit tricky to keep it all straight, but the next tutorial
will show you how Orchid's self-documenting nature and its admin panel allows you to find all this information for 
yourself.

### Using Multiple Archetypes

Pages usually have more than one possible archetype, and you are able to mix configuration values from all of them at 
the same time. For example, you can use the "all pages" Archetype to set all pages in your site to use a given layout 
template, and then use the "page group" Archetype to customize the page template. There are a few things to keep in 
mind, however, when configuring pages with multiple Archetypes:

- For single values (such as Strings or numbers):
    - Archetypes are prioritized. If multiple Archetypes provide the same property, the value from the Archetype with 
        the highest priority is chosen. For example, the "page group" Archetype has higher priority than the "all pages"
        Archetype since it is more specialized, so if both archetypes set a layout, the one set by the "page group" 
        archetype would be used.
    - Values specified in a page's Front Matter _always_ override Archetype values. For example, if the "page group" 
        Archetype set the layout, it can be overridden for a single page by setting the layout in that page's Front 
        Matter.
- For lists or maps:
    - List items are concatenated together. The order in which list items are added is unspecified, but most items that
        are configured as a list (such as menu items) have a way of manually specifying the sort order that doesn't rely
        on the order the list items originally appear in. 
    - Maps are combined with a deep merge. If multiple Archetypes include the same key, the values at each key are
        recursively combined with this same strategy, where higher-priority Archetypes override single values, and lists
        and maps are merged together.
        
Using this knowledge, let's set up our locations pages using both the "all pages" and "page groups" archetypes. We'll 
also go ahead and set up similar archetype configs for the services and staff pages. Replace the `config.yml` contents 
with the following snippet:

```yaml
theme:
  primaryColor: '#dd9999'
  menu:
    - type: 'page'
      itemId: 'Locations'
    - type: 'page'
      itemId: 'Services'
    - type: 'page'
      itemId: 'Staff'

allPages:
  template: 'page'
  menu:
    - type: pageIds
      structure: nested
      order: 1

pages:
  locations:
    template: 'location'
    menu:
      - type: separator
        title: 'All Locations'
        order: 2
      - type: pages
        group: 'locations'
        order: 3
```

When the site rebuilds, you'll notice that the `template` for the locations pages is set to `location`, and that all 
pages have the `pageIds` menu item, which has been set from the "all pages" archetype, while the "page groups" archetype
added the page menu items for all locations.

## Conclusion

As your site grows in scope and complexity, there is a need to be able to configure it accordingly. There are many pages
that are similar, and many of those might have subtle nuances that need to be addressed individually. Orchid's 
Archetypes gives you the freedom to do exactly that, so let's recap: 

1. Large sites typically lead to lots of configuration repetition which doesn't scale very well, and is very difficult
    to change once established. 
2. Orchid allows pages to be configured from multiple sources, not just from its Front Matter. Most commonly, this 
    additional configuration comes from the same `config.yml` that you can use to configure the rest of the site, so 
    everything is nicely managed from one location.
3. The specifics of where in `config.yml` the Archetype data comes from is determined by the individual plugin. Plugins
    each have their own conventions for how to structure Archetype data that makes the most sense for that one plugin. 
4. Multiple Archetypes can be used simultaneously, and are ordered such that more-specific Archetypes override 
    configurations from more-general ones. When multiple archetypes define lists or maps at the same key, they are 
    merged recursively rather than overriding one another.
