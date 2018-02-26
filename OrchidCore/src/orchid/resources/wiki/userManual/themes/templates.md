---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
Templates in Orchid all live within your resources' `templates/` directory, regardless of whether it comes from local 
resources, plugins, or themes. Anytime a layout is requested or another template is included/extended from Pebble, the
path is referenced relative to `templates/`, even if it is not specified. It is not necessary to specify the extension
of template files, either, as Orchid will infer the exact extension of a template based on what language the theme 
prefers.

In many cases, Orchid is pretty flexible with the exact template it uses, and in other cases it is pretty strict. This
page will describe the general rules that define which templates are used in which circumstances. 
{% endblock %}

{% block sectionBody %}
## Layouts
---

When a Page is rendered with a Layout (i.e. not raw or as binary content), Orchid will locate the desired layout and 
inject the Page Content and Components into that layout. Layouts are exclusively set by you and not managed internally
by Orchid, giving you complete flexibility to set your page's in whatever context looks best. Unlike many other CMSs and
SSGs, the Layout does not directly control how the specific content for the page looks, it just controls the "chrome" 
around that content, or the sidebars, menus, etc. that make up the full markup of the page. 

That being said, Orchid uses **Page Templates** to capture the kind of "layout" functionality you'd expect from other 
tools, see the next section for more on Page Templates. This separation between the markup of a particular page type and
the layout it is embedded in is one of the most powerful features of Orchid's themeing capabilities, which helps keep
theme's small but flexible.

When a Layout is requested, Orchid checks the following patterns. The first pattern that matches a resource is then 
chosen as the Layout template. The patterns used to find the layout are below. The patterns use the following dynamic 
values in their lookup path:

- `{template}` is the exact value of `layout` in the page's Front Matter 
- `{themePreferredExtension}` is the language that a Theme chooses as its primary Template language, which defaults to
    `peb`, but if a theme were to be set up using templates from another language this would then use that other 
    languages' extension 
- `{defaultExtension}` is your preferred template language, which would make it so templates of your preferred language 
    are used, which defaults to `peb`
- `{templateBase}` is `layouts`
 
---
 
- `templates/{template}`
    - The exact full path and extension of the layout file from anywhere in your templates
- `templates/{template}.{themePreferredExtension}`
    - The exact full path of the layout file from anywhere in your templates, where the extension matches your theme
- `templates/{template}.{defaultExtension}`
    - The exact full path of the layout file from anywhere in your templates, where the extension matches the default
- `templates/layouts/{template}`
    - The path and extension of the layout file within `layouts/`
- `templates/layouts/{template}.{themePreferredExtension}`
    - The path of the layout file within `layouts/`, where the extension matches your theme
- `templates/layouts/{template}.{defaultExtension}`
    - The path of the layout file within `layouts/`, where the extension matches the default

This has the effect of a layout being found if it is an exact match anywhere in your templates, then falling back at the 
end to just the name of the file as it sits within `layouts`. So you could use a layout as specific as 
`my/custom/layout/in/a/deep/path.peb` and know that it will get chosen, even if it isn't in the `layouts` directory, or
simply use `single` expecting it to match the file `layouts/single.peb`. It is preferred for you to use the "simple" 
version, as it makes it safer for refactoring later if needed, as well as just being shorter and easier to understand at 
a glance.

## Page Templates
---

Plugins' page types can choose the "pattern" with which a page template is found, which allows Orchid to 
dynamically change how the "page content" is structured within the layout simply based on the templates available and 
the data within a page. The patterns that a page sets up are then expanded and used in a similar format as is used with
the layouts above. The exact pattern used by the page is determined by the plugin and is usually based on the specific 
configuration of the page that makes it unique amongst other pages. You can set an array of possible patterns within a
page's Front Matter to customize it yourself, or just let the plugin take care of it for you. 

As an example, the Posts plugin specifies a custom list of templates. These are added to the beginning of a list and 
Orchid will automatically add the value of the page's `key` (or its "type") and the fallback value of `page` to the end
of this list. The full list of "patterns" for a Post page then becomes:

- `post-{category}`
- `{category}`
- `post`
- `page`

These get expanded into the same list as with Layouts, except using the `pages` `{templateBase}` directory instead of 
`layouts`, and the patterns injected one-at-a-time as `{template}`, resulting in the following list of possible 
templates:

- `templates/post-{category}`
- `templates/post-{category}.{themePreferredExtension}`
- `templates/post-{category}.{defaultExtension}`
- `templates/pages/post-{category}`
- `templates/pages/post-{category}.{themePreferredExtension}`
- `templates/pages/post-{category}.{defaultExtension}`
- `templates/{category}`
- `templates/{category}.{themePreferredExtension}`
- `templates/{category}.{defaultExtension}`
- `templates/pages/{category}`
- `templates/pages/{category}.{themePreferredExtension}`
- `templates/pages/{category}.{defaultExtension}`
- `templates/post`
- `templates/post.{themePreferredExtension}`
- `templates/post.{defaultExtension}`
- `templates/pages/post`
- `templates/pages/post.{themePreferredExtension}`
- `templates/pages/post.{defaultExtension}`
- `templates/page`
- `templates/page.{themePreferredExtension}`
- `templates/page.{defaultExtension}`
- `templates/pages/page`
- `templates/pages/page.{themePreferredExtension}`
- `templates/pages/page.{defaultExtension}`

