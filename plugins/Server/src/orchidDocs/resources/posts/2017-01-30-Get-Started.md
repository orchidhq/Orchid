---

title: Get Started

---

Getting set up with Orchid Posts is simple. Refer to the [Orchid documentation](#) for help getting set up with Orchid 
before continuing.

Adding Orchid Posts is as simple as adding the plugin to your dependencies in `build.gradle`. The plugin will 
automatically be registered and start creating your static pages.

```
dependencies {
    ...
    orchidDocsCompile 'com.github.JavaEden:OrchidPosts:{{options.v}}'
}
```

The next step is to start adding content to your wiki. Create a `/posts` folder in your resources directory. All posts 
must exist within this directory, but can be placed in any subdirectories. 

Posts should follow the naming convention used by Jekyll. That is, the name of a file within the `posts` directory 
contains both the date of publication shown on the site, and also the title of the blog post in the following format:
`YYYY-MM-DD-<title with spaces replaced by dashes>.md`, where the extension should match the format you are using.