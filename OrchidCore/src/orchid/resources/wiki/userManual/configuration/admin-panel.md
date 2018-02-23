---
pluginName: OrchidPluginDocs
contentTypeTitle: admin panel
bundles:
  - OrchidAll
---

{% extends '_wikiBase_contentTypes' %}

{% block sectionIntro %}
The Orchid Admin panel provides a great way to view everything that _can_ be done with your current site, and helping 
you set up your site most effectively. Since Orchid is self-documenting by its very nature, it is able to inspect your 
current plugins and themes and generate a reference of all the available generators, components, etc. that you can use 
and all the options available on them. And since it is self documenting, everything you see in the Admin Panel is 
guaranteed to be the most up-to-date info available for your current plugin versions, so you never again have to trust 
that the plugin developers are keeping their documentation relevant over time.

{{parent()}} 
{% endblock %}

{% block sectionBody %}
## Using the Admin Panel
---

With your Orchid site running locally in `serve` mode (`./gradlew orchidServe`) and the OrchidPluginDocs plugin 
installed, all you have to do is visit `http://localhost:8080/admin` in your browser to access the Admin Panel. On the 
left you'll see a list of the most common components of your site that you will be configuring, under the 
`Configuration` menu item. 

If you click the hamburger icon on the right, you will open up the full list of everything in Orchid that can possibly 
be extended, along with the classes that are currently registered for those types. You can switch tabs in that side 
panel to view all the events that are being sent within Orchid, so you can inspect places in the Orchid lifecycle that 
you could run your code. Most importantly with these displayed events is some basic info showing the progress of a build
and how long it took to render each page. A progress bar is also displayed near the top of the screen showing progress
for the build as well. 

On the bottom of the screen is a command bar, where you can enter commands that Orchid will respond to. Most commonly, 
you might want to run the `build` command to force a rebuild of your site, but plugins may add additional commands to do 
things like generating boilerplate content files or anything else.

## Effectively Using the Admin Panel 
---

The admin panel is currently designed to be a generated reference manual for you to see the options available to your 
site. It is certainly possible that plugins may eventually turn the Admin Panel into a full control center for managing
all your content and all site options, but for now it is best left as a generated reference. If you want to write 
content with a WYSIWYG interface, you should check out the 
[Netlify CMS plugin]({{ link('OrchidNetlifyCMS', 'staticPages-plugins') }}).

The classes that have options that can be set in your `config.yml` or a page's Front Matter will show a code snippet on
their Admin Panel page, demonstrating their unique usage. There will also be tabs showing the full description of all 
the options that are special for that class, as well as the options inherited by all objects of that type. For example, 
all Components have an option for `templates` where you can set a custom template to use for the component, which is 
shown in the "Inherited Options" tab. The FormComponent has a special option field named `form`, and its description is
shown in the "Own Options" tab, as well as showing up on the "Overview" tab as a code snippet.


{% endblock %}