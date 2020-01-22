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

Files can be included using the standard `include::[]` directive. File paths are all absolute, and resolved relative to
your site resources root. Nested relative includes are not currently supported.

```asciidoc
= homepage.adoc

include::other_page.adoc[]
```

### Images

Images are not currently supported. Local images references by the `image::[]` macro will not be copied to the final
site automatically, so you should stick with external URLs for now.

```asciidoc
= homepage.adoc

image::https://picsum.photos/200/300.jpg[]
```

### Safe Mode

AsciiDoc security rules (as defined in 
[Running AsciiDoctor Securely](https://asciidoctor.org/docs/user-manual/#running-asciidoctor-securely)) can be 
optionally configured via the 'asciiDocSafeMode' key under the `services.compilers.adoc` section of the config.yml 
equivalent. 

By default, the safe mode is set to `SAFE`, which enables the popular `includes::[]` directive. Tightening the security 
mode as per the below to config.yml will disable this as required.

```text
services:
  compilers:
    adoc:
      safeMode: 'SECURE'
  ``````
