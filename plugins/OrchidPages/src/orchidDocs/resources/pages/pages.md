---

title: Creating Pages
root: true

---

Pages within your wiki can be written just as any other kind of content. The file extension determines which compiler
should be used to process the content within, and any compiler can be used with Orchid Pages, so long as it has been
registered properly. No restrictions have been placed on the content or extension of a file within your pages.

Data from the rest of the Orchid build can be injected into your Pages pages by using YAML Front Matter. Refer to the 
[Orchid documentation](#) for more about YAML Front Matter, how it works within Orchid, and the kinds of data that
can be accessed within Front Matter.

In addition to the normal variables available in Front Matter, the following properties are also available:

* `title`: Sets the title of a page
* `root`: Set to `true` if you want the page to show in the root of a site. This would turn a URL of 
`www.example.com/pages/pageOne/` into `www.example.com/pageOne/`. 