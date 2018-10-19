---
official: true
noDocs: true
description: Load the full text of Bible verses quickly and easily.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973072/plugins/bible.jpg
    alt: Bible
    caption: Photo by Priscilla Du Preez on Unsplash
menu:
  - type: 'page'
    itemId: 'Orchid Bible'
  - type: 'pageChildren'
    itemId: 'Orchid Bible'
    asSubmenu: true
    submenuTitle: Docs
---

### Adding Bible verses

Bible verses can be added simply by passing their reference to the `bible` function. The verse text will be downloaded 
and displayed on the page, along with the verse reference. You will need to sign up for an API key for the  
[Bibles.org API](https://www.bibles.org/pages/api) to download verse text, and the Bible version must be the `id` of one
of the [available versions on Bibles.org](https://www.bibles.org/versions_api).

As a filter

{% highlight 'jinja' %}
{% verbatim %}
{{ "John 3:16"|bible("eng-KJV") }}
{% endverbatim %}
{% endhighlight %}

{% if site.isProduction() %}> {{ "John 3:16"|bible("eng-KJV") }}{% endif %}

As a function

{% highlight 'jinja' %}
{% verbatim %}
{{ bible("Galatians 2 19-21", "eng-NASB") }}
{% endverbatim %}
{% endhighlight %}

{% if site.isProduction() %}> {{ bible("Galatians 2 19-21", "eng-NASB") }}{% endif %}