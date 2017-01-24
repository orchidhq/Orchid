It is possible to create generators that are only used in your project without creating a publishing separate library just
to display your one special content type. With the OrchidDocs configuration, you can set up your `build.gradle` such that
you build an additional jarfile containing the classes in your OrchidDocs java project, and then that jar is included in
the Orchid build. This makes it easy to manage special documentation formats that take advantage of the structure and 
comments of your code, making it very easy to keep up-to-date.