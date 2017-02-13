---
---

Getting set up with Orchid Wiki is simple. Refer to the [Orchid documentation](#) for help getting set up with Orchid 
before continuing.

Adding Orchid Wiki is as simple as adding the plugin to your dependencies in `build.gradle`. The plugin will 
automatically be registered and start creating wiki pages.

```
dependencies {
    ...
    orchidDocsCompile 'com.github.JavaEden:OrchidWiki:{{options.v}}'
}
```

The next step is to start adding content to your wiki. Create a `/wiki` folder in your resources directory, and make sure
it has at least `GLOSSARY.md` and `SUMMARY.md`. Additional wiki pages must exist within this directory, but can be
placed in any subdirectories. 

If you'd like to change the path of your wiki, add a command-line option to change the wiki path. This option will 
change both the source resource directory of your wiki content as well as the output path. If this option is not specified, 
the wiki path defaults to 'wiki'.

```
orchid {
    ...
    cmdArgs.add('-wikiPath <new path>')
}
```