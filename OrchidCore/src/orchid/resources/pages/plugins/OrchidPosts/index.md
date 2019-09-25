---
from: docs.plugin_index
description: Add Jekyll-like blogging functionality to your Orchid site.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973072/plugins/posts.jpg
    alt: Posts
    caption: Photo by Emma Matthews on Unsplash
tags:
    - content
    - blog
---

## About

Add Jekyll-like blogging functionality to your Orchid site, with dated blog posts and RSS/Atom feeds.

This plugin only creates the blog posts themselves. To generate archives for your blog posts' tags and categories, 
check out the {{ anchor('Orchid Taxonomies') }} plugin, and refer to the [OrchidStarter repo](https://github.com/JavaEden/OrchidStarter/blob/master/src/orchid/resources/config/taxonomies.yml)
for basic taxonomies setup.

## Demo

- Try the [starter app](https://github.com/JavaEden/OrchidStarter)
- Run [PostsGeneratorTest](https://github.com/JavaEden/Orchid/blob/dev/plugins/OrchidPosts/src/test/kotlin/com/eden/orchid/posts/PostsGeneratorTest.kt) for demo

## Usage

### Basic Usage

Orchid supports blogging in a manner similar to Jekyll, but made to be much easier to manage for large blogs. Blog posts
all have an intrinsic publish date, and can be grouped into hierarchical categories. Blog posts are found as files in 
`posts/` where the filename matches the pattern of `YYYY-MM-DD-post-slug`. The file type can be anything that Orchid can
process, and the date in the filename is automatically set as the post's `publishDate` and will not be rendered in the 
output site until after that date. 

```text
. / (resources root)
├── homepage.md
├── config.yml
└── posts/
    └── 2019-01-01-blog-post-one.md <-- compiled as Markdown to /2019/1/1/blog-post-one
```

You may also group your posts by year, month, and day in folders, instead of requiring them all to be in the filename. 
For example, instead of having a post from a file at `posts/2018-01-01-post-one.md`, you could instead group your posts
by year (`posts/2018/01-01-post-one.md`), by year and month (`posts/2018/01/01-post-one.md`), or by year, month, 
and day (`posts/2018/01/01/post-one.md`). This is completely optional, and you can mix-and-match individual posts into
these formats as needed for better organization.

```text
. / (resources root)
├── homepage.md
├── config.yml
└── posts/
    ├── 2018/ <-- group posts by year
    |   └── 01 <-- group posts by month 
    |       └── 02-blog-post-two.md <-- filename includes day, lives at /2018/1/2/blog-post-two
    └── 2017/ <-- group posts by year
        └── 02-03-blog-post-three.md <-- filename includes month and day, lives at /2017/2/3/blog-post-three
```

### Post Title

By default, the title of the blog post is given as the `post-slug` part of the filename, converted to a human-readable,
capitalized title, unless a `title` is set in the post's Front Matter.

```md
// posts/2018-01-01-post-one.md <-- Post title is "Post One"
---
---
```

```md
// posts/2018-01-01-post-one.md
---
title: First Blog Post <-- Post title is "First Blog Post", not "Post One"
---
```

### Permalinks

Posts can customize their permalink by setting the `permalink` property in their Front Matter. The `permalink` takes a 
string with certain path segments set up as dynamic parts, such as `blog/:year/:month/:slug`. Any path segment which 
matches the pattern of `:key` or `{key}` will attempt to fill that segment with some dynamic data, such as the post's 
published year, month, or date, its category, or any variable set in its Front Matter.

```md
// posts/2018-01-01-post-one.md
---
title: First Blog Post
permalink: 'blog/:year/:slug' <-- permalink at /blog/2018/post-one
---
```

### Using Categories

Categories must be set in your `config.yml`, and posts will only be added if they are in the path within `posts/`
corresponding to one of these configured categories. Categories may be nested inside a parent category, but it must 
build a complete hierarchy, where every parent category is also listed as its own category.

```yaml
posts:
  baseDir: 'blog'  # (1) 
  categories:
    - 'personal'  # (2)
    - 'programming'  # (3)
    - 'programming/android'  # (4)
    - 'programming/web'  # (5)
```

1) Looks for the blog posts in `blog/` instead of `posts/`
2) Creates a category in `{baseDir}/personal`, and every post in that folder will be in the `personal` category
3) Creates a category in `{baseDir}/programming`, and every post in that folder will be in the `programming` category
4) Creates a category in `{baseDir}/programming/android`, and every post in that folder will be in the `android` primary
    category, but will also be in the `programming` category since it is a parent category of `android`. 
