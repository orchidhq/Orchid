---
classnames:
  - OptionsExtractor
  - OptionExtractor
---

Orchid has a novel and very powerful way of managing options that makes it easier for both developers and users. For 
developers, you can spend less time focusing on finding options, and also not be stuck with 
[Stringly-typed](http://wiki.c2.com/?StringlyTyped) options. For users, options are self-documenting, so you know you 
have access to all possible options and have insight into their default values, even if the documentation isn't 
up-to-date.

The core for Orchid's options processing is all implemented in [JavaEden/Common](https://github.com/JavaEden/Common), 
with Orchid and plugins contributing extensions that make it all easier to work with the specifics of Orchid options.

In a nutshell, the options-processing works like the following (each section is covered in more detail in other pages):

1) Run through all fields in a class and its parent classes and find the ones annotated with `@Option`. 
1) Gather additional data from all the Archetypes of a given class (Archetypes are declared as annotations on the class)
1) Merge Archetypal data with the data that is passed directly to the Extractor.
1) For each field:
    1) Get the option name, which is either a String named in the `@Option` annotation, or the field name itself. 
    1) Find the corresponding data in the full, merged data.
    1) Find an OptionExtractor that can handle the type of data that the field holds
    1) Pass the data to this OptionExtractor, which returns an instance that can be assigned to the field. If this value
        comes back as empty, get the default value from the OptionExtractor, which is often defined as a custom 
        annotation on that field.
    1) Assign the value received from the OptionExtractor to the field. This can be set through a Bean-style `setter`
        method, or by directly assigning the field.

The way options are found and assigned gives us several guarantees that make it very easy for developers to reason about
and use options:

1) An option is always assigned each Extraction. As some classes (such as singletons) have options extracted into them
    multiple times in a single Orchid run (typically once per build cycle), this guarantees that options are always 
    reset and nothing leaks from one extraction to the next.
1) Options are type-safe, and converted the the type of the field automatically, regardless of their format in the 
    actual declared options. For example, an integer field will always give a sensible Integer value: if the declared
    value is actually an int, it will be used as-is. If it is a String, the string will be parsed to an int. Likewise, 
    doubles, booleans, Java 8 Local Date, Time, and DateTimes, and many others are supported by default. 
1) Supporting new option types is as simple as creating a new OptionExtractor, and it then works everywhere. You no 
    longer have to bloat your code with the same type-coercion logic everywhere. 
1) An option looks up its data within its declared options, not the other way around. So we can safely set additional 
    properties for custom use outside of the Options processing, and we can also have multiple Options pull from the 
    same source data. There is no restriction on having unique option names when the names are set as Strings in the 
    `@Option` annotation.
1) Orchid knows all there is to bw known about every possible option, and is able to auto-generate documentation for 
    all these options. Orchid is privy to each option's result type and default value, and descriptions can be attached
    to each option as well via the `@Description` annotation. This also means that the auto-generated documentation is
    available during Orchid's runtime, so we tailor the documentation to only those classes that are relevant to 
    _your specific build_, and bring it all to you rather than forcing you to go find it yourself.
