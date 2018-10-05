---
pluginName: Orchid Wiki
contentTypeTitle: wiki
bundles:
  - Orchid All
---

{% extends '_wikiBase_contentTypes' %}

{% block 'pluginNotes' %}
## API Documentation

{% docs className='com.eden.orchid.wiki.WikiGenerator' tableClass='table' tableLeaderClass='hidden' %}
{% endblock %}
