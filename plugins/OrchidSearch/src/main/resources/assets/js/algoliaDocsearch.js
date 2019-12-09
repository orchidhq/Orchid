---
---

docsearch({
    apiKey: '{{component.apiKey}}',
    indexName: '{{component.indexName}}',
    {% if component.appId is not empty %}appId: '{{component.appId}}',{% endif %}
    inputSelector: '{{component.selector}}',
    debug: {{component.debug}},
});
