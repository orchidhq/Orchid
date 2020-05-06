---
description: 'Orchid uses Google Guice to manage the plugin system, with some additional features that provide powerful introspection into your site from the admin panel.'
classnames:
  - OrchidModule
---

Orchid is built on top of Google Guice for dependency injection, and there really isn't anything that cannot be 
extended. A basic knowledge of Guice might be helpful for more advanced customization, but a basic explanation here will
certainly be enough to get started writing basic plugins.

## Dependency Injection 

"Dependency Injection" (often shortened to DI) is a fancy term for a rather simple concept. To understand DI, lets look 
at a pretty standard example: logging. It is typical for an application to log information like the following snippet:

```java
public class LoggerExample {
    private final Logger logger;
    
    public LoggerExample() {
        this.logger = LoggerFactory.getInstance();
        this.logger.log("Hello World"); 
    }
} 
```

This is commonly known as the _Factory Pattern_, and it has some issues. The most obvious issue is that the 
LoggerExample is tied to exactly one type of Logger, so it becomes difficult to swap that implementation out. Sure the 
implementation can be changed within the factory body, this becomes much more difficult if the Logger required other 
conditions to be set up before it can be created.

Dependency Injection allows us to instead set up our class such that the Logger is passed directly to its constructor.

```java
public class LoggerExample {
    private final Logger logger;
    
    @Inject
    public LoggerExample(Logger logger) {
        this.logger = logger;
        this.logger.log("Hello World"); 
    }
} 
```

Now, the burden of creating the Logger is up to another class, and the LoggerExample's only job is to simply use the 
logger given to it. This makes it easier to test, but also gives consumers of the LoggerExample class information about
what it does without looking at its source, namely that it is going to log something. 

Now at its heart, DI is a concept and not tied to any particular framework. You could actually just set up all your
classes to receive dependant objects in their only constructor and never need to set up a full framework, but this 
quickly gets too cumbersome as the project size grows and changes over time. So while it is possible to implement DI
without a full framework, it is much more common to use a framework like Spring or Guice to do the hard work for you.
 
That being said, Orchid is based on Guice, which has several advantages over Spring:

1) Guice is smaller and more lightweight than Spring
1) Guice has a much quicker startup time
1) Spring is tailored very heavily for web applications and the Servlet lifecycle, but Guice is designed to work for 
    any application. Since Orchid is not a web application, much of what is included in Spring by default is not needed.

## Guice Module Registration 

Nearly everything in Orchid is set up using dependency injection similar to as shown above, using Guice as the DI 
framework. But in order for Guice to know how to construct the dependencies that are injected, we need to tell Guice
what classes can be used in our dependency graph. We do this by creating **modules** which instruct Guice on how to set
this up for us. 

When Orchid is first started, it scans the classpath, looking for all classes that implement the Guice `Module` 
interface, collect them all together, and then pass them all to Guice for the actual injection. So simply including a
dependency in your `build.gradle` which has a Module in it is enough for Orchid is add it to the Orchid build.

```java
public class CustomModule extends OrchidModule {

    @Override
    protected void configure() {
        bind(CustomService.class).to(CustomServiceImpl.class);
        
        addToSet(OrchidGenerator.class, CustomGenerator.class);
    }
} 
```

You should generally have your modules extend `OrchidModule` rather than using the default Guice `AbstractModule` class,
because it offers a few extra conveniences more useful for Orchid, and is itself a child class of `AbstractModule`. That 
being said, Orchid auto-registers any class that implements the Guice `Module` interface, so either can be used just as
easily.

Looking at the module above, you'll need to override the `configure()`, and in that method you will set up your 
bindings. There are two types of bindings you will commonly use for Orchid: binding concrete implementations to abstract
classes or interfaces, which is what is how Guice is typically used, and adding implementations to sets. The second is 
_by far_ the most commonly used binding in Orchid, and if you are building your own extendible interfaces, should almost 
always be the preferred way to set up your bindings. Both of these are described below.

### Simple Bindings

The `bind()` method takes an abstract class, and assigns a specific implementation class to it. Instances of this class 
should _always_ request injection of the abstract class, so that Guice can handle providing the concrete implementation,
changing it if necessary. 

If you are the one creating both the abstract and implementation classes, you should avoid setting up the association 
within the module itself. Instead of using `bind(A.class).to(B.class)`, you can instead use the `@ImplementedBy` 
annotation on the abstract class to set up an equivalent binding. 

