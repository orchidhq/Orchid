---
classnames:
  - OrchidGenerator
  - OrchidCollection
  - OrchidPage
---

## Generators
---

Generators are the work-horses of Orchid, and form the basis around which everything else in Orchid lives. Each 
Generator is kind-of like a mini static site generator all on its own, except that it is able to work with all the other
generators in your build to produce a site that is flexible and extensible in ways that no other tool could ever be. 

Generators primarily have two jobs: to decide what Pages should exist for a particular section of the site, and how we 
need to process content to create those pages. Generators work in two phases: the Indexing phase, and the Generation 
phase, which will de described further below. Generators create a logical grouping of content within your site, and are
usually set up to be minimal and do just one job very well, rather than attempting to do everything itself.
 
As one build typically has many generators, each with a focused task, and each one can be turned on or off at will, 
you'll be able to pick-and-choose the exact set of features that your site needs without worrying about bloating it with 
stuff you don't need.

A familiar example for what exactly a generator is, is the {{anchor('Orchid Posts')}} generator, which creates blog 
posts with customizable permalinks and orders its pages by publication date. The Posts generator does not generate any
archives of its own, but instead the {{anchor('Orchid Taxonomies')}} generator does that job for us. The Taxonomies 
generator can inspect all the blog posts being created by the Posts generator, and can generate archives for those 
pages. Because both the Posts and Taxonomies generators are small and focused, they can be easily composed or used with
other plugins as well, or completely omitted if you find you don't want a blog or need full archives. 

Most plugins are centered around building a single generator, along with {{anchor('Components')}} and 
{{anchor('menus')}} and other features that expand upon the content from that generator..

### Indexing Phase

During the Indexing phase, Orchid asks each Generator, in turn, for a list of Pages it intends to produce. The methods
that each generator uses to determine these pages is irrelevant; all Orchid cares about is the end result, which is that
there are some Pages that need to be rendered into the output site. 

It is common for Generators to create Pages that correspond directly to a source file, such as with Blog posts, Static
Pages, or the Wiki you're currently reading. Another common occurrence is for some external program to parse a content
model from source code, which is then integrated into Orchid. Such is the case with the Orchid Javadoc and KSS plugins. 

Generators may also index content that is not intended to be a page in the output, but as the content for a component or 
menu. Such content is typically smaller in scope but used on many pages. An example would be the Orchid Forms and 
Presentations plugins. Each Form has a non-trivial configuration, and a single form may be used across many pages. By 
indexing the form definition in a Generator, we can be sure that all forms of a given type are always kept in sync
throughout the site, and are immune to changes not propagating.

### Generating Phase

After all Generators have finished indexing their content, Orchid does some processing on these output Pages before 
passing them back to their source Generator to be rendered. Pages can be rendered in several ways: into a Layout 
template resource, into a String layout, as raw content, and as a binary stream. 

**Rendering as Layout**

When rendering a page as a template, Orchid asks the Page what layout it should use. Orchid then attempts to locate that
layout in the theme or in the local resources directory, which takes precedence over the theme-defined layouts and allow
for themes to be customized without needing access to the theme files itself. 

**Rendering as String**

Rendering a stream is similar to rendering as a layout, except that the template definition comes as a static String 
rather than an override-able resource. This is typically more useful in testing, but may be used in specific situations
if the Generator calls for a very specific layout and doesn't want to allow it to be changed.

**Rendering Raw**

Many types of pages should not be rendered into a layout but should still have its content processed, such as compiling 
SCSS into CSS. The content of these Pages is loaded and processed as a String.

**Render Binary**

Other pages will become corrupted if the source content is loaded as a String, and must be treated as a stream of bytes,
such as images, videos, or PDF files. The content of these pages are not processed at all, and are simply copied 
directly from the source to the destination.

Pages may be set as a draft, which will skip the rendering of these pages. This is done behind the scenes and is not
a concern of the Generator. The Generator should simply render all pages passed to it as if it should be rendered.

Generators can also set a new theme to be used just for its own pages, which completely changes the layouts, CSS/JS, 
menus, etc. that are loaded for these pages.

## Creating Generators

Here is a description of the pieces involved in creating and setting up a custom Generator. Code samples, in both Java
and Kotlin, are also shown below.

