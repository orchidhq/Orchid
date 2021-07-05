---
description: ''
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1584294848/plugins/snippets.jpg
    alt: Snippets
    caption: Photo by Charles Deluvio on Unsplash
tags:
    - content
    - docs
    - components
---

## About

Extract snippets from project files and create reusable content sections. Render as plain content, or tag your snippets
so they can be easily rendered as tabs. 

## Installation

{% include 'includes/dependencyTabs.peb' %}

## Demo

- Run [WikiGeneratorTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidSnippets/src/test/kotlin/SnippetsTest.kt) for demo

## Usage

You can configure multiple methods of loading snippets, called "sections". Each section loads snippets, and you can 
assign tags to the snippets loaded in each section. You can then display individual snippets, or tagged groups of 
snippets, with Components, TemplateTags, or a TemplateFunction.

## Load Snippets

### File Snippets

File snippets are entire files, each loaded as a snippet. The name of the file becomes the "name" of the snippet. Front
Matter inside each snippet's file can add tags to that snippet.

The File adapter will load all files in a given directory, and is non-recursive by default. If no configuration to the 
Snippets plugin is provided, file snippets from the `snippets/` directory will be used.

```yaml
# config.yml

snippets:
  sections:
    - tags: ['file_snippets']
      adapter: 
        type: 'file'
        baseDirs: ['snippets']
        recursive: false
```

With the above config, the following files will be loaded as snippets:

```md
# snippets/snippet-one.md
---
tags: 
  - 'one'
---
Snippet One
```

```md
# snippets/snippet-two.md
---
tags: 
  - 'two'
---
Snippet Two
```

The following table shows how these snippets are available as data:

| Snippet Name  | Tags                   | Content     |
| ------------- | ---------------------- | ----------- |
| `snippet-one` | `file_snippets`, `one` | Snippet One |
| `snippet-two` | `file_snippets`, `two` | Snippet Two |

### Embedded Snippets

Embedded snippets search through the contents of text files to locate snippets between pairs of "start" and "end" 
delimiters. A single file can have multiple snippets embedded within it. The name, and optionally additional tags, are 
given by the "start" delimiter. The Embedded adapter will recursively search files in a directory by default. 

Snippets can be found between delimiters of the format: `snippet::snippet_name[tags]` and `end::snippet_name`. These 
delimiters must be at the _end_ of the line, and the content is everything _between_ those lines (the delimiter lines 
themselves are not part of the snippet content). Embedded snippets are commonly used for pulling content out of unit
test code, and so should be added as standard line comments in whatever code language they are embedded in. 

The tags portion of the start delimiter should be a simple comma-separated list of tags. The snippet name of the end 
delimiter is optional, so can simply be `end::` for brevity. If present, it should match the name of the start 
delimiter.

```yaml
# config.yml

snippets:
  sections:
    - tags: ['embedded_snippets']
      adapter: 
        type: 'embedded'
        baseDirs: ['src/test']
        recursive: true
```

With the above config, the following file will have its content loaded as snippets:

```md
# src/test/SnippetsTest.md

# snippet::snippet-one[one]
Snippet One
# end::snippet-one

# snippet::snippet-two[two]
Snippet Two
# end::
```

The following table shows how these snippets are available as data:

| Snippet Name  | Tags                       | Content     |
| ------------- | -------------------------- | ----------- |
| `snippet-one` | `embedded_snippets`, `one` | Snippet One |
| `snippet-two` | `embedded_snippets`, `two` | Snippet Two |

In addition, you can customize the regex used for the start and end delimiters with the `startPattern` and `endPattern`
properties. By default, the adapter expects the regex group 1 of the `startPattern` to contain the name of the snippet,
which is also the same group for the end delimiter. Group 2 is the default comma-separated list of tags, but is not 
required to be part of the regex. The indicies of both of these groups can also be customized with `patternNameGroup` 
and `patternTagGroup` properties.

```yaml
# config.yml

snippets:
  sections:
    - tags: ['embedded_snippets']
      adapter: 
        type: 'embedded'
        baseDirs: ['src/test']
        recursive: true
        startPattern: '^.*?START(.+?)$'
        endPattern: '^.*?END(.+?)$'
        patternNameGroup: 1
        patternTagGroup: 2
```

### Remote Snippets

Remote snippets come from external webpages. The Remote adapter will download a webpage over HTTP and extract a snippet
from a CSS selector on that page.

The Remote adapter will only fetch a file URL, but you can load multiple selectors from that one webpage. You must 
configure multiple sections to load snippets from multiple webpages.

```yaml
# config.yml

snippets:
  sections:
    - tags: ['remote_snippets']
      adapter: 
        type: 'remote'
        url: 'https://www.example.com'
        selectors:
          - name: 'snippet-one'
            selector: '#one'
            tags: ['one']
          - name: 'snippet-two'
            selector: '#two'
            tags: ['two']
```

With the above config, the following webpage will have its content loaded as snippets:

```md
<div id="one">
    Snippet One
</div>

<div id="two">
    Snippet One
</div>
```

The following table shows how these snippets are available as data:

| Snippet Name  | Tags                     | Content     |
| ------------- | ------------------------ | ----------- |
| `snippet-one` | `remote_snippets`, `one` | Snippet One |
| `snippet-two` | `remote_snippets`, `two` | Snippet Two |

If you only have a single selector for a webpage, you can use an abbreviated form of configuration:

```yaml
# config.yml

snippets:
  sections:
    - tags: ['remote_snippets']
      adapter: 
        type: 'remote'
        url: 'https://www.example.com'
        name: 'snippet-one'
        selector: '#one'
```

## Display Snippets

You can display snippets in a number of ways once they have been loaded. There are 2 mains ways to display snippets: 
as a single snippet (looked up by name), or multiple snippets in tabs (looked up by tags).

Snippets are compiled according to their normal file extension before being embedded into the page. For example, a File
snippet with a `.md` extension will be compiled as Markdown, even if it's embedded in an Asciidoc page. Likewise, 
Embedded snippets that came from a `.md` file will also be compiled as markdown. 

### Single Snippet

Single snippets display their contents with no additional markup. Snippets are looked up using their "name".

#### `snippet` Component

```md
---
components:
  - type: snippet
    snippetName: 'snippetName'
---
```

#### `snippet` TemplateTag

{% verbatim %}
```md
---
---
{% snippet 'snippetName' %}
```
{% endverbatim %}

#### `snippet` TemplateFunction

{% verbatim %}
```md
---
---
{$ set s = snippet('snippetName') %]
Content: {{ s.content | raw }}
Name: {{ s.name }}
Tags: {{ s.tags | join(', ') }}
```
{% endverbatim %}

### Snippet Tabs

Multiple snippets can be easily displayed together in tabs by giving them the proper tabs. You then query snippets by
one or more tags, and all snippets which have _all_ the specified tags will be loaded and displayed in tabs together. 
The "name" of the snippet will be the text of the tab, while the snippet contents will be the content of that tab, when
selected.

These tabs use the default `{{ '{% tab %}' }}` template tag to render the tabs, and so will match the rest of the tabs
for your theme.

#### `snippets` Component

```md
---
components:
  - type: snippets
    snippetTags: ['snippetTag1', 'snippetTag2']
---
```

#### `snippets` TemplateTag
{% verbatim %}
```md
---
---
{% snippets ['snippetTag1', 'snippetTag2'] %}
```
{% endverbatim %}

## Snippet Admin Panel

This plugin documents itself in the {{anchor('Admin Panel')}}. All snippets that were located will be displayed in a 
table with their name and tags. You can filter the entries in this table by tag, just the same as filtering them by tag
for display in tabs.
