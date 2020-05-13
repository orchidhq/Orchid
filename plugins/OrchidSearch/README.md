---
description: Add static full-text search with Lunr.js to any Orchid site
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973700/plugins/search.jpg
    alt: Search
    caption: Photo by Louis Blythe on Unsplash
tags:
    - search
---

## About

Orchid Search allows any Orchid site to have full-text search capabilities, without requiring a backend server or using
any 3rd-party search services! Using the wonderful [Lunr.js](https://lunrjs.com/) library, Orchid is able to generate an 
index of all content on your site as static JSON files, and then search it from your browser.

Alternatively, if your project is registered for [Algolia DocSearch](https://community.algolia.com/docsearch/), you can
easily add the required code snippets with the official Orchid integration.

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Try the [starter app](https://github.com/orchidhq/OrchidStarter)

## Usage

### Static Search with Lunr.js

Using static search with Orchid is a two-step process: instructing Orchid to build indices of your site's content, and 
adding the Javascript widget to enable client-side search functionality. Because static search requires Orchid to 
generate search indices and the search is performed directly in the browser, it is best for small sites.

Generating search indices is enabled by default when this plugin is added to your site. The content of the indices is 
the "intrinsic page content" for every page in your site, which is generated from the `indices` generator. You can 
control which other plugins are included in the generated indices with the `includeFrom` and `excludeFrom` options on 
the `indices` generator:

```yaml
# config.yml
indices:
  includeFrom:
    - 'wiki'
    - 'pages'
    - 'posts'
  excludeFrom:
    - 'javadoc'
```

Next, you need to add the `orchidSearch` meta-component to your pages. This component will automatically attach itself 
to the search field in your theme, which is already set up correctly in all official themes. 

```yaml
# config.yml
theme:
  metaComponents:
    - type: 'orchidSearch'
```

### Algolia DocSearch

In contrast to Orchid's own static search, which becomes slow and inefficient with large sites, Algolia Docsearch is a
much more optimized client-server solution hosted by Algolia and offered as a free search service to any open-source 
project. OrchidSearch is the official DocSearch integration for Orchid.

To get started, [fill out the form](https://community.algolia.com/docsearch/#join-docsearch-program) to get your project
approved by Algolia for DocSearch. Once it's been accepted, simply add the `algoliaDocsearch` meta-component, and 
similar to the `orchidSearch` component, it will automatically attach itself to your theme's search input. You will need 
to add your DocSearch credentials to this component for it to work properly:

```yaml
# config.yml
theme:
  metaComponents:
    - type: 'algoliaDocsearch'
      apiKey: '<API_KEY>'
      indexName: '<INDEX_NAME>'
      appId: '<APP_ID>' # Should be only included if you are running DocSearch on your own.
```

When using DocSearch, you probably do not want the search indices being generated anymore, as they slow down your 
builds. Simply disable the `indices` generator to prevent them from being created. 

```yaml
# config.yml
services:
  generators:
    disabled:
      - 'indices'
```
