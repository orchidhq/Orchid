---
---

{% extends '_wikiBase' %}

{% block sectionBody %}

## Collections
---

Collections take groups of pages or other indexed content and make it accessible. Whenever you want to create a link 
from one page to another, you will typically use one of the `anchor`, `link`, `find` or `findAll` Pebble functions to 
lookup the related content. All these functions implement a similar interface, so using one over another is simple and 
always familiar. 

Internally, these functions find the related content based on the Collections that a each generator has set up, 
delegating the hard work of determining how exactly to find the content to the plugin, while keeping a common linking 
strategy across all plugins, regardless of how simple or complex their lookups are.

{% endblock %}
