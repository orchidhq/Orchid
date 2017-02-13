Writing extensions is very easy. Just create a project which includes Orchid-Core as a compile dependency, then create
any number of classes that implement `Generator`, `Option`, or `Compiler` and have the `@AutoRegister` annotation. The 
classes below will automatically be registered and used in their appropriate place.

* `Generator`: - A class which both adds items to the site index tree, and renders pages in the resulting site. Any page
generated from Orchid Core or official Extensions (with the exception of HomePages) comes directly from Generator implementations.
* `Option`: - A class which handles a single command-line option. Every option passed to Orchid, including setting the 
Theme and setting input and output directories, is an Option implementation.
* `Compiler`: - A class which transforms content from one format to another. All internal features, including the Twig
and Markdown processing, are implemented as Compilers

Other extensions, such as plugins for particular Compilers (like Flexmark extensions) should still use the `@AutoRegister`
annotation, but since they are not core classes they must manually register themselves. The class's static initializer
block is a good place for `@AutoRegister`ed classes to do this.

Additional Core types will be added soon, enabling features like just-in-time global content transformation (independent 
of compiler, such as for highlighting specific information on any output page), custom javadoc-comment parsing, and 
theme widgets, for embedding rich content into any page.