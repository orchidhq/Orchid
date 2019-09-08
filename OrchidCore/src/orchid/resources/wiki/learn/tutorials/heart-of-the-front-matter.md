---
description: Practice using Front Matter to customize your site and use Pebble to make your content more dynamic.
---

## Introduction

Last tutorial we started to get our feet wet adding pages to our site and customizing their content. In this tutorial, 
I will introduce you to Front Matter and Page Templates, and show you how to use this to increase consistency among the
pages in your Orchid site.

Before continuing, make sure you have followed along with the previous tutorial and have started your local Orchid 
server with `gradle orchidServe`. We will be building on that example in this tutorial.

You can follow along with this tutorial on your own, or find the source for this in the
[OrchidTutorials repository](https://github.com/JavaEden/OrchidTutorials/tree/master/03). 

## Front Matter

If you've worked with other static site generators, you're probably very familiar with Front Matter. Orchid uses Front 
Matter just like the other tools, but there are a few special things that Orchid does with Front Matter, so don't 
skip ahead just yet!

### What Is Front Matter?

Front Matter is a way to add configuration values and other data to a single page. It comes as a block of YAML between 
pairs of triple-dashed lines at the very beginning of a page. Let's see how we can use Front Matter to customize the 
title of our `houston.md` page. Go ahead and change the contents of `pages/locations/houston.md` to the following:

```text

---
title: 'H-Town'
---
## Location

Houston, TX

## Address
 
1234, Example Dr.
Houston, TX, 12345

## Phone

(123) 456-7890

## Business Hours

M-F: 6am - 9pm
Sa: 6am - 10pm
Su: Closed
```

When Orchid has rebuilt your site, refresh the page and you'll notice that it's title now displays as "H-Town" instead 
of "Houston". You will also notice that the Front Matter has been removed from the actual page content, so that it is
not embedded into your theme with the rest of the text in the page.

Whenever Orchid sees a page with a Front Matter block, it will remove that block from the page content, parse the text 
within the Front Matter as YAML, and use that to configure the page. There are lots of configuration values available 
for use in the Front Matter, and a later tutorial will show you how to find all of these values, but you are not limited
to just those configuration values. You can put whatever kind of data you want in your Front Matter, and it will become 
available for use just the same.

### Pre-Compilation

Now that we have Front Matter added to the top of our page, Orchid will start doing something really cool. For our 
`houston.md` page, before it gets processed as Markdown, it will be _precompiled_ as Pebble. Pebble is the main 
templating language used by Orchid, and it has a syntax very similar to Liquid or Twig, which means it can render any 
text content it wants.

What this means for us is that we can use Pebble to add dynamic data into our Markdown, which can then be processed as 
Markdown afterwards.

To see how this works, let's move the info for our Houston business location into its Front Matter, and then use Pebble
to "inject" that data back into our Markdown.

```text
{% verbatim %}
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
---
## Location

{{ city }}, {{ state }}

## Address
 
{{ address }}
{{ city }}, {{ state }} {{ postal_code }}

## Phone

{{ phone }}

## Business Hours

{% for hours in business_hours %}
- {{ hours }}
{% endfor %}
{% endverbatim %}
```

It should be pretty obvious what's going on in this page now. All the tags that look like 
`{% verbatim %}{{ city }}{% endverbatim %}` will print that variable into the page at that spot. There are also _tags_,
such as the `{% verbatim %}{% for %}{% endverbatim %}` and `{% verbatim %}{% if %}{% endverbatim %}` tags, which will 
iterate over the data or control which sections of content get displayed. This makes it possible to use Pebble kind-of 
like a full programming language, and because it runs before the page is converted to Markdown, you can use it to build 
more complex and powerful Markdown pages much more easily.

This precompilation step is only performed for pages that have a Front Matter block, and it is entirely optional. 
However, in some cases you may find Pebble to make the underlying page format unparseable and may wish to disable it. If 
you need to have Front Matter but don't want the rest of the page precompiled, you can set `precompile` to `false` in 
the Front Matter. Alternatively, you may precompile it as a different language by setting `precompileAs` to the desired 
file extension.

Alright, so now that we've got our Houston page set up with Front Matter and being precompiled as Pebble, we should 
probably do the same with our other locations pages as well. 

```text
{% verbatim %}
---
city: 'Austin'
state: 'TX'
postal_code: '12345'
address: '1234, Example Dr.'
phone: '(123) 456-7890'
business_hours:
  - 'M-F: 6am - 9pm'
  - 'Sa: 6am - 10pm'
  - 'Su: Closed'
---
## Location

{{ city }}, {{ state }}

## Address
 
{{ address }}
{{ city }}, {{ state }} {{ postal_code }}

## Phone

{{ phone }}

## Business Hours

{% for hours in business_hours %}
- {{ hours }}
{% endfor %}
{% endverbatim %}
```

```text
{% verbatim %}
---
city: 'Dallas'
state: 'TX'
postal_code: '12345'
address: '1234, Example Dr.'
phone: '(123) 456-7890'
business_hours:
  - 'M-F: 6am - 9pm'
  - 'Sa: 6am - 10pm'
  - 'Su: Closed'
---
## Location

{{ city }}, {{ state }}

## Address
 
{{ address }}
{{ city }}, {{ state }} {{ postal_code }}

## Phone

{{ phone }}

## Business Hours

{% for hours in business_hours %}
- {{ hours }}
{% endfor %}
{% endverbatim %}
```

But now you'll notice that the main content of all three of our pages is the exact same! We've managed to move all the 
data into our Front Matter to make it easier to manage, but trying to keep all our locations pages looking the same will 
be difficult if we have to change every page whenever we want to update it. 

Most SSGs and CMSs would address this with theme layouts. You would add a new layout for these locations pages, and in
that layout you'd put all the structured data that we currently have in our page content. While Orchid does have theme 
layouts (we'll get to that in another tutorial), this can cause an issue as they aren't very reusable, and you end up 
with a lot of single-use layouts that are all difficult to maintain or make site-wide changes with.

To address this, Orchid introduces several new mechanisms for managing the content on a page that are maintainable, 
reusable, and composable. The technique we'll talk about in the rest of this tutorial is called **page templates**.

## Page Templates

### What Are Page Templates?

Put simply, page templates are kind of like your theme's layout, except they are only concerned with the small section 
of the layout that displays your page content. Typically, a full layout also includes menus, sidebars, widget areas, and
also renders the full HTML structure, but a _page template_ just structures the _page content_. While a normal CMS would
have you create `page-single.peb` and `page-sidebar.peb`, you can now just create a `pages/page.peb` template for your 
page content and then embed that in the `layouts/single.peb` or `layouts/sidebar.peb` theme layout. 

The different might seem insignificant in this small example, but for sites with many different types of pages and 
layout styles, you will actually use exponentially fewer templates than the alternative. Consider that a site with 10 
page types and 3 different layouts would need **30 templates** in the traditional manner, but only 13 with Orchid! For 
even larger sites, the savings are even larger, and the maintenance burden will decrease significantly.

### Creating a Page Template

The first thing we'll need to do is make ourselves a new page template, and then we'll tell Orchid to use that template 
instead of the default one. So make a page in `templates/pages/location.peb` and add the following content.

```jinja
{% verbatim %}
{% filter compileAs('md') %}
## Location

{{ city }}, {{ state }}

## Address

{{ address }}
{{ city }}, {{ state }} {{ postal_code }}

## Phone

{{ phone }}

## Business Hours

{% for hours in business_hours %}
- {{ hours }}
{% endfor %}

---
{% endfilter %}

{{ page.content | raw }}
{% endverbatim %}
```

Notice that this new file has the `.peb` extension, which means it will be compiled as Pebble. Unlike our Markdown 
pages, page templates should use the file extension for a _templating_ language, like `.peb`, and it does not support 
precompiling. This is because the page template is actually considered part of the _theme_ and not part of the 
_page content_. 

As such, when setting up your page template, you should design it as if it were part of the theme, and include 
`{% verbatim %}{{ page.content | raw }}{% endverbatim %}` at the appropriate area within that template. `page.content` 
is what is actually precompiling and rendering the Markdown of your page, and since Pebble is a _safe_ templating 
language, it will automatically escape the HTML page content to prevent cross-site-scripting (XSS) unless you use the 
`| raw` filter as it is printed out.

### Using Your Custom Page Template

To use our custom page template, we must add a property in each page's Front Matter telling Orchid which template we
want to use. The `templates` property accepts either a String or an array, and the first template that Orchid finds will
be loaded and used as the page template.

```text

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
---

Houston, TX location coming soon.
```

Orchid will look for the page template in several places. First it will look for a template in `templates/` that matches
the exact file path and filename. If that doesn't exist, it will look for any file with that filename in the 
`templates/pages/` directory. In either case, if a file extension is not provided, it will assume `.peb`. So by passing 
`location` as the template, our custom page template at `templates/pages/location.peb` will be used.

Any time that you specify a custom template, it will be preferred to the defaults for that page type. Plugins with 
special page types may set special formats for looking up page templates without needing to specify it. For example, the 
OrchidPosts plugin will default to looking for `post-[category].peb` or `post.peb` before falling back to `page.peb`. 
All pages, if not specified by the plugin or by the user, fall back to using `page.peb`, which is automatically provided
for you.

## Conclusion

Let's review what we've learned in this tutorial.

1. Orchid can render Markdown and other files just fine on their own, but you can optionally add a Front Matter block at 
    the start of the page to configure it or add custom data.
2. When a Front Matter block is present in a page, it will be first precompiled as a Pebble template. You can use any 
    Pebble tags or inject any page or site variables into the content before it gets processed as Markdown.
3. To increase consistency and ease the maintenance burden of large sites, you can use page templates in addition to 
    theme layouts to give structure to page content. Page templates can be mixed-and-matched with theme layouts to 
    provide great flexibility with many fewer templates than layouts alone.
4. You can choose the page template with the `templates` property in a page's Front Matter. This can be a string or an 
    array, and templates are typically kept in the `templates/pages/` directory.
5. Plugins may provide their own formats for looking up page templates, but setting one yourself will override the 
    preference of the plugin.
