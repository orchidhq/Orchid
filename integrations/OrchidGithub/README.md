---
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

## Installation

{% include 'includes/dependencyTabs.peb' %} 

## Demo

- Run [GithubWikiAdapterTest](https://github.com/orchidhq/orchid/blob/dev/integrations/OrchidGithub/src/test/kotlin/com/eden/orchid/github/wiki/GithubWikiAdapterTest.kt) for demo

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

Custom domains are supported for Github Pages sites by adding a `CNAME` file to the site as it is deployed. Since 
0.19.0, Orchid will do this automatically based on your site's configured base URL if it does not end in `.github.io`. 
You will still need to configure DNS records with your domain registrar to point your domain to the Github Pages site, 
as described in [this help article](https://help.github.com/en/github/working-with-github-pages/managing-a-custom-domain-for-your-github-pages-site).

For versions earlier than 0.19.0, you will need to create the `CNAME` file automatically. As this file needs to be in 
the _deployed_ site's root, and not your repo root, this will require the {{ anchor('OrchidPages') }} plugin to be able
to create the necessary file. Create `pages/CNAME` file in your site resources (with no file extension) and add the 
following contents to it (replacing the domain with your own). Be sure that it's copied directly, with no extra newlines
at the top or end, otherwise Github will not read the file properly.

```txt
---
renderMode: raw
usePrettyUrl: false
---
www.example.com
```

### Github Releases Publisher

The `githubReleases` publisher will create a [release](https://help.github.com/en/categories/releases) on Github release
notes from the {{anchor('OrchidChangelog')}} plugin's latest changelog version.

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
