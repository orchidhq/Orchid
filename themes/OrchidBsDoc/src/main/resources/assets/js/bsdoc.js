function trianglify(allOptions) {
    var header = $('#jumbotron');

    $(window).resize(function() {
        trianglify(reverseColors, allOptions);
    });

    var defaultOptions = {
        cell_size: 90,
        x_colors: [window.colors[1], window.colors[0]],
        y_colors: 'match_x',
        height: header.outerHeight(),
        width: header.outerWidth()
    };
    if (allOptions) {
        for (var key in allOptions) {
            defaultOptions[key] = allOptions[key];
        }
    }
    var trianglifyOptions = defaultOptions;

    var pattern = Trianglify(trianglifyOptions);
    header.css('background-image', 'url(' + pattern.png() + ')');
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
    setupSidenavClick();
    setupSmoothScroll();
    setupScrollspy();
    setupTooltips();
}

function setupSmoothScroll() {
    $('a').click(function () {
        var href = $.attr(this, 'href');

        if(href) {
            if (href.startsWith('#')) {
                var target = $(href);

                if (target.length != 0) {
                    $('html, body').animate({
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
}

function setupSidenavClick() {
    $('.nav.bs-docs-sidenav li.has-children > a').click(function () {
        $(this).parent().toggleClass('active');
    });
}

function setupScrollspy() {
    var sidebar = $('#bs-docs-sidebar');
    if(sidebar.length > 0) {
        console.log("scrolling spy");
        $('body').scrollspy({
            target: '#bs-docs-sidebar'
        });
        sidebar.affix({
            offset: {
                top: function() {return $('#jumbotron').outerHeight();},
                bottom: function() {return $('#footer').outerHeight();}
            }
        })
    }
}

function setupTooltips() {
    $('[data-toggle="tooltip"]').tooltip();
}