1) Create a class that extends {{ anchor('OrchidGenerator') }}
    1) Override the constructor with the properties listed below. Orchid loads all Generators via dependency-injection, 
        so you may specify any dependency in your constructor, but it must be annotated `@Inject`. 
        1) {{ anchor('OrchidContext context', 'OrchidContext') }} - Should be injected into your generator's 
            constructor and passed to the superclass.
        2) `String key` - The unique key representing this generator. Options are loaded at this key, and this generator
            may be enabled or disabled also by this key.
        3) `int priority` - The priority given to this generator, relative to the others. Generators with a higher 
            priority are indexed before those with lower priority, and the same is true of when the pages from each
            generator are rendered. You can learn more about the priority you should choose in the next page.
    2) Override the `startIndexing()` method. It may return a List of Pages which are created by this generator, which 
        are then indexed and made accessible by other plugins. You are also free to index other content in this callback 
        that does not directly correspond to a Page, such as the pages for a presentation that is embedded as a 
        Component.
    3) Override the `startGenerating()` method. This receives a Stream of pages to render, which may or may not be the 
        same list of pages that were indexed, and may also be a parallel stream. This allows for future optimizations 
        such as only rendering the pages that have changed during `serve` mode.
    4) (optional) Override the `getCollections()` method to return custom collections of the pages indexed by this 
        generator. You can learn more about collections {{ anchor('in the Collections page', 'collections') }}. The 
        default implementation returns a {{ anchor('FileCollection') }} representing all the pages returned from 
        `startIndexing()`. 
2) Register your generator with your plugin's module. 
3) (optional) Create a class that is your generator's `Model`, and add all content to be indexed to that Model. Your 
    plugin's other components, menus, etc. should inject and read data from this model instead of from the generator.

## Generator Priority

Generators are ordered by priority, and then indexed and generated in that order. This allows you to set "dependencies" 
from one generator to another, so that you can have the pages indexed by one generator be used as the basis for indexing
pages in another. A good example of this is the {{ anchor('taxonomies generator', 'Orchid Taxonomies') }}, which looks 
at the pages indexed by the {{ anchor('pages generator', 'Orchid Pages') }}, 
{{ anchor('posts generator', 'Orchid Posts') }}, and {{ anchor('wiki generator', 'Orchid Wiki') }}, and creates custom 
archives for these pages. But because it is just based on generator priority, another plugin could create a new 
generator with the same priority as the posts generator, and it would be able to have its content read by the Taxonomies
generator as well!

Generally-speaking, there are 4 main priortiy groups that generators will typically run in. These priorities are set as
static constants in `OrchidGenerator`, but you are free to hardcode your own integer priority or add/subtract from these
to suit your needs. These groups are:

- `OrchidGenerator.PRIORITY_INIT` - Typically used for Generators that produce pages that content pages depend on, like 
    registering global assets.
- `OrchidGenerator.PRIORITY_EARLY` - Typically used for Generators that produce content pages. These are your plugins 
    that define strong content models, like blog posts, wikis, etc. You want these generators to run early so that other 
    generators may do interesting things with the content they create.
- `OrchidGenerator.PRIORITY_DEFAULT` - Typically used for Generators that produce pages based on data contained in 
    content pages, or just index content for use in components, menus, or collections.
- `OrchidGenerator.PRIORITY_LATE` - Typically used for Generators that produce data files, like sitemaps, RSS feeds, or 
    JSON indices of all your site's pages. These should run late so as to capture the "content" pages as well as the 
    "non-content" or "archive"-type pages.

## Page Rendering Types

There are several modes that Orchid can render pages in. Rather than trying to figure it out itself, Orchid allows 
generators to be flexible and define their own ways to render pages. 

**Rendering as Layout**

```java
javapages.forEach(context::renderTemplate)
```

When rendering a page as a template, Orchid asks the Page what layout it should use. Orchid then attempts to locate that
layout in the theme or in the local resources directory, which takes precedence over the theme-defined layouts and allow
for themes to be customized without needing access to the theme files itself. 

Rendering as a layout is typically done for "content pages" (`PRIORITY_EARLY`). The user defines the layout that will be 
used for rendering that page in its Front Matter or Archetype options. The layouts are requested from `page.getLayout()`
and this should typically not be overridden, since the layout comes from the Theme and a plugin doesn't know which theme
is it used with and which layouts are available to it. To customize the page's display, use `page templates` instead, 
which are designed to be customized by the plugin and can be embedded in any layout.

**Rendering as String**

```java
javapages.forEach(context::renderString)
```

Rendering a stream is similar to rendering as a layout, except that the template definition comes as a static String 
rather than an override-able resource. This is typically more useful in testing, but may be used in specific situations
if the Generator calls for a very specific layout and doesn't want to allow it to be changed.

**Rendering Raw**

