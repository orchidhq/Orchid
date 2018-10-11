---
pluginName: Orchid Pages
contentTypeTitle: static page
bundles:
  - Orchid Blog
  - Orchid All
---

{% extends '_wikiBase_contentTypes' %}

{% block 'pluginNotes' %}
## API Documentation

{% docs className='com.eden.orchid.pages.PagesGenerator' tableClass='table' tableLeaderClass='hidden' %}
{% endblock %}
