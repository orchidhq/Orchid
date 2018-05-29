---
official: true
title: Orchid Changelog
description: Track and discover changes across the various versions of your library or application.
images:
  - src: http://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974267/plugins/changelog.jpg
    alt: Changelog
    caption: Photo by Baron Fig on Unsplash
---

### Using Orchid Changelog

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