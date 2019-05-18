---
from: docs.plugin_index
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

== Software

You can install 'package-name' using
the `gem` command:

 gem install package-name

== Hardware

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

== Software

You can install 'package-name' using
the `gem` command:

 gem install package-name

== Hardware

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
