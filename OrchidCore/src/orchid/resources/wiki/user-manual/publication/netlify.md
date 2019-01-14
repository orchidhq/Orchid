---
description: Upload your site to Netlify after building, for really large sites that can't be built on Netlify's CI platform.
---

## Description

The `netlify` Publisher allows you to upload your site to Netlify.

To use the `netlify` publisher, you'll need to provide Orchid with a `netlifyToken` containing a Personal Access Token
from Netlify. Since PATs are confidential and allow anyone who has it complete access to your account, you should set 
this as an environment variable and add it to your Gradle orchid config from that variable rather than committing it to
source control.

{% highlight 'groovy' %}
orchid {
    ...
    args = ["netlifyToken", "${System.getenv('NETLIFY_TOKEN')}"]
}
{% endhighlight %}

After your PAT is set up, you'll need to create a new site on Netlify, and set the domain as the `siteId` in your 
publisher config. And that's it, Orchid will do the hard work of authenticating with Netlify and determining which files
to upload.

## Example Usage

{% highlight 'yaml' %}
services:
  publications: 
    stages: 
      - type: netlify
        siteId: 'orchid.netlify.com'
{% endhighlight %}

## API Documentation

{% docs className='com.eden.orchid.impl.publication.NetlifyPublisher' tableClass='table' tableLeaderClass='hidden' %}
