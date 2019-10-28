---
---

docsearch({
    // Your apiKey and indexName will be given to you once
    // we create your config
    apiKey: '{{component.apiKey}}',
    indexName: '{{component.indexName}}',
    //appId: '<APP_ID>', // Should be only included if you are running DocSearch on your own.
    // Replace inputSelector with a CSS selector
    // matching your search input
    inputSelector: '{{component.selector}}',
    // Set debug to true if you want to inspect the dropdown
    debug: {{component.debug}},
});