```java
javapages.forEach(context::renderRaw)
```

Many types of pages should not be rendered into a layout but should still have its content processed, such as compiling 
SCSS into CSS. The content of these Pages is loaded and processed as a String. You will also likely render pages raw
when generating "data" pages, such as sitemaps, RSS feeds, or JSON indices.

**Render Binary**

```java
javapages.forEach(context::renderBinary)
```

Other pages will become corrupted if the source content is loaded as a String, and must be treated as a stream of bytes,
such as images, videos, or PDF files. The content of these pages are not processed at all, and are simply copied 
directly from the source to the destination.

Pages may be set as a draft, which will skip the rendering of these pages. This is done behind the scenes and is not
a concern of the Generator. The Generator should simply render all pages passed to it as if it should be rendered.

Generators can also set a new theme to be used just for its own pages, which completely changes the layouts, CSS/JS, 
menus, etc. that are loaded for these pages.

It is a common occurrence when rendering "asset" pages that some assets may be binary and some are text-based and should
be rendered raw. For example, you might want to render all pages in the `assets/` directory, which may contain both 
`scss`, which should be rendered raw, and `jpeg` files, which should be rendered binary. To determine which rendering 
mode to use, you may call `context.isBinaryExtension("")` to determine whether a given file extension represents a 
binary type and should be rendered as binary. Orchid accepts `jpg`, `jpeg`, `png`, `pdf`, `gif`, `svg`, `otf`, `eot`, 
`ttf`, `woff`, and `woff2` as binary formats by default, but this list can be customized by the user in their 
`config.yml` for types that do not fall in this list.

# Creating a Simple Generator

A great way to learn all the ins-and-outs of building powerful but flexible generators would probably be to browse the 
source code for the official Orchid plugins, but for just starting out and trying to understand generators, let's walk
through building a simple generator. You should start with the sample code at the bottom of this page, and from there
we'll build up and discuss what it's all doing and how you can customize it for your own needs. For brevity, I will be
doing all sample code here in Kotlin, but you are free to adapt it to Java or any other JVM language. 

## Indexing

Indexing a Generator all happens within the generator's `startIndexing()` callback. This method is abstract and must be 
overridden by all custom generators. Stick with me a minute, but let's take a look at the full method signature and try 
to understand what it is telling us, both about the Generator and about Orchid.

```java
override fun startIndexing(): List<OrchidPage>?
```

The first thing we'll notice is that this method returns a list of `OrchidPage`s, _which may be null_. This tells us

1) The goal of a generator's `startIndexing()` method is to **tell Orchid what pages it indexed**. By passing pages
    back from this method, the Generator does not necessarily need to hold onto them itself. Of course, you may hold 
    onto your custom pages if you need to, and in fact, most official plugins do. This is typically because the plugin
    has created custom `OrchidPage` subclasses, and it has other features that depend on those subclasses. That being 
    said, it is sufficient for a generator to simply return the pages it indexed without holding onto them. Orchid will
    hold onto them for you and make them available to other plugins that want to look at them. 
2) A generator can do more than just index pages. In fact, it is expected that there are generators that don't index any
    pages at all, and instead just want to index other content to hold onto itself, or render pages without indexing 
    them first.
    
One other thing we'll notice is that nothing gets passed to the `startIndexing()` method. Orchid doesn't assume anything 
about where these pages come from, and it really doesn't care. Orchid does not try to make assumptions like the local 
file directories that pages come from, because indexed pages may not come directly from a page, or even index pages at 
all. This gives the generator unrestricted freedom to do whatever it needs to do to add pages to the final site. While
many plugins do have content that comes directly from a local file, the great power of Orchid comes from the fact that 
even more plugins generate content from other sources, such as code comments or other pages!

### Creating Pages

Now that you understand the main goals of indexing in Orchid, let's look at a simple, practical example of indexing 
pages. For this example, lets say we want to take all markdown pages in your sire resources' `public` folder and render 
them into your output site at the same URL. 

The basic `startIndexing()` callback will look like this:

```java
override fun startIndexing(): List<OrchidPage>? {
    return context.getLocalResourceEntries("public", arrayOf("md"), true)
        .map { resource -> OrchidPage(resource, "public") }
}
```

In this example, `context.getLocalResourceEntries("public", arrayOf("md"), true)` returns a list of 
{{ anchor('OrchidResource') }} from the `public/` directory in your _local_ resource that have the `.md` extension, and 
it searches for these resources recursively. There are many methods on the `context` object which can be used to locate 
resources in different ways, see {{ anchor('ResourceService') }} for all possible methods.