```java
@ImplementedBy(B.class)
public interface A {
} 
public class B implements A {
} 
```

This method of using the `@ImplementedBy` annotation has several advantages over the module binding. First, it makes it
easier to understand the intended relationship in the source code for both classes, and both the interface and the 
implementation point directly to one another. In addition, bindings within a module have a higher priority than the 
annotation, which allows another plugin to extend yours by creating a module to register a different interface. Guice
will complain if two modules try to create a binding to the same interface, but it will happily accept both a module
and annotation binding, treating the annotation as the "default".

### Multi-Bindings

The most common types of bindings in Orchid are multi-bindings, or more specifically _set bindings_. What this means, is
that rather than having Guice inject a single instance, you can have it inject a `Set<>` of some type, where each 
instance in that set is a unique type. This is how we can get a plugin architecture in Orchid, as most bindings are 
bound as sets rather than single instances, and you can contribute new implementations to any of those sets seamlessly.

The `OrchidModule` class adds the `addToSet()` method, where the first parameter is the abstract class or interface 
class, followed by a vararg list of implementation classes. Orchid will also keep track of all sets bound in this way so
that you can easily see every possible class type which can be extended through multi-bindings. You can see all 
available set types from the {{ anchor('Admin Panel', 'Admin Panel') }}.

Some common examples of classes that are bound through sets are:

- Generators
- Components
- Menu Items
- Template Tags

## Ignoring Modules

In some cases, you may wish for a particular module to not be auto-registered, even though it is on the classpath. A 
good example is the `JavadocModule` from the {{ anchor('OrchidJavadoc') }} plugin. This plugin should 
only register its bindings when Orchid has been started from the Javadoc tool, rather than Orchid's normal main class,
and it manually adds the `JavadocModule` itself.

For these cases, you can add the `@IgnoreModule` annotation to the module class to have it skipped during 
auto-registration. 

# Understanding the Orchid Content Model

Generators are at the very core of how Orchid is so flexible. While most static site generators define rules and models 
around how content is discovered and rendered, Orchid offloads this entire process to plugins, and instead build a 
powerful framework for making it _incredibly easy_ to define these rules yourself. Some existing tools are pretty rigid, 
like Jekyll's strong base of blogging content, while others are very flexible, like Hugo's custom taxonomies, but they 
are all limited to that _one_ way of doing things. Orchid, on the other hand, is able to capture the essence of all 
these tools and bring all their content models into one site, even opening the content for one plugin to build off the 
content of another, giving you unlimited possibilities.

Orchid uses a unique way to create content that makes it easy to work with, but also gives a guarantee that the data you
need is available when you need it. To do this, it uses **Generators**, and employs a separate _indexing_ and 
_generating_ phase for each generator, which will be described in the next couple pages. But the big idea here is that 
Orchid allows plugins to load their data before any pages are rendered, so each rendered page can contain data from 
any other generator, knowing that it will always be there when you need it. 

This allows us to do some really cool things like building pages out of smaller Components, creating custom menus with 
menu items dynamically populated from the pages that Orchid has indexed, and creating strong links among pages that 
adapt to changing URL structures, even between different plugins. In addition, this allows us to build powerful content 
models that each work in isolation, but work together really well when combined in the rendered templates.

## Available Content Models

Orchid was created to take the best parts of many popular static site generators and similar tools, and bring them into
one place. Below is a list of some well-known tools whose content model has inspired some official Orchid plugins, along
with the Orchid plugins that came from this inspiration.

- [**Hugo**](https://gohugo.io/)
    - {{ anchor('OrchidPages') }}
    - {{ anchor('OrchidTaxonomies') }}
- [**Jekyll**](https://jekyllrb.com/)
    - {{ anchor('OrchidPosts') }}
- [**Gitbook**](https://toolchain.gitbook.com/)
    - {{ anchor('OrchidWiki') }}
- [**Javadoc**](https://www.oracle.com/technetwork/java/javase/tech/index-jsp-135444.html)
    - {{ anchor('OrchidJavadoc') }}
    - {{ anchor('OrchidSwiftdoc') }}
- [**SC5 Styleguide**](https://styleguide.sc5.io/)
    - {{ anchor('OrchidKSS') }}
