---
from: docs.plugin_index
description: Track and discover changes across the various versions of your library or application.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974267/plugins/changelog.jpg
    alt: Changelog
    caption: Photo by Baron Fig on Unsplash
tags:
    - docs
    - publication
---

## About

The Changelog plugin tracks changes by adding files in the `changelog/` directory. The filename is used as the version
name by default, but it can also be set manually within the file's Front Matter as the `version` property. You can also 
set in the Front Matter whether a version is considered a "major" or a "minor" version. The content of the page is then
taken as the release notes for that version.

This plugin also generates a JSON file, `meta/versions.json`, which includes a listing of all the major versions of your
project (you can optionally have it include minor versions as well). Sites which archive each version in a different URL
root may wish to have their theme try to locate the latest `meta/versions.json`, and use Javascript to display those 
versions. This way, older versions of your project's documentation can create a link to the documentation on newer 
versions.

You may also include the `changelog` component on any page to render a listing of all versions with their release notes.

## Demo

- {{anchor('Changelog Component', 'Changelog')}}
- [versions.json]({{ 'meta/versions.json'|baseUrl }})

## Usage

### Basic Usage
Changelog versions are created as files sitting in the `changelog/` directory of your site content. The filenames should
be the version name, and they can be put in subdirectories for better organization. Alternatively, you can name the 
files something different and set the `version` property in each entry's Front Matter.

The files are processed with the 
same rules for other site content, where the file extension determines how to process the content. The content of the 
file is the release notes for that version.

**Example Changelog Directory Structure**
```text
. / (resources root)
├── homepage.md
├── config.yml
└── changelog/
    ├── 0.1/
    |   └── 0.1.0.md
    ├── 0.2/
    |   └── 0.2.0.md
    |   └── 0.2.1.md
    ├── 1.0/
    |   └── 1.0.0.md
    |   └── 1.0.1.md
    |   └── 1.1.0.md
    └── 2.0/
        └── 2.0.0.md
```

**Example Changelog entry**
```markdown
// changelog/1.0.0.md 
---
---

This project is now stable and ready for production use!

# New Features

...

# Breaking Changes

...
```

Changelog entries are ordered according to the versions' [Semantic Versioning](https://semver.org), but are limited to
just major, minor, and patch versions.

Bumps between changelog versions are detected automatically. 

### Customizing `versions.json`

Orchid creates a `meta/versions.json` file, which is an index of the "important" versions that others would need to know
about. Major versions are added to `versions.json` by default. Each entry contains the version number and whether that version
was a major, minor, or patch change or not.

You can also have minor versions added to this index by adding `includeMinorVersions: true` to your `config.yml`:

```yaml
changelog:
  includeMinorVersions: true
```

In addition, you can have the release notes for each version added included with each entry in `versions.json` by adding
`includeReleaseNotes: true` to your `config.yml`. The release notes are the compiled content of each entry:

```yaml
changelog:
  includeReleaseNotes: true
```