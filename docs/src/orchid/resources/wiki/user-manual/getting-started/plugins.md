---
description: Orchid's many plugins ease the transition from other tools, see the best plugins here. 
---

Orchid was created from the very beginning to support plugins. In fact, everything that Orchid does is set up in a 
modular, pluggable way, which makes it possible to customize your site with exactly the features you want and nothing 
more.

## Popular Plugins

{% for plugin in data.featuredPlugins %}
### {{ anchor(plugin.title, plugin.itemId) }}

{{ find(plugin.itemId).description }}
{% endfor %}

## Complete Plugin List

The full list of available plugins is available in the plugin directory. You can also browse themes, or find bundles
of plugins which are frequently used together.

<div class="field is-grouped is-grouped-multiline">
  <p class="control is-expanded">
    {{ anchor(title='Browse Plugins', collectionType='taxonomies', itemId='plugins', customClasses='button is-primary is-block') }}
  </p>
  <p class="control is-expanded">
    {{ anchor(title='Browse Themes', collectionType='taxonomies', itemId='themes', customClasses='button is-primary is-block') }}
  </p>
  <p class="control is-expanded">
    {{ anchor(title='Browse Bundles', collectionType='taxonomies', itemId='bundles', customClasses='button is-primary is-block') }}
  </p>
</div>

## Custom Plugins

If you're interested in creating custom plugins for Orchid, either for private use or to publish publicly, check out the
{{ anchor('Extending Orchid') }} guide. 
