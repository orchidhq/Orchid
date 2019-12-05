---
description: Get to know the basics of how Orchid makes it easy to generate internal links in you site.
---

## Introduction

Our Orchid site is starting to get pretty well furnished and is easy to maintain, but there are still a few things we
need to clean up before it is fully ready-to-go. Most notably, we still have those hard-coded links in our locations
index page. In this tutorial, I will show you the way Orchid addresses this problem.

Before continuing, make sure you have followed along with the previous tutorial and have started your local Orchid 
server with `gradle orchidServe`. We will be building on that example in this tutorial.

You can follow along with this tutorial on your own, or find the source for this in the
[OrchidTutorials repository](https://github.com/orchidhq/OrchidTutorials/tree/master/04).

## Making Dynamic Links

Let's first review what we have in our `locations/index.md` page:

```twig
- [Houston](http://localhost:8080/locations/houston)
- [Dallas](http://localhost:8080/locations/dallas)
- [Austin](http://localhost:8080/locations/austin)
```

There are a few issues with this. The first, most obvious issue is that these links are hardcoded to using our 
development base URL of `http://localhost:8080`. We don't want to go live with this site when it has links to 
development URLs, but Markdown doesn't have any way to dynamically change those links.

But remember how we added a Front Matter block to enable Pebble precompilation in the previous tutorial? Even though we
don't really have any data to put in Front Matter, let's add it anyway so I can introduce you to one of the important
**global variables** available to you in Orchid, `site`.

### Site Global Variable

The `site` variable is available on every page in Orchid, and contains a few properties that might be of use, either for
display or for control flow. A few notable properties are: 

- `site.about` - Additional information about your site, such as its title, avatar, favicon. Another tutorial will 
    teach you how to customize these values
- `site.baseUrl` - Your site's base URL
- `site.debug` - True if Orchid is running in debug
- `site.orchidVersion` - The current version of Orchid
- `site.version` - The current version of your app or site

The `site.baseUrl` property looks like just what we need! We can replace the hard-coded base URL in `locations/index.md`
with that variable instead, and it will automatically update these URLs based on the `baseUrl` property set in 
`build.gradle`.

```twig
{% verbatim %}
---
---
- [Houston]({{ site.baseUrl }}/locations/houston)
- [Dallas]({{ site.baseUrl }}/locations/dallas)
- [Austin]({{ site.baseUrl }}/locations/austin)
{% endverbatim %}
```

These links are still kind-of "hard-coded", however, since the path of these links is just passed directly to the 
Markdown processor. Instead, we cal pass these paths to the `baseUrl` Pebble filter, which allows Orchid to be a bit
more clever with these paths (to ensure proper formatting), and is also easier to understand and more idiomatic.

```twig
{% verbatim %}
---
---
- [Houston]({{ '/locations/houston'|baseUrl }})
- [Dallas]({{ '/locations/dallas'|baseUrl }})
- [Austin]({{ '/locations/austin'|baseUrl }})
{% endverbatim %}
```

And yet, we can still do better. There are still some parts to those URLs that might get changed without our knowing,
which would lead to broken links. Let's see how we can avoid that.

## Linking Functions

Orchid comes with a few functions that can be used to intelligently find other pages within your Orchid site. Generally-
speaking, you can use these functions to locate any page by its title, and from that, let Orchid generate the entire URL
for you. This separates the link that is printed out from your intent behind making the link, which is to go to a 
specific, known page.

### Link Function

The first function we can use is `link()`. Let's start with an example:

```twig
{% verbatim %}
---
---
- [Houston]({{ link('Houston') }})
- [Dallas]({{ link('Dallas') }})
- [Austin]({{ link('Austin') }})
{% endverbatim %}
```

Now, instead of manually creating the links to our individual location pages, we simply pass the _title_ of our target 
page to the `link()` function, which returns the String URL of the page whose title is the same, which is exactly what 
we want! If we change the location of our locations pages, say to `/locations/tx`, we do not have to go and update all
our links to those pages as long as they still have the same title. In addition, these links use the same base URL that 
is in `site.baseUrl`, so we can trust that the links will adapt to both our development and production sites. 

### Anchor Function

The `link()` function is very useful, but there is still a problem. If we happen to pass the title of a page that 
doesn't exist, it will simply return an empty string. But Markdown will still create an anchor tag, though it will just 
have an empty `href` attribute. This is bad both for SEO and for your user's browsing experience. What we really want is 
to only create a clickable link if the page actually exists, otherwise just render text. While this isn't a perfect 
solution, at least you will have the visual cue of text _not_ being a link when it should, and it's less detrimental 
to your SEO.

Most SSGs and CMSs would have you just check if that returned URL is empty, and wrap the link in an `if` statement. But 
this gets tedious and makes your templates hard to read, so Orchid offers another tag, specifically designed for 
intelligently generating a clickable anchor link, `anchor()`. With the `anchor()` function, we can replace the entire 
Markdown link, and let Orchid create it for us instead:

```twig
{% verbatim %}
---
---
- {{ anchor('Houston') }}
- {{ anchor('Dallas') }}
- {{ anchor('Austin') }}
{% endverbatim %}
```

Not only is our template easier to read now, but it won't generate links if the page doesn't exist, instead just 
returning the text that is passed to the function directly. By default, the text passed to the function is used as the 
text in the link, but this can be changed by passing an additional argument to the function. If there are 2 parameters,
then the first one is the text that will be displayed in the link, while the second is used to look up a page.

```twig
{% verbatim %}
---
---
- {{ anchor('Houston, TX', 'Houston') }}
- {{ anchor('Dallas, TX', 'Dallas') }}
- {{ anchor('Austin, TX', 'Austin') }}
{% endverbatim %}
```

## Generating the Page List

### Find All Function

Our locations index page is looking really good now. All our links are fully dynamic and will automatically update if 
the page is moved and for our different environments. But our business is growing very quickly, and we're adding new 
locations almost daily! You don't want to have to update this list every time you add a new location; what you really 
want is to have the entire list automatically generated based on the files you have already created for each location. 

Fortunately for you, Orchid has a way to handle that too, which works in very much the same way as just linking to a 
single page. There is a `findAll()` function that takes the same input as the `link()` and `anchor()` functions, but 
instead of returning the String URL or anchor to that page, it will return a list of Page objects, which you can then 
use to build links for yourself just as easily. 

Take a look at the snippet below as an example. It is incomplete because there is a bit more we will want to add to the 
functions parameters, but I'll get back to that in a moment. For now, just notice that we can iterate over the result 
and build links manually. 

```twig
{% verbatim %}
{% for page in findAll(...) %}
- [{{ page.title }}]({{ page.link }})
{% endfor %}
{% endverbatim %}
```

### Filtering Collections

In our example of small business locations, all the pages we want to link to come from the `pages` plugin, and more 
specifically, because their topmost folder within `pages` is `locations/` they are considered to be in the `locations` 
page group. Together, these two properties are known as the **collection type** and **collection ID**.

Each plugin that generates pages, like our Static Pages plugin, also generates one or more _collections_ of pages. These
collections are what's actually underlying all the `link()`, `anchor()`, and `findAll()` functions, and they all allow
you to filter the pages that get matched by their corresponding `collectionType` and `collectionId`. It is up to the 
plugin to decide exactly how these are formatted, so be sure to check the plugin's documentation, but as a general rule
of thumb, the `collectionType` matches the key of the generator they are from, while the `collectionId` is a specific 
subset of the pages from that generator.

When you pass a `collectionId` or `collectionType` to any of the above functions, the scope of the search is limited to 
just those matching collections. The `itemId` is the property we've been passing as the title, and is required for 
`link()` and `anchor()`, but is optional for `findAll()`. The `itemId` filters the pages contained within the matching 
collections.

Armed with this knowledge, we can finish out our example and not only generate dynamic links to our location pages, but 
also dynamically generate the entire list! 

```twig
{% verbatim %}
{% for page in findAll(collectionType='pages', collectionId='locations') %}
- [{{ page.title }}]({{ page.link }})
{% endfor %}
{% endverbatim %}
```

## Conclusion

And just like that, we can now maintain our Orchid site more easily than ever before! Not only are we able to update the 
content for all our locations pages at once (as we learned in the last tutorial), but we can generate our full list of
locations on the index page dynamically, so new locations are automatically included in that list. 

Let's review all the individual pieces we learned in this tutorial. 

1. Orchid offers many ways to generate dynamic links between your content. The first, and simplest way, is to use 
    `site.baseUrl` which prints out your site's base URL, fro which you can manually build the link to a page. The 
    `baseUrl` Pebble filter accomplishes the same thing, prepending the base URL to whatever is passed to it.
2. Instead of manually building a URL, you can ask Orchid for the String URL to a page in your site with the `link()`
    function, and build a Markdown link using that.
3. To prevent broken links, you can ask Orchid to build a full HTML anchor using the `anchor()` function. This uses the
    same search query as used by the `link()` function, and you may optionally pass the text to include in the link as 
    the first parameter to this method.
4. The same searches which generate single links can also be used to locate lists of pages from your Orchid site, using
    the `findAll()` function. You can iterate over the results of this list and build links to these pages for each item
    in the list, which allows you to make dynamic lists of page links.
5. The `link()`, `anchor()`, and `findAll()` functions also accept the optional `collectionType` and `collectionId` 
    parameters, which filter the results and can be used to narrow your search to exactly the pages you want. 
