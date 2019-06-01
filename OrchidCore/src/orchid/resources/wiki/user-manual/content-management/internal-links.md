---
description: Orchid's has a simple yet powerful convention for finding pages and generating links throughout your site.
---

## Locating Pages with Collections
---

Collections take groups of pages or other indexed content and make it accessible. Whenever you want to create a link 
from one page to another, you will typically use one of the `anchor`, `link`, `find` or `findAll` Pebble functions to 
lookup the related content. All these functions implement a similar interface, so using one over another is simple and 
always familiar. 

Internally, these functions find the related content based on the Collections that a each generator has set up, 
delegating the hard work of determining how exactly to find the content to the plugin, while keeping a common linking 
strategy across all plugins, regardless of how simple or complex their lookups are.

While collections typically represent collections of pages, they may contain _anything_, which makes it possible for
plugins to easily open up access to its data in a decoupled fashion with a consistent API, regardless of what it is 
collecting.

## Searching In Collections
---

Searching within collections is designed to have a consistent API that allows your searches to be as simple and 
intuitive to use as possible. There are three fundamental properties that are important when searching a collection: 
`itemId`, `collectionType`, and `collectionId`. In the vast majority of searches, just providing the `itemId` is enough 
to uniquely identify the page of interest, but in the case that this is not specific enough, the `collectionType` and 
`collectionId` can be used to guarantee the search returns the page you expect. 

**Item Id**

The `itemId` is most commonly the Title of a page, and as such you can usually get the page you want just by linking to
a page title. Ultimately, plugins define which pages match a given `itemId` and are free to use whatever they need to 
match a page to your query, so be sure to check the plugins' documentation to know exactly how to format your `itemId`. 

In addition to using a page's title as the itemId, you can use a `key=value` reference, which will find pages based on 
a property in its Front Matter. 

```yaml
---
pageId: 'page-one'
---
```

```jinja
{% verbatim %}
{{ link('pageId=page-one') }}
{% endverbatim %}
```

**Collection Id**

The `collectionId` is used to filter pages by those in a specific collection. An example of this is linking to another
page in the same wiki, or linking to a blog post with a specific tag.

**Collection Type**

The `collectionId` is usually defined to be the `key` of the generator which produced the collection. In this way, you
can pretty easily restrict the search results to those from a single plugin.  

### Anchor Function

Use the `anchor` function to easily create a link to another page in your Orchid site. If a page is found that matches
the query, an anchor tag is generated pointing to that page. If no page if found, the link text is returned directly, 
so this function will help prevent dead links.

The first parameter is the link text to be displayed, and the rest are the normal search params. If only one parameter
is given, it will be used as both the `itemId` _and_ the link text, making it almost trivial to make a link.

This function only matches items if they are an {{anchor('OrchidPage')}}.

```jinja
{% verbatim %}
{{ anchor('Link Text/Item Id') -> <a href="...">Link Text</a> or "Link Text" }}
{{ anchor('Link Text', ['itemId', 'collectionId', 'collectionType']) }}
{{ anchor(title='Link Text', itemId='itemId', collectionId='collectionId', collectionType='collectionType') }}
{% endverbatim %}
```

### Link Function

Use the `link` function to render just the String URL to a matching page. This is useful if you need to display that URL 
directly or you need an anchor to wrap other elements. Otherwise you should use `anchor` to let Orchid render the link
for you. 

This function only matches items if they are an {{anchor('OrchidPage')}}.

```jinja
{% verbatim %}
{{ link(['itemId', 'collectionId', 'collectionType']) -> https://orchid.netlify.com/... }}
{{ link(itemId='itemId', collectionId='collectionId', collectionType='collectionType') }}
{% endverbatim %}
```

### Find Function

The `find` function will find a matching page and return the {{anchor('OrchidPage')}} object directly to the template.

```jinja
{% verbatim %}
{{ find(['itemId', 'collectionId', 'collectionType']) -> OrchidPage }}
{{ find(itemId='itemId', collectionId='collectionId', collectionType='collectionType') }}
{% endverbatim %}
```

### Find All Function

The `findAll` method matches _all_ pages in a given query, and returns the list of matching pages directly to the 
template.

```jinja
{% verbatim %}
{{ findAll(['itemId', 'collectionId', 'collectionType']) -> List<OrchidPage> }}
{{ findAll(itemId='itemId', collectionId='collectionId', collectionType='collectionType') }}
{% endverbatim %}
```

### Page Menu Item

Use the `page` menu item to find a matching page and add it to the menu. 

This menu item only matches items if they are an {{anchor('OrchidPage')}}.

```yaml
menu: 
  - type: page
    itemId: 'itemId'
    collectionId: 'collectionId'
    collectionType: 'collectionType'
```
