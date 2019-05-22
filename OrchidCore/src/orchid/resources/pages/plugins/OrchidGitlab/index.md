---
from: docs.plugin_index
tags:
    - wiki
    - publication
---

## About

OrchidGitlab connects Orchid to Gitlab's services, including integrating 
[wikis](https://docs.gitlab.com/ee/user/project/wiki/) and publishing your site directly to 
[Gitlab Pages](https://about.gitlab.com/product/pages/). 

## Demo

- Run [GitlabWikiAdapterTest](https://github.com/JavaEden/Orchid/blob/dev/integrations/OrchidGitlab/src/test/kotlin/com/eden/orchid/gitlab/wiki/GitlabWikiAdapterTest.kt) for demo

## Usage

### Wiki Adapter

OrchidGitlab comes with a `gitlab` Wiki Adapter, to integrate a project wiki as an Orchid wiki section. This will
clone the wiki repository and convert its contents to an Orchid wiki automatically. If a `_Sidebar` file exists in the
wiki, pages will be ordered according to the order of links in that sidebar file. Otherwise, pages will be ordered 
alphabetically, with a summary page generated listing all pages in order.

```yaml
# config.yml
wiki: 
  sections:
    userManual:
      adapter: 
        type: "gitlab"
        repo: "[username/repo]" # the wiki attached to this project will be used
```

### Gitlab Pages Publisher

The `gitlabPages` publisher will take your rendered site and publish it to a static website on 
[Gitlab Pages](https://about.gitlab.com/product/pages/). You must have a personal access token set for publication to work, see 
[Configuration](#configuration) below.

```yaml
# config.yml
services:
  publications:
    stages:
      - type: 'gitlabPages'
        username: '[username]' # the username which created the personal access token
        repo: '[username/repo]' # the repo to publish to
```

### Configuration

You must generate an 
[personal access token](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html) from Gitlab to successfully 
authenticate and allow Orchid to perform actions on your behalf, like publishing a site to Gitlab Pages. This can be set
as the `gitlabToken` environment variable, or passed to Orchid from Gradle.

```groovy
// build.gradle
orchid {
    gitlabToken = project.property("gitlabToken")
}
```

Note that this token grants full access to your account, and should be treated like any normal password. Never check it 
in to your repository.
