---
---

## Orchid Taxonomies
---

Tag your pages with custom taxonomies and generate custom archives and landing pages for your taxonomy groups.

### Understanding Taxonomies

Orchid supports user-defined taxonomies in a manner inspired by 
[Hugo's custom taxonomies](https://gohugo.io/content-management/taxonomies/). Taxonomies define logical relationships
among various pages in your site, generating archives for these groups of pages to bring related content into one place.
The data that defines these logic groupings are often an intrinsic property of the page as defined by the Generator that
produced it, but taxonomies may also be "tagged" by hand to give you complete flexibility over your taxonomies. 

To understand how to use taxonomies, let's first look at a simple example you're probably already familiar and quite
comfortable with, blog categories and tags. 

In this example, we will consider the **category** and the **tag** as two different _taxonomies_, that is, blog posts 
can be logically grouped by both their categories and their tags. Specifically, as it relates to the OrchidPosts plugin,
blog posts can be assigned to a single category, which may have parent categories, and it may also be given any number
of tags that are unrelated to is category. This creates the logical association that a Post is in one category (which
is commonly included in the URL), and it has many tags, which is used to group posts across different categories. 

A user visiting your blog would expect to be able to view all the different categories on your site, whether they are 
included in your menu, or on a page of their own. The individual "categories" on your blog, such as "News", "Sports", 
or "Technology" would be considered the **terms** within the _categories_ **taxonomy**, and a **page** is assigned to 
exactly one **term**, or one category. Likewise, the _tags_ taxonomy can assign one or more  _tag_ **terms** to a 
_page_.

You could imagine custom taxonomies also being used for things like actors, directories, and generes being used to 
categorize movies (as described in the Hugo article), individual sections in your Wiki, or anything else. The 
OrchidTaxonomies allows you to create taxonomies of any kind, generating archives for all your taxonomies' terms, and 
the pages in each term.

To recap:

- **Taxonomy** - A logical categorization of related content
- **Term** - A specific, named value in the Taxonomy
- **Page** - An individual piece of content assigned to a Term

### Using Orchid Taxonomies

The OrchidTaxonomies plugin does not create any taxonomies by default, you must set them up yourself. This is done by
setting the taxonomies on the `taxonomies` property of the `taxonomies` generator options object in `config.yml`.

There are several different ways to set up the taxonomies configuration. (1) You can set the taxonomies as a String to 
use the taxonomy default values; (2) you can set each list item to be a map of config values, where the `key` property 
is the taxonomy type; (3) or you can set each list item to be a map with a single property that is the taxonomy type, 
and whose value is a map of configuration values. 

{% highlight 'yaml' %}
# Method (1), String as taxonomy types
taxonomies: 
  taxonomies:
    - 'categories'
    - 'tags'
{% endhighlight %}

{% highlight 'yaml' %}
# Method (2), Map with config values and `key` property as taxonomy type
taxonomies: 
  taxonomies:
    - key: 'categories'
      title: 'Blog Categories'
    - key: 'tags'
      title: 'Tags'
{% endhighlight %}

{% highlight 'yaml' %}
# Method (3), Map with only key as taxonomy type, and value as config values
taxonomies: 
  taxonomies:
    - categories: 
        title: 'Blog Categories'
    - tags:
        title: 'Tags'
{% endhighlight %}

Note that there is no difference between Method (2) and Method (3), it is simply a matter of preference.

### Configuring Taxonomy Terms

Individual Terms within a Taxonomy may also be customized. Terms are located from your content and are not set up in
`config.yml`. But for any term that _is_ found in your content, optional configurations may used for it by setting the
configuration as values of an object in its taxonomy configuration object. For example, if we have `categories` on our
site for "News" and "Sports", we could configure the individual terms with the following configuration in `config.yml`:

{% highlight 'yaml' %}
taxonomies: 
  taxonomies:
    - categories: 
        title: 'Blog Categories'
        news: 
          title: 'Local News'
        sports: 
          title: 'Sports'
{% endhighlight %}

### Assigning Pages to Terms

Taxonomies can either be set up as "single" or "multiple". In the case of categories and tags, the "tags" taxonomy is a 
**multiple** taxonomy type, since each Post can be given any number of tags. in contrast, the "categories" taxonomy is a
**singular** taxonomy type, since each Post can only be in one specific category (for the OrchidPosts plugin, you 
may use "categores" as a multiple taxonomy to include each post in the archives for its own category and for all its 
parent categories).

In the case of the OrchidPosts plugin, once the Taxonomy is set up in `config.yml` no further configuration is necessary
to generate the Post archives. The OrchidPosts plugin already does the work of assigning the Pages to Terms, and the 
OrchidTaxonomies plugin is able to use this configuration for itself (internally, it uses Reflection to call a getter on 
the Page, which returns the categories and tags). Other plugins may already be set up in their own manner as well, such 
as Wiki `sections` and Static Page `groups`, which makes it very easy to generate archives for any plugin you may be 
using. 

If you want to assign pages to Taxonomies that are not set up by the plugin, you can simply add the Term values in the
page's Front Matter. For "singular" taxonomies, the value must be a String. For "multiple" taxonomies, the value can be 
a String or a list of Strings.

{% highlight 'yaml' %}
---
categories: 'Sports'
tags:
  - 'Outdoor Sports'
  - 'Hiking'
  - 'Nature'
---
{% endhighlight %}

### Archive Ordering

The archive pages in each Taxonomy and each taxonomy's term can be customized to suit your needs. Both the Taxonomy and 
Term configuration objects support an `orderBy` property, along with `orderByDirection`. `orderBy` is a list of 
properties on the items that make up the archive, and the items will be sorted in the order of those properties. For 
example, if we order Posts by `[year, month, day`], posts will ordered primarily by year. If the years are equal, then 
they will be ordered by month, and the same for equal month ordering by day.

For Term ordering, within a Taxonomy archive, you may wish to create a configuration for each term with an `order` 
property, and then set `orderBy` to `order` to sort the Terms in their specific, defined order. Pages can be sorted on
any property, either set in their Front Matter or intrinsic to the Page, such as `year`, `month`, or `day` as described 
above (which are only available on Post pages), or anything else that is common to _all_ pages, such as `publishDate`
or `title`.

### Archive Permalinks

Both Taxonomy and Term archive pages can set a `permalink` in their configuration. Permalinks work the same as in other
plugins, where path pieces starting with `:` or surrounded by `{}` will be replaced with that page's specific value. 
OrchidTaxonomies adds support for several additional path types that should be included in these permalinks:

- `taxonomy` - the `key` of the taxonomy. Used in both Taxonomy and Term archive pages
- `term` - the `key` of the term within a specific taxonomy. Only used in Term archive pages
- `archiveIndex` - Archives are paginated (defaulting to 100 items/page), and this path type will set the index of the 
    archive page. The index of the first page in an archive is omitted for "prettier" URLs, and resumes with 2 on the
    second page (if items even need to be paginated).