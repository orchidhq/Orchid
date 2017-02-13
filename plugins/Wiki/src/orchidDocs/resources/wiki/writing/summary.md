The Summary page creates an index page for your entire wiki. There is no structure to how your Summary needs to be set
up, it is just a page with links to each Wiki page. The pages of the Wiki are navigable, showing 'previous' and 'next'
links if your theme supports it, and the order these pages are created is the same order as they are declared within the
Summary.

The Summary is parsed after its HTML has been generated, with the text of the link becoming the page's title and the 
`href` being the location of the source content within the base wiki directory. For example, look at the `SUMMARY.md` 
file below:

```

# Get Started

### [Get Started](get_started.md)

# Pages

### [Page One](pages/one.md)
### [Page Two](pages/two.md)
### [Page Three](pages/three.md)

```

This Summary has four pages, denoted by four Markdown links. These pages are titled 'Get Started', 'Page One', 'Page Two',
and 'Page Three'. The content for these files exists within the `wiki/` directory of your resources (or wherever you
have set the `-wikiPath` option), and are given relative to the root, so it expects the following folder structure:

```
resources
│   ...    
└───wiki
│   │   SUMMARY.md
│   │   GLOSSARY.md
│   │   get_started.md
│   └───pages
│       │   one.md
│       │   two.md
│       │   three.md
```

The output of these pages get written as an `index.html` file within the folder structure declared by the link, including
the filename but excluding the extension. So Orchid Wiki would generate the following routes:

```
/wiki/get_started/
/wiki/pages/one/
/wiki/pages/two/
/wiki/pages/three/
```

See the next page for how to write individual pages within your Wiki.