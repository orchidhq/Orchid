---
official: true
description: No configuration, fully-featured Netlify CMS for Orchid.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974475/plugins/netlifycms.jpg
    alt: Netlify CMS
    caption: Screenshot of Netlify CMS
menu:
  - type: 'page'
    itemId: 'Orchid Netlify CMS'
  - type: 'pageChildren'
    itemId: 'Orchid Netlify CMS'
    asSubmenu: true
    submenuTitle: Docs
---

### Using Netlify CMS with Orchid

The Orchid Netlify CMS plugin adds everything necessary to get the [Netlify CMS](https://www.netlifycms.org/) added to
your Orchid site so you can manage all your content with an beautiful and easy-to-use web interface. This plugin
generated the Netlify CMS' required `config.yml` (not to be confused with Orchid's `config.yml`), and also created 
widgets for the Orchid-specific content types like Components, Menus, and Template Tags.

This plugin can be used with any netlify CMS backend you like, but when Orchid is running in `serve` mode, it will 
configure itself to be the backend. This allows Orchid's embedded server to serve the content to Netlify CMS for 
development and local content editing. No configuration is necessary to use Orchid as the Netlify CMS backend, it is 
automatically set up for you when running `serve`, and your configured backend is then used when your site is generated
for production.

The _only_ configuration necessary for you to add the Netlify CMS to your Orchid site, is to configure your backend. You
may also want to change the `resourceRoot` and `mediaFolder`, which must be configured relative to your Git repository
root, but if you are using the default Gradle plugin settings and your Orchid site isn't a Gradle multi-project, then 
the defaults will already be set up correctly for you.