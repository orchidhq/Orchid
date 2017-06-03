# Orchid

[![Build Status](https://travis-ci.org/JavaEden/Orchid.svg?branch=master)](https://travis-ci.org/JavaEden/Orchid) (Linux and Mac)
[![Build status](https://ci.appveyor.com/api/projects/status/0358qdkmfhbqedo1/branch/master?svg=true)](https://ci.appveyor.com/project/cjbrooks12/orchid/branch/master) (Windows)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](http://www.gnu.org/licenses/lgpl-3.0)

A beautiful and truly unique documentation engine and static site generator.

### Roadmap

- improve options parsing
    - Make Options the command-line arguments that are found by scanning classpath and are not able to be changed after
    the build has begun. The values of these options cannot be changed or overridden by Preferences
    - Make Preferences classes that process the content of `config.yml`, and `data/` files. It should open a clean
    annotation-based interface for parsing these preferences, and only the preferences declared in one of these classes
    will be available in templates. Preferences, by default, are re-processed with every build, so when running in watch
    mode, changes to preferences will be reflected across the project. This can be disabled with an Option flag. 
    - Options are a simple map of key-value pairs, which follow the Javadoc specification for command-line arguments
    - Preferences are a nested map and accessed in a similar way to the Index, except that nodes have _either_ a value 
    or children, not both (like JSON).
- Improve Components
    - Pass both the current Page and any additional parameters to a static factory method specific to the component.
    - Themes Page templates declare component areas where page components can be rendered
    - Components can be added to standard component areas forcefully by the Generator (i.e. Javadoc class methods as a 
    component would be registered in this way)
    - Components can also be added in a page's Front Matter, so that the user can craft individual pages around components
    - They can be applied on entire groups of Generator pages (i.e. all Post Archive Pages show Tag List components)
- Improve Themes
    - Standardize the usage of Layout, Page, and Component templates so that individual plugins can inject a Page 
    seamlessly into the themes's Layouts, and so that components can be rendered seamlessly in any Page template
    - More themes. I want a good theme used across all pages which has a strong sidebar for documentation, similar to Hugo
    - One Theme must be set for the whole project, but more themes may optionally be set on a per-generator basis. These
    additional themes are intrinsically part of the Preferences parsing process. Should pass the target theme to the
    Renderer along with the data to be rendered. This makes the theme process not a globally-defined construct, and also
    makes it possible for individual pages/components to force a theme upon themselves (e.g. a generator has a feature to
    apply themes on a page-by-page basis instead).
- Admin interface (long-term, far-off)
    - Allow full administration of every part of your Orchid site through its embedded admin area
        - Writing content for Pages which are defined by the Generators
        - Managing Preferences
        - Viewing which site components are registered to what
            - Themes being used by individual Generators
            - The Compilers/Parsers being used by a given Page
            - Which Generators are enabled/disabled
    - Everything is connected via websockets so the build can notify the admin page about whats going on with the build 
    - Wbsocket communications should be JSON payloads and should have a format that is compatible with the HTTP endpoints
    also available in the project, so that data can be requested either over HTTP or websocket