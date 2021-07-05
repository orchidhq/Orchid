---
---

var refTagger = {
    settings: {
        bibleReader: "{{ component.bibleReader.value }}",
        bibleVersion: "{{ component.bibleVersion.value }}",
        roundCorners: {{ component.roundedCorners }},
        dropShadow: {{ component.dropShadow }},
        linksOpenNewWindow: {{ component.openInNewWindow }},
        {% if component.darkMode %}tooltipStyle: "dark",{% endif %}
        socialSharing: ["faithlife","twitter","facebook"],
        noSearchTagNames: [{{ component.excludeTagsFormatted()|raw }}],
        noSearchClassNames: [{{ component.excludeClassesFormatted()|raw }}],
    }
};
(function(d, t) {
    var g = d.createElement(t), s = d.getElementsByTagName(t)[0];
    g.src = '//api.reftagger.com/v2/RefTagger.js';
    s.parentNode.insertBefore(g, s);
}(document, 'script'));
