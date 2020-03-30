---
description: 'Using multiple themes, you can pick the right theme for different parts of your site.'
---

Themes are what create structure for your site and make it unique. At its core, an Orchid theme contributes CSS and
Javascript assets to the pages using that theme, and it also helps decide the templates that are used to lay out 
your pages. The templates and assets added by your theme can always be overridden in your local build, but it is never
necessary to know anything about the theme templates for it to work well with Orchid. 

## Picking a Theme

Themes in Orchid are added just as a dependency in your `build.gradle`, but they are not actually used in your site 
unless you set the `theme` property in `config.yml` :

```groovy
// build.gradle
dependencies {
    orchidRuntime 'io.github.javaeden.orchid:OrchidEditorial:{{ site.version }}'
}
```

```yaml
# config.yml
site:
  theme: Editorial
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

Your themes are configured in your `config.yml`, using values from `theme`, or from the 
{{ anchor('config archetype', 'Configuration') }} at the theme's key. For example, this current theme is `Editorial`, and
can be configured any of the following ways:



```yaml
# config.yml
theme:
  primaryColor: {{theme.primaryColor}}
```

```yaml
# config.yml
Editorial:
  primaryColor: {{theme.primaryColor}}
```

```yaml
# config/theme.yml
primaryColor: {{theme.primaryColor}}
```

```yaml
# config/Editorial.yml
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
  theme: 'Editorial'
``` 

Alternatively, you can set the `theme` property to an object with a property of `key` and a value of the theme's name.
This object is then used to configure the theme options for that specific theme as it is used by that generator's pages
when they are rendered.

```yaml
# config.yml
wiki:
  theme: 
    key: 'Editorial'
    primaryColor: '#000000'
``` 

## Custom Themes

See more about creating custom themes {{ anchor('here', 'Custom Themes') }}.
