(function() {
    function ready(fn) {
        if (document.attachEvent ? document.readyState === "complete" : document.readyState !== "loading") {
            fn();
        } else {
            document.addEventListener('DOMContentLoaded', fn);
        }
    }

    ready(function () {
        var hosts = document.querySelectorAll('[data-shadowed]');

        for(var i = 0; i < hosts.length; i++) {
            var shadowHost = hosts[i];

            if (shadowHost.attachShadow) {
                var shadowRoot = shadowHost.attachShadow({mode: 'open'});
                var innerHtml = '';

                var stylesheets = shadowHost.getAttribute('data-shadowed');
                stylesheets = (stylesheets.includes(';')) ? stylesheets.split(';') : [stylesheets];

                for(var j = 0; j < stylesheets.length; j++) {
                    innerHtml = innerHtml + '<link rel="stylesheet" href="' + stylesheets[j] + '">';
                }

                innerHtml = innerHtml + shadowHost.innerHTML;
                shadowRoot.innerHTML = innerHtml;
            }
        }
    });
})();