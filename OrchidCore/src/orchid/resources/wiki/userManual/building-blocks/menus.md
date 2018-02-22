---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
{% endblock %}

{% block sectionBody %}
## Menus Overview

Menus are typically defined by the Theme and by the Page, and it is common for both kinds of menus to appear on a single
output Page's layout. Menus typically pull pages from the Index to dynamically generate the menu items, so all that
is required to keep a menu up-to-date is to choose the appropriate menu item types.

It is common for plugins to define their own menu item types, especially ones that correspond directly to the Pages a
Generator in the plugin creates. The exact methods of pulling indexed pages into a menu item are left up to the plugin, 
and may be as opinionated as showing the latest blog posts in a single category, or as generic as simply asking for a 
URL to link to.  
{% endblock %}