---
description: Orchid's has a simple yet powerful convention for finding pages and generating links throughout your site.
---

## Locating Pages with Collections

Collections take groups of pages or other indexed content and make it accessible. Whenever you want to create a link 
from one page to another, you will typically use one of the [linking utilities](#linking-utilities) to lookup the 
related content. All these functions implement a similar interface, so using one over another is simple and always
familiar. 

Searching within collections is designed to have a consistent API that allows your searches to be as simple and 
intuitive to use as possible. There are three fundamental properties that are important when searching a collection: 
`itemId`, `collectionType`, and `collectionId`. In the vast majority of searches, just providing the `itemId` is enough 
to uniquely identify the page of interest, but in the case that this is not specific enough, the `collectionType` and 
`collectionId` can be used to guarantee the search returns the page you expect.

### Item Id

The `itemId` is usually the title of a page, but may be another "identifier" set up by specific plugins. Pages can have
more than one `itemId`, such as a class page from the {{ anchor('orchid-kotlindoc-feature') }} plugin having a title of the 
simple class name, but having an additional ID for the fully-qualified name.

Since 0.18.0, item IDs are no longer determined ad-hoc but are strongly defined for all pages, which means they can be
found in your {{ anchor('Admin Panel') }}!

### Collection Id

The `collectionId` is used to filter pages by those in a specific collection. An example of this is linking to another
page in the same wiki, or linking to a blog post with a specific tag.

### Collection Type

The `collectionType` is usually defined to be the `key` of the generator which produced the collection. In this way, you
can pretty easily restrict the search results to those from a single plugin.

## Linking Utilities  

### Anchor Function

Use the `anchor` function to easily create a link to another page in your Orchid site. If a page is found that matches
the query, an anchor tag is generated pointing to that page. If no page if found, the link text is returned directly, 
so this function will help prevent dead links.

The first parameter is the link text to be displayed, and the rest are the normal search params. If only one parameter
is given, it will be used as both the `itemId` _and_ the link text, making it almost trivial to make a link.

This function is analogous to the `{{ anchor('page', itemId='Menus', pageAnchorId='page-menu-item') }}` menu item.

```twig
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

This function is analogous to the `{{ anchor('page', itemId='Menus', pageAnchorId='page-menu-item') }}` menu item.

```twig
{% verbatim %}
{{ link(['itemId', 'collectionId', 'collectionType']) -> https://orchid.run/... }}
{{ link(itemId='itemId', collectionId='collectionId', collectionType='collectionType') }}
{% endverbatim %}
```

### Find Function

The `find` function will find a matching page and return the page object directly to the template.

This function is analogous to the `{{ anchor('page', itemId='Menus', pageAnchorId='page-menu-item') }}` menu item.

```twig
{% verbatim %}
{{ find(['itemId', 'collectionId', 'collectionType']) -> OrchidPage }}
{{ find(itemId='itemId', collectionId='collectionId', collectionType='collectionType') }}
{% endverbatim %}
```

### Find All Function

The `findAll` method matches _all_ pages in a given query, and returns the list of matching pages directly to the 
template.

This function is analogous to the 
`{{ anchor('collectionPages', itemId='Menus', pageAnchorId='collection-pages-menu-item') }}` menu item.

```twig
{% verbatim %}
{{ findAll(['itemId', 'collectionId', 'collectionType']) -> List<OrchidPage> }}
{{ findAll(itemId='itemId', collectionId='collectionId', collectionType='collectionType') }}
{% endverbatim %}
```

## Debugging Internal Links

Orchid 0.18.0 introduces a new "diagnosis mode" to help you find broken links and address various other issues in your 
site. It can be enabled with the `--diagnose` CLI flag, or through the conventions of your build tool shown below:

```bash
# Gradle
./gradlew orchidBuild -PorchidDiagnose=true

# Maven
./mvnw orchidBuild -Dorchid.diagnose=true

# SBT
./sbtw -Dorchid.diagnose=true orchidBuild
```

When diagnosis mode is enabled, all cases where the `anchor()` function does not return a page will be printed to the 
terminal. The same behavior will be logged for other cases throughout the internals and plugins of Orchid which try to
link to a page but cannot. This is helpful for detecting cases where a page was renamed or removed, but links to it are
still active. It may indicate a link that needs to be updated, or a section of your documentation that needs to be 
removed or rewritten.

In addition to detecting broken links, diagnosis mode will also enable other diagnostic info, such as complete 
stacktraces from exceptions thrown while rendering a template.
