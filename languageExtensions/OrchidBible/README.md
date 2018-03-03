---
---

## Orchid Bible
---

Load the full text of Bible verses quickly and easily.

### Adding Bible verses

Bible verses can be added simply by passing their reference to the `bible` function. The text will be loaded 

As a filter

{% highlight 'html' %}
{% verbatim %}
{{ "John 3:16"|bible("eng-KJV") }}
{% endverbatim %}
{% endhighlight %}

> {{ "John 3:16"|bible("eng-KJV") }}

As a function

{% highlight 'html' %}
{% verbatim %}
{{ bible("Galatians 2 19-21", "eng-NASB") }}
{% endverbatim %}
{% endhighlight %}

> {{ bible("Galatians 2 19-21", "eng-NASB") }}