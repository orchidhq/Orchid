---
description: A collection of Template Tags and Components that help you get past the writer's block and make building your site a dream.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973072/plugins/writersblocks.jpg
    alt: Writer's Blocks
    caption: Photo by Ilario Piatti on Unsplash
tags:
    - markup
    - blog
---

## About

This plugin adds a collection of useful tags and functions for Pebble to make writing content simpler.

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Try the [starter app](https://github.com/orchidhq/OrchidStarter)

## Usage

### Tags

#### Alert

Wrap content in a callout or bootstrap-style alert.

```jinja
{% verbatim %}
{% alert 'warning' %}
    This is your final warning!
{% endalert %}
{%- endverbatim %}
```

#### Github Gist

Embed a Github Gist.

```jinja
{% verbatim %}
{% gist 'gistId' %}
{%- endverbatim %}
```

#### Instagram

Embed a photo from Instagram.

```jinja
{% verbatim %}
{% instagram 'username' 'postId' %}
{%- endverbatim %}
```

#### Spotify

Embed a Spotify track or playlist.

```jinja
{% verbatim %}
{% spotify 'track | playlist' 'trackOrPlaylistID' %}
{%- endverbatim %}
```

#### Tabs

Embed a Twitter post, collection, or timeline.

```jinja
{% verbatim %}
{% tabs %}
    {% tab1 'One' %}Tab Content One{% endtab1 %}
    {% tab2 'Two' %}Tab Content Two{% endtab2 %}
{% endtabs %}
{%- endverbatim %}
```

#### Twitter

Embed a Twitter post, collection, or timeline.

```jinja
{% verbatim %}
{% twitter 'username' %}
or
{% twitter 'username' 'tweetId' %}
{%- endverbatim %}
```

#### Youtube

Embed a Youtube video.

```jinja
{% verbatim %}
{% youtube 'videoId' %}
{%- endverbatim %}
```

### Functions

#### Encode Spaces

Converts spaces to HTML-encoded non-breaking spaces to preserve spacing.

```jinja
{% verbatim %}
{{ encodeSpaces('text to encode') }}
{%- endverbatim %}
```

#### Newline to BR

Converts newlines to BR tags to force HTML line breaks.

```jinja
{% verbatim %}
{{ nl2br('text to encode') }}
{%- endverbatim %}
```

#### Pluralize

Attempts to convert a word to its English plural form.

```jinja
{% verbatim %}
{{ pluralize('word', 2) }}
{%- endverbatim %}
```

### Components

#### Google Analytics/Tag Manager

The `googleAnalytics` and `googleTagManager` add the relevant tracking scripts to your pages as a
{{ anchor(title='meta component', itemId='Components', pageAnchorId='meta-components') }}. By default components are 
only added in production builds.

Both the `googleAnalytics` and `googleTagManager` meta-components work pretty similarly, and are added to your site 
as follows:

```yaml
# config.yml
theme:
    metaComponents:
      - type: 'googleAnalytics'
        propertyId: 'UA-XXXXX-Y' # Your Google Analytics Property ID
        productionOnly: true # set to false to add the component in debug builds (for verifying correctness)
```

```yaml
# config.yml
theme:
    metaComponents:
      - type: 'googleTagManager'
        containerId: 'GTM-XXXX' # Your GTM container ID
        productionOnly: true # set to false to add the component in debug builds (for verifying correctness)
```
