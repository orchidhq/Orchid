---
description: 'Using multiple themes, you can pick the right theme for different parts of your site.'
---

Themes are what create structure to your site and make it unique. At its core, an Orchid theme simple contributes CSS 
and Javascript assets to the pages using that theme, and it also helps decide the templates that are used to lay out 
your pages. The templates and assets added by your theme can always be overridden in your local build, but it is never
necessary to know anything about the your themes templates for it to work well with Orchid. 

## Picking a Theme

Themes in Orchid are added just as a dependency in your `build.gradle`, but they are not actually used in your site 
unless you set the `theme` property in `build.gradle` or set it as the theme for a specific Generator in your
`config.yml`:

```groovy
// build.gradle
dependencies {
    orchidRuntime 'io.github.javaeden.orchid:OrchidAll:{{ site.version }}'
}
orchid {
    theme   = "BsDoc"
    baseUrl = "http://localhost:8080"
}
```

```yaml
# config.yml
wiki:
  theme: BsDoc
```

Your theme's documentation should tell you what the key for the theme is, or you can find it in the Orchid Admin Panel.

There are a number of {{anchor('officially-supported Orchid themes', 'themes')}} for you to choose from, or else 
services like [Jitpack](https://jitpack.io/) make it exceedingly simple for theme designers to publish new themes just 
by hosting the theme on Github, which makes it easier for you to get set up with community-developed themes.

{% alert 'info' :: compileAs('md') %}
Note: If you have developed a theme that you'd like to feature in our showcase, you can submit a pull request to the 
`docs` branch of the Orchid repository and it will be featured on this site.
{% endalert %}

## Configuring your Theme

Your themes are configured in your `config.yml`. Options can be set for all themes at the `theme` key, or for a specific 
theme at the key corresponding to its key. You can also break the theme options out into their own `config/` files, as 
described {{ anchor('here', 'Configuration') }}. For example, this current theme is `BsDoc`, and can be configured 
any of the following ways:

```yaml
# config.yml
theme:
  primaryColor: {{theme.primaryColor}}
```

```yaml
# config.yml
BsDoc:
  primaryColor: {{theme.primaryColor}}
```

```yaml
# config/theme.yml
primaryColor: {{theme.primaryColor}}
```

```yaml
# config/BsDoc.yml
primaryColor: {{theme.primaryColor}}
```

## Using Multiple Themes

Orchid seamlessly supports multiple themes within a single build, allowing you to pick the best theme for each section 
of your site. You even use your base theme with different options for different areas of your site if you'd like. 

Orchid currently supports setting custom themes for the pages from each generator. Simply set the name of the Theme 
you'd like to use in the generator's `theme` property in `config.yml`. The theme will use the normal theme options as 
outlines in the section above.

```yaml
# config.yml
wiki:
  theme: 'BsDoc'
``` 

Alternatively, you can set the `theme` property to an object with a property of `key` and a value of the theme's name.
This object is then used to configure the theme options for that specific theme as it is used by that generator's pages
when they are rendered.

```yaml
# config.yml
wiki:
  theme: 
    key: 'BsDoc'
    primaryColor: '#000000'
``` 
