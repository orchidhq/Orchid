---
---

Orchid has a well-defined order in which resources of any type are identified. This ordering sets Orchid up such that
plugins provide basic templates which can always be customized later by the chosen Theme to match the markup of the 
entire site. But any resource defined by a theme or a plugin can always be overridden by your local site, so you never
have to edit core theme files to tweak the output of your site.

## Resource Lookup Order
---

The exact order in which all resources are located is shown in the diagram below. A resource can be anything, including 
your templates and your content files, and each plugin or theme provides the resources bundled in its JarFile. 

![Resource Lookup Order]({{site.baseUrl}}/assets/media/resource-lookup-order.png "Resource Lookup Order")

### Local Resource Sources

Orchid ships with a single Local Resource Source, which locates resources as files within your `resourceDir`. This is 
typically `src/orchid/resources` within the normal Gradle project structure, but can be changed by setting the `srcDir`
in the `orchid` configuration block of your `build.gradle`.

Local resource sources are special in the fact that plugins typically index content based on local resources only.

While not currently implemented, Orchid supports having multiple Local resource sources in the case that you want to 
host local resources in multiple base directories or connect to a [Headless CMS](https://headlesscms.org/) which manages
your content remotely or add content from your own private APIs or CMS. See the Developer's Guide for more on 
implementing new resource sources.

### Theme Resource Sources

Themes are defined as being a Resource Source in themselves; that is, the Theme class directly provides Resources to 
Orchid from its JarFile. The resources that come from a theme are typically the final templates and assets that will be 
used in the rendered site, providing customization of those templates and assets from plugins which are usually fairly 
generic.

When resolving a resource that doesn't exist in a local source, only the current theme is considered. The "current" 
theme is usually the default theme set in `build.gradle` (in all cases except when rendering), or the theme that is set
as a custom theme for a generator in `config.yml`. 

All assets (CSS and JS only) provided by a theme are rendered in the output site with a "namespace" that allows multiple
instances of the same theme to each provide different versions of the same assets (e.g. stylesheets with different 
colors) without overwriting each other. This namespace is transparent to the theme picking which assets to add and which
ones are loaded on any given page, but the path of assets in the output site won't be the exact same as their input 
path. 

### Plugin Resource Sources

Plugins that contribute new things like Template Tags or Components must provide their own default templates so that 
they can always be dropped into any new theme and still work great. Components may also inject CSS or Javascript into a
page as well, which also comes from the plugin's resources. Keep in mind that _any_ resource provided by a plugin can
**_always_** be overridden by the theme or by your local resources, but without any customization plugins will still 
have everything they need to function perfectly. This makes it very easy to add new plugins to your site, as they 
usually require no setup at all to work.
