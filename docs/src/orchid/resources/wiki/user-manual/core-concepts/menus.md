---
description: Orchid's built-in menu generation intelligently connects site structure to site menus which adapt themselves to site content.
---

## Menus

Menus are typically defined by the Theme and by the Page, and it is common for both kinds of menus to appear on a single
output Page's layout. Menus typically pull pages from the Index to dynamically generate the menu items, so all that
is required to keep a menu up-to-date is to choose the appropriate menu item types.

It is common for plugins to define their own menu item types, especially ones that correspond directly to the Pages a
Generator in the plugin creates. The exact methods of pulling indexed pages into a menu item are left up to the plugin, 
and may be as opinionated as showing the latest blog posts in a single category, or as generic as simply asking for a 
URL to link to.

The most common menu items are described below. Individual plugins may contribute additional types.

### Page Menu Item

Add an item to the menu to any single page in your site by its `itemId` (optionally filtered by `collectionType` and 
`collectionId`). See {{ anchor('Internal Links') }} for more about locating to pages with these properties.

This menu item is analogous to the `{{ anchor('find()', itemId='Internal Links', pageAnchorId='find-function') }}` or 
`{{ anchor('anchor()', itemId='Internal Links', pageAnchorId='anchor-function') }}` template functions.

```yaml
menu: 
  - type: `page`
    itemId: 'itemId'
    collectionId: 'collectionId'
    collectionType: 'collectionType'
```

### Collection Pages Menu Item

Add items to the menu to a group of pages located by their `collectionType` and `collectionId`. See 
{{ anchor('Internal Links') }} for more about locating to pages with these properties.

This menu item is analogous to the 
`{{ anchor('findAll()', itemId='Internal Links', pageAnchorId='find-all-function') }}` template function.

```yaml
menu: 
  - type: `collectionPages`
    collectionId: 'collectionId'
    collectionType: 'collectionType'
```
