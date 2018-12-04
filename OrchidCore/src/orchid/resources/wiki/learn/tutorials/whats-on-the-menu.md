---
description: Learn how to configure your theme and generate dynamic menus.
---

## Introduction

Now that we have an understanding of how Orchid uses collections to find pages, let's apply this knowledge with a few new concepts in order to build dynamic menus. 

Before continuing, make sure you have followed along with the previous tutorial and have started your local Orchid server with `gradle orchidServe`. We will be building on that example in this tutorial.

You can follow along with this tutorial on your own, or find the source for this in the [OrchidTutorials repository](https://github.com/JavaEden/OrchidTutorials/tree/master/05). 

## Scaling Your Site

Our site currently has four pages in it: one for each of our business's locations in Houston, Dallas, and Austin, which are considered to be in the "locations" page group, since they all sit at `/locations/...` in the site URL structure; and one more page listing all locations. As our business grows, we will want to add more content to our site so that customers can find everything they need online. So let's add a few more pages in a couple more page "groups" for our services and staff. Add the following pages to your site (you can leave these files empty for now). 

{% highlight 'text' %}
└── pages/
    ├── locations/
    |   ├── houston.md
    |   ├── dallas.md
    |   └── austin.md
    ├── services/
    |   ├── home-repair.md
    |   ├── electrical.md
    |   └── security.md
    └── staff/
        ├── billy-bob.md
        └── john-doe.md
{% endhighlight %}

As an additional exercise, try creating index pages for these groups as well.

## Theme Customization

Before we start adding menus to our site, we first must get familiar with how to configure the site. 

Orchid has a special file in the root of your resources, `config.yml`. This file is a YAML config file, and in it, you can place all the configuration you need. Here are some examples of things that can be customized from `config.yml`:

- Theme colors 
- Theme menus 
- Which plugins to include or exclude
- Plugin parameters
- Some core Orchid functionality

To start things off, let's pick a better color for our theme. Since we're using the BsDoc theme in this example, add the following to your `config.yml` file:

{% highlight 'yaml' %}
theme:
  primaryColor: '#dd9999'
{% endhighlight %}

Once your site rebuilds, you'll notice everything that was previously purple is now a sandy red! The BsDoc theme is set up to recompile its SCSS stylesheets injecting configuration values for its primary color. In a later tutorial, I will go more in-depth about this process and how to find all the configurations available in any theme, but for now it suffices to know `config.yml` is where you go to customize your site and your theme.

Now we're ready to start building our site's menu!

## Theme Menus

Most themes in Orchid will have at least one menu area, your site's primary navigation, and some will include menus for other areas, such as the footer. Regardless of where a menu is in your theme, it will always be configured in the same way: in your theme's configuration in `config.yml`. Let's add a menu to the theme configuration we used earlier:

### Link Menu Item

{% highlight 'yaml' %}
theme:
  primaryColor: '#dd9999'
  menu:
    - type: 'link'
      title: 'Locations'
      url: 'http://localhost:8080/locations'
    - type: 'link'
      title: 'Services'
      url: 'http://localhost:8080/services'
    - type: 'link'
      title: 'Staff'
      url: 'http://localhost:8080/staff'
{% endhighlight %}

This will generate a menu with 3 items in the theme's main navbar, with each one pointing to the index page for each of our page groups. Let's break the configuration down a bit more because the structure of this configuration is used in many other places throughout Orchid as well.

The `menu` property of our `theme` object in `config.yml` should be a list of menu items. Each menu item should include a `type` field, which tells Orchid the type of menu item to use. There are many different menu item types; some examples are `link` (used in the example) which creates a menu item pointing to an arbitrary URL, and `page` which looks up a page in your site and links to it. 

Menu items are dynamically evaluated on each page and may add any number of items. `url` and `page` will each add 0 or 1 item to the menu (depending on whether they are configured correctly and a page can be found), but some other types like `pages` will add a menu item for each static page set up in your site pointing to that page. Not only that, but menu items are inherently recursive, where a menu item might contain a submenu, which might contain a submenu, and so on. Note that not every theme will support fully-recursive menus, and will commonly only support 1 or two levels of nesting.

Each type of menu item can also declare any number of options for additional customization. The `link` item above requires 2 additional properties: a `url` to link to, and a `title`. But these `link` items are not ideal for the same reasons described in the previous tutorial: the links are hardcoded to the development URL. Let's change these menu items to allow Orchid to generate the actual link dynamically, changing for development or production seamlessly as needed.

### Page Menu Item

{% highlight 'yaml' %}
theme:
  primaryColor: '#dd9999'
  menu:
    - type: 'page'
      itemId: 'Locations'
    - type: 'page'
      itemId: 'Services'
    - type: 'page'
      itemId: 'Staff'
{% endhighlight %}

This time, instead of using `link` menu items we're using `page`. Hopefully, you recognize the `itemId` property on these menu items; if not, you may want to go back through the previous tutorial to learn about linking functions. That's because the `page` menu item type (and many others) locate and link to pages in the exact same way as described in that tutorial. But as a quick refresher: you can generally link the proper page in your site by setting the `itemId` to the title of the page you want to link to, and `collectionType` and `collectionId` can help make sure you're linking to exactly the page you expect by filtering the pages to those in a specific collection.

## Page Menus

Themes aren't the only things that can define menus. It is very common for individual pages to also include menus that describe the content just on that page, or for linking to its related pages. Page menus are configured in exactly the same way as theme menus, it's just in a page's Front Matter instead of `config.yml`. Apart from that, there is absolutely no difference in how menus are generated between the two. So let's look at a couple other menu item types that will really take your site's menus to the next level!

### Static Pages Menu Item

The Pages plugin that we're using to set up our locations, services, and staff pages also includes a `pages` menu item type. This menu item type will display all the pages that the plugin is generating, optionally filtering it to a specific page group. Let's use this menu item type so that each page creates a menu which links to all the other pages in its group.

In all the locations pages, include the following snippet in its Front Matter:

{% highlight 'yaml' %}
---
menu:
  - type: 'pages'
    group: 'locations'
---
{% endhighlight %}

Now, when navigating to any of our Locations pages, you'll see a sidebar with links to all of our locations. You'll also notice that the BsDoc theme highlights the menu item if it matches the page you're currently on, so you always know where you are in the site! By specifying the `group` in the menu item config, only "locations" pages are added to the menu, but leaving it out will include _all_ static pages. 

You can do the same with the other page groups, but I'll leave that as an exercise for you.

### Page Ids Menu Item

Now that we have our locations pages linking amongst one another, now let's get each page generating links just for itself. Rather than linking to an external URL or an internal page, menu items can also link to IDs within the page. The `pageIds` menu item, also included as part of the Static Pages plugin, will find all headings with IDs in a page's content and generate menu items anchored to that ID. Orchid's Markdown processor is set up to generate IDs for each heading, so let's start by adding some sections to our locations pages with headings to link to. 

For example, in `pages/locations/houston.md` let's add the following content. You can add similar content the other locations pages as another exercise if you wish.

{% highlight 'md' %}
# About Our Houston Location

Duis et mauris in leo aliquam bibendum et ut purus. Nulla sagittis volutpat massa non vestibulum.  

## Specialty Services

Pellentesque lorem magna, porttitor sed massa vitae, vestibulum volutpat magna. 

## All Staff

Vestibulum tristique finibus est, sed suscipit dui commodo quis. Duis condimentum in neque at auctor. 

# Reviews

- Sed venenatis nibh a quam efficitur accumsan. 
- Vivamus in consectetur magna. 
- Donec viverra lorem nunc, eu finibus erat posuere ut. 
- Sed in leo ac est suscipit euismod a vitae sem.
{% endhighlight %}

Now let's set add the `pageIds` menu item to this page. I've also added a `separator` menu item between the page's own links and those that link to other locations pages for clarity.

{% highlight 'yaml' %}
---
menu:
  - type: 'pageIds'
    structure: 'nested'
  - type: 'separator'
    title: 'All Locations'
  - type: 'pages'
    group: 'locations'
---
{% endhighlight %}

Perfect! Orchid is now reading through the content on that page, pulling out all the IDs it finds, and dynamically generating menu items that will scroll the webpage to that location in its content. You can customize these page ID menu items to be either nested (such that `h2` headers are created as a submenu of the previous `h1`), or flat with no submenus. 

One important thing to note here is that only the intrinsic page content is considered when generating these page ID links. IDs from the theme or page templates are not looked at, but its just the actual content within the Markdown file that is used. 

## Conclusion

Now our site is starting to look and function like a complete website! Not only do we have lots of pages that are easy to customize individually, but we now have a way to dynamically generate menus which will link to all of our other locations, no matter how many new locations we add! 

Let's go back and review everything we learned in this tutorial.

1. `config.yml` is the central location for configuring your site's theme, including colors and menus.
2. The `theme.menu` property in `config.yml` sets up the primary site theme. In the BsDoc theme, this is the main navbar.
3. Menu configuration accepts a list of menu item objects. The `type` property of each object tells Orchid which type of menu item to create and each item type can also declare its own options for further customization. 
4. Some menu items types will add just one item to the generated menu, some will add many. 
5. Menu items may link to external URLs, lookup and link internally to pages within your site, or link to specific IDs within a single page. Looking up internal page links uses the same convention of `itemId`, `collectionType`, `collectionId` for locating pages as the linking functions do.  
