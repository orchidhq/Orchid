Creating themes is fundamentally the same as creating any other Extension: just create a project and give it a class 
extending the abstract `Theme` class. Themes do not need to be `@AutoRegister`ed because a single theme is declared and 
loaded as a command-line arg. A good way to get started developing themes in to copy one of the official Orchid themes
and go from there. In most cases, you will want to keep most of the templates the same, just changing the assets and 
structure of the layouts and pages, and creating additional CSS to handle the ClassDoc pages. Advanced Orchid theme developers
may want to get more creating with presenting Javadoc content.