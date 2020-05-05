---
description: A framework for testing Orchid plugins.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1573330826/plugins/test.jpg
    alt: Forms
    caption: Photo by David Travis on Unsplash
---

## About

The OrchidTest plugin helps you develop your own components for Orchid by providing a framework for integration-testing
Orchid modules and a set of assertions to check what was rendered. This framework is intended to be used with 
[JUnit5](https://junit.org/junit5/docs/current/user-guide/), with assertions built on the [Strikt](https://strikt.io/)
assertion library and [kotlinx.html](https://github.com/Kotlin/kotlinx.html).

## Demo

All of Orchid's official plugins are tested with this framework. See any of the tests in the main Orchid repo for demos.

## Usage

### Basic Usage

An OrchidTest integration test is a standard JUnit5 class that extends `OrchidIntegrationTest`. The `OrchidModule`s 
under test should be passed to the superclass constructor. Tests are regular `@Test`-annotated functions, in which you
will set up with mock resources and run Orchid, and then make assertions on the pages that were rendered and the 
contents of those pages.

```kotlin
class CustomPluginTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    CustomPluginModule()
) {

    @Test
    fun test01() {
        ...
    }
}
```

Each test function typically consists of 3 sections: 

1) Arrange test resources and configurations
2) Execute test to perform an Orchid build
3) Assert which pages were rendered, and the contents of those pages

### Arrange test resources and configurations

Use the `resource()` function to add resources to your test, which work exactly the same as if you were adding a 
file to your site's local resources. The first argument is the full file path and name, and the second is the raw 
contents of that page, typically as a Kotlin 
[multiline string](https://kotlinlang.org/docs/reference/basic-types.html#string-literals).

You can also add options to the `config.yml` with `configObject()`. It accepts a primary key to add options to, and JSON
snippet for the options at that key (later, this will accept some kind of configuration DSL instead of JSON strings).

Finally, you can pass command-line flags to the test build `flag()`.

```kotlin
@Test
fun test01() {
    resource(
        "homepage.md", 
        """
        |## Homepage Title
        |
        |- List item 1
        |- List item 2
        |- List item 3
        """.trimMargin()
    )
    configObject(
        "wiki", 
        """
        |{
        |  "sections": [
        |    "section1", 
        |    "section2"
        |  ]
        |}
        """.trimMargin()
    )
    flag("legacySourceDoc", "true")
    
    ...
}
```

### Execute test

Calling `execute()` within a test function will run a complete Orchid build with the configured resources and options. 
It will run the build to completion and return an object with the results of the build, or else will throw an exception
if the build failed, failing the test as well. This method call is typically placed directly with `expectThat()` for 
convenience in writing assertions.

```kotlin
@Test
fun test01() {
    ...
    
    expectThat(execute())
    
    ...
}
```

### Assert Build Results

You will typically want to make assertions on which pages were rendered in the site. The following functions are 
available on the test results builder:

- `printResults()` - Make no assertions, but print all rendered pages to the terminal for quick debugging.
- `somethingRendered()` - Assert that at least one file was rendered
- `nothingRendered()` - Assert that no files were rendered
- `pagesGenerated(size: Int)` - Assert that a specific number of pages were rendered
- `pageWasRendered()` - Assert that a file at the provided path was rendered
- `pageWasNotRendered()` - Assert that a file at the provided path was _not_ rendered
- `nothingElseRendered()` - Each call to `pageWasRendered()` marks a page as rendered. Calling `nothingElseRendered()` 
    after all appropriate calls to `pageWasRendered()` will assert that all rendered pages have been evaluated, to make 
    sure additional pages were not rendered unintentionally.

In addition to checking which _pages_ were rendered, you may wish to make assertions on the collections that were 
created. The following functions are available:

- `collectionWasCreated()` - Assert that a collection with the given `collectionType` and `collectionId` was created
- `noOtherCollectionsCreated()` - Similar to `nothingElseRendered()`, except that it checks whether all collections have
    been evaluated.

For each call to `pageWasRendered()`, you must provide an assertion lambda on the located page object. Most commonly, 
you will call `htmlBodyMatches()` in that callback and provide a `Kotlinx.html` DOM builder to match the HTML body
contents. The builder is flexible in the order that attributes are listed on each tag, and recursively checks all nested
HTML structures for equivalence. 

```kotlin
@Test
fun test1() {
    ...
    
    expectThat(execute())
        .printResults()
        .pageWasRendered("/index.html") {
            htmlBodyMatches {
                h2 {
                    +"Homepage Title"
                }
                ul {
                    li { +"List item 1" }
                    li { +"List item 2" }
                    li { +"List item 3" }
                }
            }
        }
        .nothingElseRendered()
}
```
