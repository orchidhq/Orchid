---
---

## Orchid Bible
---

Load the full text of Bible verses quickly and easily.

### Adding Bible verses

Bible verses can be added simply by passing their reference to the `bible` function. The verse text will be downloaded 
and displayed on the page, along with the verse reference. You will need to sign up for an API key for the  
[Bibles.org API](http://www.bibles.org/pages/api) to download verse text, and the Bible version must be the `id` of one
of the [available versions on Bibles.org](http://www.bibles.org/versions_api).

As a filter

{% highlight 'html' %}
{% verbatim %}
{{ "John 3:16"|bible("eng-KJV") }}
{% endverbatim %}
{% endhighlight %}

As a function

{% highlight 'html' %}
{% verbatim %}
{{ bible("Galatians 2 19-21", "eng-NASB") }}
{% endverbatim %}
{% endhighlight %}
