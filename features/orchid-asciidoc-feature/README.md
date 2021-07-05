---
description: Compile your content using Asciidoctor.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973072/plugins/asciidoc.jpg
    alt: Asciidoc
    caption: Photo by Markus Spiske on Unsplash
tags:
    - markup
---

## About

OrchidAsciidoc adds support for [AsciiDoc markup](https://asciidoctor.org/) in Orchid, as a replacement for Markdown.

## Installation

{% include 'includes/dependencyTabs.peb' %} 

## Demo

<details open>
<summary>Source</summary>

{% filter compileAs('adoc') %}
```asciidoc
= My Article
J. Smith

https://wikipedia.org[Wikipedia] is an
on-line encyclopaedia, available in
English and *many* other languages.

*Software*

You can install 'package-name' using
the `gem` command:

 gem install package-name

*Hardware*

Metals commonly used include:

* copper
* tin
* lead
```
{% endfilter %}

</details>

<details>
<summary>Result</summary>

{% filter compileAs('ad') %}
= My Article
J. Smith

https://wikipedia.org[Wikipedia] is an
on-line encyclopaedia, available in
English and *many* other languages.

*Software*

You can install 'package-name' using
the `gem` command:

 gem install package-name

*Hardware*

Metals commonly used include:

* copper
* tin
* lead
{% endfilter %}

</details>

## Usage

Simply include this plugin and you're all set. Orchid will now recognize files with file extensions of `ad`, `adoc`, 
`asciidoc`, or `asciidoctor`, and compile them as Asciidoc instead of Markdown, no further configuration necessary.

```text
. / (resources root)
├── homepage.md
├── config.yml
└── pages/
    ├── page1.md
    └── page2.adoc <-- will be processed as AsciiDoc
```

Of course, you may also embed chunks of AsciiDoc markup within content of other formats using `compileAs('adoc')`:

```markdown
## Markdown Header

{% verbatim %}
{% filter compileAs('adoc') %} <-- filtered block will be processed as AsciiDoc
= AsciiDoc Header
{% endfilter %}
{%- endverbatim %}

```

### Includes

Files can be included using the standard `include::[]` directive. Included files are first resolved relative to the 
current file, which is the normal behavior of Asciidoctor. If no file can be found relative to it, Orchid will then 
check for a file at the given path in your site resources, similar to how most other inclues in Orchid work. Nested 
relative includes are not currently supported.

```asciidoc
= homepage.adoc

include::other_page.adoc[]
```

Due to constraints with the AsciidoctorJ library, the following limitation exists when including files in Orchid. 

- `tag` and `tags` attributes may not cover the full range of functionality available in Asciidoctor, as it had to be 
    re-implemented manually in Orchid. Tag names and the `*` wildcard are supported, but 
    [tag filtering](https://asciidoctor-docs.netlify.com/asciidoc/1.5/directives/include-lines-and-tags/#tag-filtering)
    through negations or more complex wildcard usage is not currently supported. If this is a feature you are needing, 
    please leave a comment on [this issue](https://github.com/asciidoctor/asciidoctor/issues/571) to let the Asciidoctor
    maintainers know you want this functionality in Orchid!

### Images

Images are not currently supported. Local images references by the `image::[]` macro will not be copied to the final
site automatically, so you should stick with external URLs for now.

```asciidoc
= homepage.adoc

image::https://picsum.photos/200/300.jpg[]
```