After we have our list of resource, we then map it directly to a list of {{ anchor('OrchidPage') }} by passing 
the resource to the page in its constructor, and giving it a type. This type is used for loading options via Archetype, 
as well as for determining the page template to use. 

There are several other constructors for the `OrchidPage` that may be of interest:

- `OrchidPage(resource: OrchidResource, key: String)`
- `OrchidPage(resource: OrchidResource, key: String, title: String)`
- `OrchidPage(resource: OrchidResource, key: String, title: String, path: String)`

where

- `resource` is the "intrinsic content" of the page. The content from this resources is available at 
    `page.getContent()`, which will be compiled according to the file extension of that resource. A Resource wraps some
    form of content, such as a file, but may also be a raw String, or you could create your own Resource type to wrap
    your own content object. 
- `key` is the "type" of the page.
- `title` is the pages's user-facing title. This will be put in the page's `<title></title>` tag, and is also the 
    default "itemId", which is used for linking to pages. If this is not given, it defaults to `resource.getTitle()`, 
    which itself typically comes from a page's Front Matter or is derived from the filename. 
- `path` is the URL in the final site where the page will be rendered to, as if it were a source file. If this is not
    given, the page will be rendered to the output-equivalent of `resource`, which is typically its "pretty" URL form. 

However, you are not limited to just what's available in the `OrchidCore` class. You may choose to make your own 
`OrchidPage` or `OrchidResource` subclasses, which may help simplify the setup of converting your custom content to the
desired output page. 

An example case where you might what to create your own `OrchidResource` class is with the Javadoc plugin, where the 
content to be displayed on a page doesn't come from a file on disk, but from the model of a class or package that comes
from the Javadoc tool. You might have your own model you wish to wrap, or you might pull data down as JSON from a 
headless CMS.

You may also wish to do some transformations on the resource before passing it to the OrchidPage. Each `OrchidResource`
has an {{ anchor('OrchidReference') }} which is passed to the page when it is created, and you are free to many 
any adjustments to the reference on either the resource or the page. 

In many cases, you may wish for the end-user to customize the resulting URL. For these cases, you can inject the 
{{ anchor('PermalinkStrategy') }} and call `permalinkStrategy.applyPermalink(page, permalink)`. The permalink 
should be a formatting string which will be evaluated and set as the page's output URL. This format string should be 
customizable by the user, typically as an `Option` on your custom `OrchidPage` class or in your generator.

### Creating Collections

Once you've let Orchid know what pages you intend to render, you may also wish to let Orchid know the logical grouping
behind those pages. This is entirely optional, as the base `OrchidGenerator` class will provide a basic implementation
for you, but you may override this method to provide your own collections. The purpose of a Collection is to allow 
Orchid to locate specific resources when requested, such as with the `anchor()` or `link()` 
{{ anchor('Template Functions', 'TemplateFunction') }}, which may not even be `OrchidPages`.

An {{ anchor('OrchidCollection') }} gets passed a List of arbitrary objects to its constructor, and it 
implements a callback `find(id: String): List<T>`. This callback just searches its own contents and matches any items to 
the id passed to it. These collections are grouped by a `collectionId` and `collectionType` (which is typically the 
generator key). The end-user can then search for Pages (or any other object in a collection) and optionally add filters 
for the `collectionId` and `collectionType` to refine the searches if necessary. 

A user can call any of the following methods to search within these collections indexed by all generators. These methods
all accept the same parameters of `itemId`, `collectionId`, and `collectionType` which correspond directly to the 
values set in the Collection implementation. `collectionId`, and `collectionType` filter the collections searched, and 
the `itemId` is passed to the collection's `find()` method. The results of all matching Collections are then combined 
together.

- `findAll()` - Find all pages in all matching collections that match the `itemId` query, and return the list. 
- `find()` - Find all pages in all matching collections that match the `itemId` query, and return the first item in the 
    resulting list.
- `link()` - The same as `find()`, but instead of returning the first item in the list, return a String representing a 
    link to that item. If the item located by `find` is an `OrchidPage`, return `page.getLink()`, otherwise return an 
    empty string. 
- `anchor()` - The same as `link()`, but instead of returning the link String directly, wrap it in an anchor tag 
    (`<a></a>`) for convenience. You can optionally pass a `title` to this function to have that title be used as the 
    link text. If the title is not given, the `itemId` will be used as the link text. In addition, you can pass custom
    CSS classes to be added to the anchor tag as well.

There are several `OrchidCollection` implementations that you are free to use, or you may create your own.

