---
---

## Orchid Writers' Blocks
---

A collection of Template Tags and Components that help you get past the writer's block and make building your site a 
dream.

### About Orchid Writers' Blocks

This plugin adds a collection of useful tags and functions for Pebble to make writing content simpler. See what's 
included below:

#### Tags

**Alert**

Wrap content in a callout or bootstrap-style alert.

{% highlight 'jinja' %}
{% verbatim %}
{% alert 'warning' %}
    This is your final warning!
{% endalert %}
{% endverbatim %}
{% endhighlight %}


**Github Gist**

Embed a Github Gist.

{% highlight 'jinja' %}
{% verbatim %}
{% gist 'gistId' %}
{% endverbatim %}
{% endhighlight %}


**Instagram**

Embed a photo from Instagram.

{% highlight 'jinja' %}
{% verbatim %}
{% instagram 'username' 'postId' %}
{% endverbatim %}
{% endhighlight %}


**Twitter**

Embed a Twitter post, collection, or timeline.

{% highlight 'jinja' %}
{% verbatim %}
{% twitter 'username' %}
or
{% twitter 'username' 'tweetId' %}
{% endverbatim %}
{% endhighlight %}


**Youtube**

Embed a Youtube video.

{% highlight 'jinja' %}
{% verbatim %}
{% youtube 'videoId' %}
{% endverbatim %}
{% endhighlight %}


#### Functions

**Encode Spaces**

Converts spaces to HTML-encoded non-breaking spaces to preserve spacing.

{% highlight 'jinja' %}
{% verbatim %}
{{ encodeSpaces('text to encode') }}
{% endverbatim %}
{% endhighlight %}


**Newline to BR**

Converts newlines to BR tags to force HTML line breaks.

{% highlight 'jinja' %}
{% verbatim %}
{{ nl2br('text to encode') }}
{% endverbatim %}
{% endhighlight %}


**Pluralize**

Attempts to convert a word to its English plural form.

{% highlight 'jinja' %}
{% verbatim %}
{{ pluralize('word', 2) }}
{% endverbatim %}
{% endhighlight %}