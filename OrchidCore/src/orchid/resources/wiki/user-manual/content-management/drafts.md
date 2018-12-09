---
description: Drafts allow you to work on pages with the confidence that they won't be published. You can even set publish and expiry dates for ephemeral pages.
---

Drafts are pages that are not yet ready to be published or were intended to be accessible only for a limited time and
are now expired. Pages that are drafts will not be rendered or be discoverable by other plugins from the index. 

Any page can become a draft, and there are multiple ways to make a draft from any given page. The easiest way is just to
set the page as a draft in its Front Matter:

{% highlight 'yaml' %}
---
...
draft: true
---
{% endhighlight %}


You can also set a `publishDate` or an `expiryDate` in the pages Front Matter, which both take a date in the ISO-8601 
date format (`YYYY-MM-DD`) or datetime format (`YYYY-MM-DDThh:mm:ss`):

{% highlight 'yaml' %}
---
...
publishDate: '2018-03-01' # considered a draft until March 1st, 2018
expiryDate: '2018-04-01' # considered a draft after April 1st, 2018
---
{% endhighlight %}


Some plugins may set the page's publish or expiry date themselves based on some external criteria. An example is the 
Posts plugin, where posts include their publish date in the filename, instead of finding it in the post's Front Matter.

In some situations, you may wish to view your drafts during development. To do this, simply set the `includeDrafts` 
option to `true` on the `render` service in your `config.yml`

{% highlight 'yaml' %}
...
services:
  render:
    includeDrafts: true
{% endhighlight %}
