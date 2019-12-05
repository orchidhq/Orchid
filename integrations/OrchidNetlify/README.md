---
description: Connect your Orchid site to Netlify services.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300/v1558903553/plugins/netlify.png
    alt: Netlify
    caption: Netlify
tags:
    - publication
---

## About

OrchidNetlify connects your Orchid site to Netlify's platform, enabling you to publish your site to Netlify without 
needing to use the Netlify's CI build platform. 

## Demo

No demo available.

## Usage

### Netlify Publisher

Netlify's CI platform is really great and easy to set up, but there may be situations where you cannot use their CI
infrastructure but still wish to host your site on Netlify, such as a build that takes longer than 15 minutes, or 
requires tooling not supported by Linux (like Swift documentation).

In this case, you can use the `netlify` publisher to upload your site to Netlify after a successful Orchid build. You 
must have a personal access token set for publication to work, see [Configuration](#configuration) below.

```yaml
# config.yml
services:
  publications:
    stages:
      - type: 'netify'
        siteId: '[site name].netlify.com' # the Netlify sitename. If your site base URL is a Netlify URL, this is optional
```

Currently, only the site is deployed to Netlify, Functions are not supported by Orchid.

### Configuration

You must generate a
[personal access token](https://www.netlify.com/docs/cli/#obtain-a-token-in-the-netlify-ui) from Netlify to successfully 
authenticate and allow Orchid to perform actions on your behalf, like publishing a site to Netlify. This can be set as 
the `netlifyToken` environment variable, or passed to Orchid from Gradle.

```groovy
// build.gradle
orchid {
    netlifyToken = project.property("netlifyToken")
}
```

Note that this token grants full access to your account, and should be treated like any normal password. Never check it 
in to your repository.
