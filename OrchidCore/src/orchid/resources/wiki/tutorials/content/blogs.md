---
pluginName: Orchid Posts
contentTypeTitle: blog
bundles:
  - Orchid Blog
  - Orchid All
---

{% extends '_wikiBase_contentTypes' %}

{% block 'pluginNotes' %}
## API Documentation

{% docs className='com.eden.orchid.posts.PostsGenerator' tableClass='table' tableLeaderClass='hidden' %}
{% endblock %}
