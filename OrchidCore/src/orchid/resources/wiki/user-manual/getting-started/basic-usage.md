---
description: 'Learn the basic commands to build and deploy your Orchid static site'
---

Orchid is a **static site generator** with a focus on building really great documentation sites. Its main job is to take
content from a variety of sources (such as Markdown or comments in your project's code), allow a series of plugins to 
transform that content into pages, and then render those pages to files. From there, you are free to deploy these files
to any webserver you'd like, and Orchid can help you with that too! 

Whether you run Orchid from Gradle, Maven, or a kscript, you'll have the following tasks available to you:

## Build

A `build` in Orchid will take your content and configurations, and generate a site in your configured output directory.
Simple as that.

**Example Usage**

{% tabs :: compileAs('md') %}
{% gradle 'Gradle' %}
```
./gradle orchidBuild
```
{% endgradle %}
{% maven 'Maven' %}
```
./mvnw orchid:build
```
{% endmaven %}
{% kscript 'kscript' %}
```
kscript ./orchid.kts build
```
{% endkscript %}
{% endtabs %}

## Serve

Running Orchid in `serve` mode will first build your site, but then also start a local webserver for you to easily 
preview it. In addition, it will watch your content directory for changes and rebuild with each change. And if you have
the {{anchor('Orchid Plugin Docs')}} plugin installed, you'll also be able to visit an admin panel at `/admin` and 
automatically get the most up-to-date documentation for your installed plugins delivered right to you!

**Example Usage**

{% tabs :: compileAs('md') %}
{% gradle 'Gradle' %}
```
./gradle orchidServe
```
{% endgradle %}
{% maven 'Maven' %}
```
./mvnw orchid:serve
```
{% endmaven %}
{% kscript 'kscript' %}
```
kscript ./orchid.kts serve
```
{% endkscript %}
{% endtabs %}

## Deploy

Once you're happy with your site, it's time to take it live! The `deploy` task will build your site and then publish it 
to a variety of destinations, such as {{anchor('Netlify')}} or {{anchor('GitHub Pages')}}, but also has some other 
goodies like uploading release notes to {{anchor('GitHub Releases')}}.

**Example Usage**

{% tabs :: compileAs('md') %}
{% gradle 'Gradle' %}
```
./gradle orchidDeploy
```
{% endgradle %}
{% maven 'Maven' %}
```
./mvnw orchid:deploy
```
{% endmaven %}
{% kscript 'kscript' %}
```
kscript ./orchid.kts deploy
```
{% endkscript %}
{% endtabs %}
