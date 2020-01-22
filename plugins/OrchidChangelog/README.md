---
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

### Directory-Based (multi-file)

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

### File-Based (single file)

Singe version 0.18.2, Orchid now supports single-file changelog formats such as 
[Keep A Changelog](https://keepachangelog.com/en/1.0.0/). In such formats, all versions of the software are documented 
in a single file, usually in the repository root in a file called `CHANGELOG.md` or something similar. OrchidChangelog
will locate this file, parse the individual versions from it based on a specified header regex, and construct your site
changelogs from these entries.

The following configuration will change from the default directory-based changelogs to the file-based format:

```yaml
changelog:
  adapter: 
    type: "file"           # required
    baseDir: "./docs"      # optional, change the directory to search for the changelog file in. Defaults to resources root dir
    filename: "CHANGES.md" # optional, the name of the file to look for
    versionRegex: '...'    # optional, change the regex used to locate version header lines in the file. Defaults to Markdown headers of level 1 or 2
```

The default syntax for a Changelog version header is a Markdown header of level 1 or 2, which contains the version name
and an optional release date in `YYYY-MM-DD` format. Versions are usually listed in the file in reverse-chronological 
order.

```markdown
## 1.1.0

- Unreleased version

## 1.0.0 - 2017-08-20

- Major version release

## 0.2.1 - 2017-07-09

- Patch version release

## 0.2.0 - 2017-07-08

- Minor version release

## 0.1.0 - 2017-06-20

- Initial release
```

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

