---
official: true
description: Create beautiful documentation for your Swift source code within Orchid.
images:
  - src: http://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1525466545/plugins/swiftdoc.jpg
    alt: Swiftdoc
    caption: Photo by OOI JIET on Unsplash
---

### Using Orchid to document Swift code

Documenting Swift code within Orchid depends on the [SourceKitten](https://github.com/jpsim/SourceKitten) command line
tool, which itself requires the full XCode environment to be installed on your system (not just the xcode command-line 
tools).

Orchid takes the source swift files and calls to the `sourcekitten` executable on your machine to get back a JSON 
representation of your source code. It then takes that model and generates pages for every class, struct, enum, 
protocol, and global. It also links typealiases and extensions to the classes they relate to, and also points each of 
the above types to a page listing the various code elements within that source file.