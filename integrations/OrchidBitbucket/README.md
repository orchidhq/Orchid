---
description: Connect your Orchid site to Bitbucket services.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300/v1558903947/plugins/bitbucket.png
    alt: Azure
    caption: Azure
tags:
    - wiki
    - publication
---

## About

OrchidBitbucket connects Orchid to Bitbucket's Cloud services, including integrating 
[Wikis](https://confluence.atlassian.com/bitbucket/wikis-221449748.html) and publishing your site directly to 
[Bitbucket Cloud](https://confluence.atlassian.com/bitbucket/publishing-a-website-on-bitbucket-cloud-221449776.html).

## Demo

This plugin is currently still in progress and not all features are available with a demo yet. Please check back later.

## Usage

### Wiki Adapter

> _This feature is still in progress and is not yet available_

OrchidBitbucket comes with a `bitbucket` Wiki Adapter, to integrate a project wiki as an Orchid wiki section. This will
clone the wiki repository and convert its contents to an Orchid wiki automatically. Pages will be ordered 
alphabetically, with the "Home" page becoming the wiki summary page.

```yaml
# config.yml
wiki: 
  sections:
    userManual:
      adapter: 
        type: "bitbucket"
        repo: "[Bitbucket wiki repository URL]"
```

### Bitbucket Cloud Publisher

The `bitbucketCloud` publisher will take your rendered site and publish it to a static website on 
[Bitbucket Cloud](https://confluence.atlassian.com/bitbucket/publishing-a-website-on-bitbucket-cloud-221449776.html) for
your workspace. You must have an app password set for publication to work, see [Configuration](#configuration) below.

```yaml
# config.yml
services:
  publications:
    stages:
      - type: 'bitbucketCloud'
        username: '[username]' # the username which created the app password
        repo: '[repo name]' # such as [workspaceID].bitbucket.io
```

### Configuration

You must generate an [App password](https://confluence.atlassian.com/bitbucket/app-passwords-828781300.html) from 
Bitbucket to successfully authenticate and allow Orchid to perform actions on your behalf, like publishing a site to
Bitbucket Cloud. This can be set as the `bitbucketToken` environment variable, or passed to Orchid from Gradle.

```groovy
// build.gradle
orchid {
    bitbucketToken = project.property("bitbucketToken")
}
```

Note that this token grants full access to your account, and should be treated like any normal password. Never check it 
in to your repository.
