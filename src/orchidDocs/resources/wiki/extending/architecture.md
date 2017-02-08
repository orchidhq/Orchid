A look at the entry point to the entire Orchid site generation process reveals a lot about the core architecture:

```java
public static boolean start(RootDoc rootDoc) {
    Orchid.rootDoc = rootDoc;

    pluginScan();
    optionsScan(rootDoc.optionsMap());
    if(shouldContinue()) {
        indexingScan();
        generationScan();
        generateHomepage();
        return true;
    }
    else {
        return false;
    }
}
```

You can see it is pretty simple, overall, taking only 6 steps to produce a beautiful site:

1) `pluginScan()`: Scan the classpath for all classes annotated with @AutoRegister. Create an instance of each class, 
ensuring it is able to run its static initializer and register itself, and build an index of all components which will
be used in the rest of the process.
2) `optionsScan(rootDoc.optionsMap())`: Parse the command-line optionsMap using `Option`s registered in step `. These set up 
properties like source and destination directories, build environments, library version, and baseUrl for resolving links.
3) `shouldContinue()`: Perform a sanity-check to ensure all required compoenents were properly registered before attempting
to continue. This requires, at the very least, a theme that exists and a destination directory, but may also take into
account optionsMap or components required by the chosen theme.
4) `indexingScan()`: Scan all registered `Generator`s and determine all files they _intend_ to generate. Ideally, no 
files should be written until after this has completed, to ensure that all possible site data is able to be linked to 
on every page generated.
5) `generationScan()`: Scan all registered `Generator`s a second time and allow them to generate their output files.
They should have access in the `index` property to any file that has been or will be generated.
6) `generateHomepage()`: Delegated directly to the theme, generate the homepage(s) for your site. This ties everything 
together at the root of your site, making it feel like a true and proper home for your documention, every time. The 
The homepage does not get 'indexed' because it is understood to always exist at the project root.