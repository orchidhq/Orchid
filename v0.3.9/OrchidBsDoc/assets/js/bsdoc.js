function trianglify() {
    var header = $('#jumbotron');

    var t = new Trianglify({
        cellsize: 90,
        noiseIntensity: 0,
        x_gradient: [window.colors[1], window.colors[0]]
    });

    var pattern = t.generate(window.screen.width | header.outerWidth(), header.outerHeight() * 1.2);

    header.css('background-image', pattern.dataUrl);
}

function geopattern(title, selectedPattern) {
    var options = {};
    options.color = window.colors[0];
    options.baseColor = window.colors[1];

    if (selectedPattern) {
        options.generator = selectedPattern;
    }

    $('#jumbotron').geopattern(title, options);
}

function buildNav() {
    var $root = $('html, body');
    $('a').click(function () {
        var href = $.attr(this, 'href');

        if(href) {
            if (href.startsWith('#')) {
                var target = $(href);

                if (target.length != 0) {
                    $root.animate({
                        scrollTop: target.offset().top - 72
                    }, 500, function () {
                        window.location.hash = href;
                    });

                    return false;
                }
            }
        }

        return true;
    });

    $('.nav.bs-docs-sidenav li.has-children').click(function () {
        $(this).toggleClass('active');
    });

    $('[data-toggle="tooltip"]').tooltip();
}
