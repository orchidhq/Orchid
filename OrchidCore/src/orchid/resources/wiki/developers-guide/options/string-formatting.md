---
---

{% extends '_developersGuideBase' %}

{% block sectionBody %}

Most options that extract a simple or primitive value (not a list or a map) are converted to Strings, then parsed back
out into the resulting type. When the value is a String, Orchid will do processing on each String to format it with
dynamic information. By default, Orchid passes each String through Clog's Parseltonuge formatter, allowing you to use
any of the registered Clog Spells as dynamic parts of the string. Future uses will allow each primitive values to 
reference resource strings, such as for translation.

{% endblock %}