- {{ anchor('FileCollection') }} - Search all the `OrchidPages` passed to it, matching the pages by their title
- {{ anchor('FolderCollection') }} - The same as as `FileCollection`, but also contains additional metadata 
    related to the specific folder that all its containing pages came from. While not used directly by the search function, this
    metadata is useful for generating admin/CMS UIs which can add new pages to this specific collection. An example of 
    this is the {{ anchor('Orchid Netlify CMS') }} plugin, which is actually the inspiration for this feature. 

### Indexing Custom Content

While a big focus of the `startIndexing()` method is to provide Orchid with the pages the generator intends to render, 
it is also quite useful for indexing _any_ kind of content that is managed just by the plugin. You might choose to index
custom, non-Page data here for Components that generate content based on Resources. Typically, the kind of content you'd
want to index here is content that potentially takes a while to load so that consuming it multiple times (such as the 
same Component rendered on many pages) only incurs that cost one, which is during the indexing phase.

## Generating

Once the pages have been indexed, it becomes almost trivial to render them. By waiting to render the pages until after 
all generators have been indexed, we have a guarantee that any piece of content will be available on any page that needs
it regardless of which generator it came from. There will never be any race conditions among plugins, because everything
is very well ordered. 

The pages that a generator index are passed to the `startGenerating()` method as a Stream, and most plugins just need to 
determine how to render it, using the Page Rendering Types described above. Note that this stream may be a parallel 
stream as an optimization, and every attempt should be made to keep all your models and pages thread-safe. But if you
don't modify anything after the indexing phase has finished (which you should _really_, _really_ avoid), and only ever
read content that has already been indexed, this should never be an issue.

That being said, there are rare circumstances in which you want to render pages without them first being indexed, and 
you're free to request more resources to render here if you need. An example of this is the Sitemap Generator, which
generates sitemaps containing links to all the pages that have been passed back to Orchid. But the sitemap pages would
become infinitely recursive if they themselves were part of the sitemap pages they were were generating, so they must
be excluded from Orchid's internal index, and instead just rendered directly in the `startGenerating()` callback. 

In addition, if you _know_ your pages to be thread-unsafe, you can always call `pages.sequential()` on the stream passed 
to the callback to ensure it is evaluated sequentially, but again, this should be avoided whenever possible by 
implementing appropriate synchronization techniques and by treating your data as if it were immutable during the 
generating phase, even if it is not.

# Examples

## Kotlin Example

<div>
```java
@Singleton
@Description("A description for this custom generator.", name = "Custom Generator)
class CustomGenerator 
@Inject 
constructor(
        context: OrchidContext
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "customKey"
    }

    @Option
    @StringDefault("custom")
    @Description("The base directory in local resources to look for custom pages in.")
    lateinit var baseDir: String

    override fun startIndexing(): List<OrchidPage>? {
        val resourcesList = context.getLocalResourceEntries(baseDir, null, true)
        val pages = ArrayList<OrchidPage>()

        for (entry in resourcesList) {
            entry.reference.stripFromPath(baseDir)
            val page = OrchidPage(entry, "pageType")
            pages.add(page)
        }

        return pages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach({ context.renderTemplate(it) })
    }

    override fun getCollections(): List<OrchidCollection<*>> {
        val collections = ArrayList<OrchidCollection<*>>()
        collections.add(FileCollection(this, "customPages", context.getGeneratorPages(this.key)))

        return collections
    }
}
```
</div>

## Java Example

<div>
```java
@Singleton
@Description("A description for this custom generator.")
public static class CustomGenerator extends OrchidGenerator {

    public static final String GENERATOR_KEY = "customKey";

    @Option
    @StringDefault("custom")
    @Description("The base directory in local resources to look for custom pages in.")
    public String baseDir;

    @Inject
    public CustomGenerator(OrchidContext context) {
        super(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY);
    }

    @Override
    public List<OrchidPage> startIndexing() {
        List<OrchidResource> resourcesList = context.getLocalResourceEntries(baseDir, null, true);
        List<OrchidPage> pages = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            entry.getReference().stripFromPath(baseDir);
            OrchidPage page = new OrchidPage(entry, "pageType");
            pages.add(page);
        }

        return pages;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        pages.forEach(context::renderTemplate);
    }

    @Override
    public List<OrchidCollection> getCollections() {
        List<OrchidCollection> collections = new ArrayList<>();
        collections.add(new FileCollection(this, "customPages", context.getGeneratorPages(this.key));

        return collections;
    }
}
```
</div>
