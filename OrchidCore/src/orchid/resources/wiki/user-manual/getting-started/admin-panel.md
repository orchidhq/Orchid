---
pluginName: Orchid Plugin Docs
contentTypeTitle: admin panel
bundles:
  - Orchid All
---

{% extends '_wikiBase' %}

{% block sectionIntro %}

The Orchid Admin panel provides a great way to view everything that _can_ be done with your current site, and helping 
you set up your site most effectively. Since Orchid is self-documenting by its very nature, it is able to inspect your 
current plugins and themes and generate a reference of all the available generators, components, etc. that you can use 
and all the options available on them. And since it is self documenting, everything you see in the Admin Panel is 
guaranteed to be the most up-to-date info available for your current plugin versions, so you never again have to trust 
that the plugin developers are keeping their documentation relevant over time.

## Using the Admin Panel
---

With your Orchid site running locally in `serve` mode and the {{anchor('Orchid Plugin Docs')}} plugin installed, all you 
have to do is visit `http://localhost:8080/admin` in your browser to access the Admin Panel. 

Click the "hamburger" icon on the top left to open a sidebar with a list of the most common components of your site that 
you will be configuring. In addition, you can view a list of anything that could possible be extended in Orchid, to give
you a sense of what you could customize with plugins. 

![Admin panel sidebar]({{site.baseUrl}}/assets/media/admin-sidebar.png)

Clicking on one of these items takes you to a page with its documentation automatically generated and displayed for easy
use. Below is an example for the Pages plugin's generator, which is responsible for rendering content from the `pages/`
directory into pages in your site.

You'll notice it has a sample YAML configuration for this generator. This shows the all the possible configuration 
options, which are guaranteed to match the version of the plugin you're using with no upkeep from the plugin creator, 
and the default values for those options. 

![Admin panel generator]({{site.baseUrl}}/assets/media/admin-pages.png) 

The "Manage" button on the top right opens a panel with some relevant content about your site, and a command box. 

![Admin panel manage panel]({{site.baseUrl}}/assets/media/admin-manage.png)

The header will also indicate indexing and generation progress during a build.

![Admin panel progress]({{site.baseUrl}}/assets/media/admin-progress.png)

If you have the {{anchor('Orchid Netlify CMS')}} plugin installed, it will be embedded directly within the admin panel.

![Admin panel progress]({{site.baseUrl}}/assets/media/admin-netlify-cms.png)

{% endblock %}