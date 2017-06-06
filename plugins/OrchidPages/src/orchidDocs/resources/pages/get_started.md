---

title: Get Started
root: true

---

Getting set up with Orchid Pages is simple. Refer to the [Orchid documentation](#) for help getting set up with Orchid 
before continuing.

Adding Orchid Pages is as simple as adding the plugin to your dependencies in `build.gradle`. The plugin will 
automatically be registered and start creating your static pages.

```
dependencies {
    ...
    orchidDocsCompile 'com.github.JavaEden:OrchidPages:{{flags.v}}'
}
```

The next step is to start adding content to your wiki. Create a `/pages` folder in your resources directory. All pages 
must exist within this directory, but can be placed in any subdirectories. 

By default, the path of a page will be the path it has within the 'pages' directory, namespaced under '/pages/'. Pages
can also be placed into the root of the site by adding a Front Matter variable of `root: true` to any given page.