5) Creates a category in `{baseDir}/programming/web`, and every post in that folder will be in the `web` primary
    category, but will also be in the `programming` category since it is a parent category of `web`. 
    
**Note:** if no categories are given, all posts in the `{baseDir}` are simply considered blog posts without any 
category, and is just a generic "blog". If you _do_ specify categories, then all posts must be in one of the defined 
categories, otherwise they will be ignored; there is no concept of 'uncategorized' posts unless you choose to create a 
category called 'uncategorized' and put uncategorized posts there. 

### Customizing Categories

Using the configuration block shown above will set up your categories with all default values, but it is likely that you
will want to change certain features of the posts in a category based on which category they are in. Currently, 
Categories can customize their own title to be different from their path, and they can set the permalink structure to be
the same for all posts in that category. 

There are several different ways to set up the category configuration. (1) You can set the category as a String to use
default values, as shown above; (2) you can set each list item to be a map of config values, where the `key` property is 
the category path; (3) or you can set each list item to be a map with a single property that is the category path, and 
whose value is a map of configuration values. 

```yaml
# Method (1), String as category path
posts: 
  categories:
    - 'personal'
    - 'programming'
```

```yaml
# Method (2), Map with config values and `key` property as category path
posts: 
  categories:
    - key: 'personal'
      title: 'Personal Blog'
    - key: 'programming'
      title: 'programming Blog'
```

```yaml
# Method (3), Map with only key as category path, and value as config values
posts: 
  categories:
    - personal: 
        title: 'Personal Blog'
    - programming:
        title: 'programming Blog'
```

Note that there is no difference between Method (2) and Method (3), it is simply a matter of preference.

### Post Authors

Blog posts can have authors assigned to them. Orchid Posts generates Author pages for known authors, but does allow for 
"guest" authors to be defined for a single post. Both known and guest authors accept the same configuration object, but
where that object is defined is different for each.

#### Known Authors

Known authors can be set just in the `config.yml` under the `posts.authors` key. These authors will have default pages 
generated for them, and can be linked to directly from the blog post. You can also create a content file under the 
`posts/authors/` directory to customize the content of that page, such as to show a bio for the author. In this case, 
the same configuration values can be set in the page's Front Matter.

From any post's Front Matter, you may set the `author` property to the `name` of one of these known authors, and that 
Author will then be linked to the post. 

#### Guest Authors

Guest authors may be set by setting a post's `author` property to a map of Author config values, instead of as the name
of a known author. This will display the same information for the author as for a known author, but they will not have
a landing page generated to link to. 

### Post Comments

OrchidPosts supports comments via Disqus with the `disqus`. Using Archetypes, it is quite simple to set up all
your blog posts to have a comments section with the `disqus` component without having to manually add the component to 
each page.

### RSS and Atom Feeds

OrchidPosts will generate feeds for your content automatically, in both the RSS and Atom formats. You can customize the 
feeds from the `feeds` generator object in your `config.yml`, such as how many entries to include in the feed, or which 
generators to include pages from (defaults to just the `posts` generator). Pages included in the feed are sorted by 
their `publishDate`, and the feed content comes from the `feeds/rss.peb` and `feeds/atom.peb` pages, which can be 
overridden and customized if needed.
