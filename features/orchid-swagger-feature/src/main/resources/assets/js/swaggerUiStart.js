---
---

(function() {
    function ready(fn) {
        if (document.attachEvent ? document.readyState === "complete" : document.readyState !== "loading") {
            fn();
        } else {
            document.addEventListener('DOMContentLoaded', fn);
        }
    }

    ready(function () {
        // Build a system
        {% if component.allSwaggerOptions.keySet() is not empty %}
        const ui = SwaggerUIBundle({{ component.allSwaggerOptions }});
        {% else %}
        const ui = SwaggerUIBundle({
            url: "{{ component.openApiSource }}",
            validatorUrl: null,
            dom_id: '#{{ component.swaggerElementId }}',
            deepLinking: true,
            presets: [
                SwaggerUIBundle.presets.apis,
                SwaggerUIStandalonePreset
            ],
            plugins: [
                SwaggerUIBundle.plugins.DownloadUrl
            ],
            layout: "StandaloneLayout",
            docExpansion: 'none',
            defaultModelRendering: 'model'
        });
        {% endif %}
        window.ui = ui
    });
})();
