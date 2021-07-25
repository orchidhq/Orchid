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

{{ buildCommandTabs(page, data.buildCommands.build) }}

## Serve

Running Orchid in `serve` mode will first build your site, but then also start a local webserver for you to easily 
preview it. In addition, it will watch your content directory for changes and rebuild with each change. And if you have
the {{anchor('orchid-plugin-docs-feature')}} plugin installed, you'll also be able to visit an admin panel at `/admin` and 
automatically get the most up-to-date documentation for your installed plugins delivered right to you!

**Example Usage**

{{ buildCommandTabs(page, data.buildCommands.serve) }}

## Deploy

Once you're happy with your site, it's time to take it live! The `deploy` task will build your site and then publish it 
to a variety of destinations, such as {{anchor('Netlify', 'orchid-netlify-feature')}} or {{anchor('GitHub Pages', 'orchid-github-feature')}}, but also has some other 
goodies like uploading release notes to {{anchor('GitHub Releases', 'orchid-github-feature')}}.

**Example Usage**

{{ buildCommandTabs(page, data.buildCommands.deploy) }}

## Run Task

In addition to the commands above, you can pass the name of an Orchid task directly through the CLI as an additional
`runTask` parameter. Available commands are `build`, `deploy`, and `serve`.

**Example Usage**

{{ buildCommandTabs(page, data.buildCommands.run) }}

{% macro buildCommandTabs(page, commands) %}
{% tabs :: compileAs('md') %}
{% gradle 'Gradle' %}
```
./gradlew {{ commands.gradle }}
```
{% endgradle %}
{% maven 'Maven' %}
```
./mvnw {{ commands.maven }}
```
{% endmaven %}
{% sbt 'SBT' %}
```
./sbtw {{ commands.sbt }}
```
{% endsbt %}
{% kscript 'kscript' %}
```
kscript ./orchid.kts {{ commands.kscript }}
```
{% endkscript %}
{% endtabs %}
{% endmacro %}
