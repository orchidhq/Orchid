---
from: docs.plugin_index
description: Connect your Orchid site to GitHub services.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300/v1558903744/plugins/github.png
    alt: GitHub
    caption: GitHub
tags:
    - wiki
    - publication
---

## About

OrchidGithub connects Orchid to Github's services, including integrating 
[wikis](https://help.github.com/en/articles/documenting-your-project-with-wikis), 
creating [releases](https://help.github.com/en/categories/releases), and publishing your site directly to 
[Github Pages](https://pages.github.com/). 

## Demo

- Run [GithubWikiAdapterTest](https://github.com/JavaEden/Orchid/blob/dev/integrations/OrchidGithub/src/test/kotlin/com/eden/orchid/github/wiki/GithubWikiAdapterTest.kt) for demo

## Usage

### Wiki Adapter

OrchidGithub comes with a `github` Wiki Adapter, to integrate a project wiki as an Orchid wiki section. This will
clone the wiki repository and convert its contents to an Orchid wiki automatically. If a `_Sidebar` file exists in the
wiki, pages will be ordered according to the order of links in that sidebar file. Otherwise, pages will be ordered 
alphabetically, with a summary page generated listing all pages in order.

```yaml
# config.yml
wiki: 
  sections:
    userManual:
      adapter: 
        type: "github"
        repo: "[username/repo]" # the wiki attached to this project will be used
```

### Github Pages Publisher

The `githubPages` publisher will take your rendered site and publish it to a static website on 
[Github Pages](https://pages.github.com/). You must have a personal access token set for publication to work, see 
[Configuration](#configuration) below.

```yaml
# config.yml
services:
  publications:
    stages:
      - type: 'githubPages'
        username: '[username]' # the username which created the personal access token
        repo: '[username/repo]' # the repo to publish to
```

### Github Releases Publisher

The `githubReleases` publisher will create a [release](https://help.github.com/en/categories/releases) on Github release
notes from the {{anchor('Orchid Changelog')}} plugin's latest changelog version.

```yaml
# config.yml
services:
  publications:
    stages:
      - type: 'githubReleases'
        repo: '[username/repo]' # the repo to create a release on
```

### Configuration

You must generate a
[personal access token](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line) from 
Github to successfully authenticate and allow Orchid to perform actions on your behalf, like publishing a site to
Github Pages. This can be set as the `githubToken` environment variable, or passed to Orchid from Gradle.

```groovy
// build.gradle
orchid {
    githubToken = project.property("githubToken")
}
```

Note that this token grants full access to your account, and should be treated like any normal password. Never check it 
in to your repository.
