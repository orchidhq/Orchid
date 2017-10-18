/*
 Editorial by HTML5 UP
 html5up.net | @ajlkn
 Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
 */

(function ($) {

    skel.breakpoints({
        xlarge: '(max-width: 1680px)',
        large: '(max-width: 1280px)',
        medium: '(max-width: 980px)',
        small: '(max-width: 736px)',
        xsmall: '(max-width: 480px)',
        'xlarge-to-max': '(min-width: 1681px)',
        'small-to-xlarge': '(min-width: 481px) and (max-width: 1680px)'
    });

    $(function () {

        var $window = $(window),
            $head = $('head'),
            $body = $('body');

        // Disable animations/transitions ...

        // ... until the page has loaded.
        setTimeout(function () {
            $body.removeClass('is-loading');
        }, 100);

        // ... when resizing.
        var resizeTimeout;

        $window.on('resize', function () {

            // Mark as resizing.
            $body.addClass('is-resizing');

            // Unmark after delay.
            clearTimeout(resizeTimeout);

            resizeTimeout = setTimeout(function () {
                $body.removeClass('is-resizing');
            }, 100);

        });

        // Fix: Placeholder polyfill.
        $('form').placeholder();

        // Prioritize "important" elements on medium.
        skel.on('+medium -medium', function () {
            $.prioritize(
                '.important\\28 medium\\29',
                skel.breakpoint('medium').active
            );
        });

        // Fixes.

        // Object fit images.
        if (!skel.canUse('object-fit')
            || skel.vars.browser == 'safari')
            $('.image.object').each(function () {

                var $this = $(this),
                    $img = $this.children('img');

                // Hide original image.
                $img.css('opacity', '0');

                // Set background.
                $this
                    .css('background-image', 'url("' + $img.attr('src') + '")')
                    .css('background-size', $img.css('object-fit') ? $img.css('object-fit') : 'cover')
                    .css('background-position', $img.css('object-position') ? $img.css('object-position') : 'center');

            });

        // Sidebar.
        var $sidebar = $('#sidebar'),
            $sidebar_inner = $sidebar.children('.inner');

        // Inactive by default on <= large.
        skel
            .on('+large', function () {
                $sidebar.addClass('inactive');
            })
            .on('-large !large', function () {
                $sidebar.removeClass('inactive');
            });

        // Hack: Workaround for Chrome/Android scrollbar position bug.
        if (skel.vars.os == 'android'
            && skel.vars.browser == 'chrome')
            $('<style>#sidebar .inner::-webkit-scrollbar { display: none; }</style>')
                .appendTo($head);

        // Toggle.
        if (skel.vars.IEVersion > 9) {

            $('<a href="#sidebar" class="toggle">Toggle</a>')
                .appendTo($sidebar)
                .on('click', function (event) {

                    // Prevent default.
                    event.preventDefault();
                    event.stopPropagation();

                    // Toggle.
                    $sidebar.toggleClass('inactive');

                });

        }

        // Events.

        // Link clicks.
        $sidebar.on('click', 'a', function (event) {

            // >large? Bail.
            if (!skel.breakpoint('large').active)
                return;

            // Vars.
            var $a = $(this),
                href = $a.attr('href'),
                target = $a.attr('target');

            // Prevent default.
            event.preventDefault();
            event.stopPropagation();

            // Check URL.
            if (!href || href == '#' || href == '')
                return;

            // Hide sidebar.
            $sidebar.addClass('inactive');

            // Redirect to href.
            setTimeout(function () {

                if (target == '_blank')
                    window.open(href);
                else
                    window.location.href = href;

            }, 500);

        });

        // Prevent certain events inside the panel from bubbling.
        $sidebar.on('click touchend touchstart touchmove', function (event) {

            // >large? Bail.
            if (!skel.breakpoint('large').active)
                return;

            // Prevent propagation.
            event.stopPropagation();

        });

        // Hide panel on body click/tap.
        $body.on('click touchend', function (event) {

            // >large? Bail.
            if (!skel.breakpoint('large').active)
                return;

            // Deactivate.
            $sidebar.addClass('inactive');

        });

        // // Menu.
        $('nav.menu li .opener').click(function (e) {
            e.preventDefault();
            $(this).parent().children('ul:first').slideToggle('fast');
            $(this).toggleClass('active');
            $window.triggerHandler('resize.sidebar-lock');
        });

    });

})($);