You can see that Orchid will fall back to the `templates/pages/page.peb` template, which just renders the Page Content 
directly. In fact, this behavior is desirable for most pages, and you will rarely find a plugin providing its own Page 
templates. Rather, you would expect the Theme to provide templates for the most common page types, and then you might 
customize these further for your specific categories, etc., extending the base `post` page template yourself.

## Example: Layouts vs Page Templates

To demonstrate the differences between layouts and page templates, lets build a simple example. In this example, you
are building a site with a main landing page which should span full-width and contain custom markup throughout the 
entire content body. You also have static pages that are also full width with custom content, but the content includes 
some extra information for each page like the author and some additional links set in each page's front matter. You also
have a blog, where the pages each contain a sidebar with a listing of the tags on each post, and a wiki, where the 
sidebar contains the full menu for the wiki. 

A normal approach to setting these up would produce the following templates, each listed with the template they extend:

- `homepage.peb` extends `single.peb`
- `staticPage.peb` extends `single.peb`
- `post.peb` extends `sidebar.peb`
- `wiki.peb` extends `sidebar.peb`

The problem with templates set up in this manner is that it forces each "page type" to know about the layout is is 
contained in, when in reality the only thing that the template actually cares to customize is what's in the main content
body on the page. It also limits each page to _only_ being able to use that one type of layout, unless you were to 
copy the templates and make a new version for each page/layout combination:

- `homepage-single.peb` extends `single.peb`
- `homepage-sidebar.peb` extends `sidebar.peb`
- `staticPage-single.peb` extends `single.peb`
- `staticPage-sidebar.peb` extends `sidebar.peb`
- `post-single.peb` extends `single.peb`
- `post-sidebar.peb` extends `sidebar.peb`
- `wiki-single.peb` extends `single.peb`
- `wiki-sidebar.peb` extends `sidebar.peb`

Page Templates set up in this manner is very much how themes in Wordpress are set up, and it ends up giving you a large
number of theme files that become difficult to manage and refactor. And while you may be able to move the logic of the 
page "layout" into some conditional logic that sets up the actual layout to reduce the number of files needed, it still 
requires the content body of the page to know information about the rest of the page that it really doesn't care about. 
Contrast this the Orchid model of page templates and layouts:

- Layouts
    - `single.peb`
    - `sidebar.peb`
- Page Templates
    - `homepage.peb` doesn't extend anything
    - `staticPage.peb` doesn't extend anything
    - `post.peb` doesn't extend anything
    - `wiki.peb` doesn't extend anything

This now allows us to embed _any_ page template into _any_ other layout, and neither one needs to know anything about
the markup, blocks, or filenames names of any of the others. You can even add a third layout, and start using that one 
without needing to change anything about the page templates, and no matter how many layouts or page types you have, you 
will always be able to mix-and-match them as you please with no issues.

All this allows Layouts to truly just be that: a template which "lays out" the various site pieces into their location
within the page. The Page Template is then only concerned with its page-specific content and structure, and doesn't care
about the layout or even the theme, in which is is embedded.

## Component Templates
---

Component Templates work exactly the same as Page Templates, but for each Component on the Page. The Component type can
specify its own list of possible template patterns, which are expanded into a full list within the `components/` 
`{templateBase}` directory. The only difference here is that there is not "default" component template like there is 
with a Page Template, and it is expected that plugins provide a template for the most generic markup needed for the 
specific component to look good.

As an example, the `readme` component does not specify any unique patterns, leaving it with just a list with `readme`.
This is then expanded into the following list, also following the rules as with Layouts and Page templates.

- `templates/readme`
- `templates/readme.{themePreferredExtension}`
- `templates/readme.{defaultExtension}`
- `templates/components/readme`
- `templates/components/readme.{themePreferredExtension}`
- `templates/components/readme.{defaultExtension}`

## Custom Tag Templates
---

Contrary to the flexibility of Components and Page templates, Tags are quite strict in the templates they allow for 
themselves. This is intentional, as Pages and Components are by nature customized by the content of the page within 
which they reside, and what they output may look different based on the page using them. By contrast, Tags are intended
to only be concerned with its inputs and outputs, and not have a template that is contingent on anything other than what 
it itself says. To that end, all custom Tag templates are found as the tag name within the `tags/` directory, and that's
it. 

By nature, Tags must be implemented as an extension to specific compiler language, and as Pebble is the only language 
that implements this extension in Orchid's core, all Tag templates look like `templates/tags/{tagKey}.peb`.


{% endblock %}