---
description: 'Get started with Orchid using Gradle, Maven, or kscript'
---

## Start a new Orchid project

The simplest way to get started with Orchid is to use the Orchid Starter repo as a base. 

1) `git clone https://github.com/JavaEden/OrchidStarter.git`
    * Clone the Starter repo anywhere you want on your system
2) `cd OrchidStarter`
    * Navigate into the directory containing the Starter repo
3) `./gradlew orchidServe`
    * Run Orchid using the included Gradle wrapper. No complicated and brittle gemfiles, NPM packages, or anything else 
    required. As long as you have a Java JDK installed, Orchid works right out-of-the-box without any configuration or
    system packages to install.
    * All available commands:
        - `gradle orchidBuild` - Build Orchid once then exit.
        - `gradle orchidWatch` - Watch Orchid for changes, rebuilding whenever a file changes. 
        - `gradle orchidServe` - Start a local development server to view your output site. Also watches Orchid for changes, rebuilding whenever a file changes.
        - `gradle orchidDeploy` - Build Orchid once, deploy the built site through the publication pipeline, then exit.
        - `gradle assemble` - Not strictly an Orchid command, but if you are set up with the OrchidJavadoc plugin, this will run Orchid from the Javadoc tool instead of generating the standard Javadocs. 

## Deploy to Netlify
    
Alternatively, you can simply click the "Deploy to Netlify" button below to automatically clone, build, and deploy the 
Starter repo to the Netlify CDN. 

[![Deploy to Netlify](https://www.netlify.com/img/deploy/button.svg)](https://app.netlify.com/start/deploy?repository=https://github.com/JavaEden/OrchidStarter)
    
## Integrate Orchid into an existing project

The Starter repo is great if you are setting up Orchid as a standalone website, but Orchid was designed to be integrated
into any project. Orchid can be set up from Gradle, Maven, or started manually through scriptlets or from another 
application.

### Gradle

To use Orchid from a Gradle project, setup your project's build.gradle file like so:

```groovy
plugins {
    // Add the official Orchid Gradle plugin so you can use Orchid with the custom DSL   
    id "com.eden.orchidPlugin" version "{{site.version}}"
}

repositories {
    // Orchid uses dependencies from both Jcenter and Jitpack, so both must be included. jcenter also includes 
    // everything available from MavenCentral, while Jitpack makes accessible any Github project.
    jcenter()
    maven { url "https://kotlin.bintray.com/kotlinx" }
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Add an Orchid Bundle. OrchidAll comes with all official themes included.
    // You must include a theme separately when using the OrchidBlog bundle.
    // Any additional plugins may be added as dependencies here as well.
    orchidRuntime 'io.github.javaeden.orchid:OrchidAll:{{site.version}}'
}

orchid {
    // Theme is required
    theme   = "{theme}"
    
    // The following properties are optional
    version = "${project.version}"
    baseUrl = "{baseUrl}"                         // a baseUrl prepended to all generated links. Defaults to '/'
    srcDir  = "path/to/new/source/directory"      // defaults to 'src/orchid/resources'
    destDir = "path/to/new/destination/directory" // defaults to 'build/docs/orchid'
    runTask = "build"                             // specify a task to run with 'gradle orchidRun'
}
```

You can now run Orchid in the following ways:

1) `./gradlew orchidRun` - Runs an Orchid task. The `runTask` should be specified in `build.gradle` or passed as a 
    Gradle project property (`-PorchidRunTask=build`). The task `help` will show a list of all tasks that can be 
    run given the plugins currently installed.
2) `./gradlew orchidBuild` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in 
    `build/docs/orchid` unless the output directory has been changed. You can then view the site by starting any HTTP 
    file server in the root of the output directory, or deploy this folder directly to your webserver.
3) `./gradlew orchidWatch` - Runs the Orchid build task a single time, then begins watching the source directory for 
    changes. Anytime a file is changes, the build will run again, and the resulting Orchid site will be in 
    `build/docs/orchid` unless the output directory has been changed.
4) `./gradlew orchidServe` - Sets up a development server and watches files for changes. The site can be viewed at 
    `localhost:8080` (or the closest available port).
4) `./gradlew orchidDeploy` - Runs the orchid build, then deploys it using Orchid's [deployment pipeline](https://orchid.netlify.com/wiki/user-manual/deployment/publication-pipeline)
    You can create and run your own deployment scripts, create a release on Github from changelogs, or publish the site 
    directly to Github Pages or Netlify.
    
_On windows, all the above commands need to be run with `gradlew` instead of `./gradlew`._

The Orchid Gradle plugin adds a new configuration and content root to your project, in the `src/orchid` directory 
(you may have to create this folder yourself). All your site content sits in `src/orchid/resources`, and any 
additional classes you'd like to include as a private plugin can be placed in `src/orchid/java`.

### Maven

To use Orchid from a Maven project, setup your project's pom.xml file like so:

```xml
<project>
    ...
    
    <properties>
        <orchid.version>{{site.version}}</orchid.version>
    </properties>

    <build>
        <plugins>
            <!-- Add the official Orchid Gradle plugin so you can use Orchid with the custom DSL -->
            <plugin>
                <groupId>io.github.javaeden.orchid</groupId>
                <artifactId>orchid-maven-plugin</artifactId>
                <version>${orchid.version}</version>

                <!-- Add an Orchid Bundle. OrchidAll comes with all official themes included.
                     You must include a theme separately when using the OrchidBlog bundle.
                     Any additional plugins may be added as dependencies here as well. -->
                <dependencies>
                    <dependency>
                        <groupId>io.github.javaeden.orchid</groupId>
                        <artifactId>OrchidAll</artifactId>
                        <version>${orchid.version}</version>
                    </dependency>
                </dependencies>

                <configuration>
                    <!-- Theme is required -->
                    <theme>${theme}</theme>
                    
                    <!-- The following properties are optional -->
                    <version>${project.version}</version>
                    <baseUrl>${baseUrl}</baseUrl>                        <!-- a baseUrl prepended to all generated links. Defaults to '/' -->
                    <srcDir>path/to/new/source/directory</srcDir>        <!-- defaults to 'src/orchid/resources' -->
                    <destDir>path/to/new/destination/directory</destDir> <!-- defaults to 'target/docs/orchid' -->
                    <runTask>build</runTask>                             <!-- specify a task to run with 'mvn orchid:run' -->
                </configuration>
            </plugin>
        </plugins>
    </build>

    <!-- Orchid uses dependencies from both Jcenter and Jitpack, so both must be included. jcenter also includes 
         everything available from MavenCentral, while Jitpack makes accessible any Github project. -->
    <pluginRepositories>
        <pluginRepository>
            <id>jcenter</id>
            <name>bintray-plugins</name>
            <url>http://jcenter.bintray.com</url>
        </pluginRepository>
        <pluginRepository>
            <id>kotlinx</id>
            <url>https://kotlin.bintray.com/kotlinx</url>
        </pluginRepository>
        <pluginRepository>
            <id>jitpack</id>
            <url>https://jitpack.io</url>
        </pluginRepository>
    </pluginRepositories>
</project>
```

You can now run Orchid in the following ways:

1) `./mvn orchid:run` - Runs an Orchid task. The `runTask` property should be specified in `pom.xml` or passed as a 
    Maven system property (`-Dorchid.runTask=build`). The task `help` will show a list of all tasks that can be 
    run given the plugins currently installed.
2) `./mvn orchid:build` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in 
    `target/docs/orchid` unless the output directory has been changed. You can then view the site by starting any HTTP 
    file server in the root of the output directory, or deploy this folder directly to your webserver.
3) `./mvn orchid:watch` - Runs the Orchid build task a single time, then begins watching the source directory for 
    changes. Anytime a file is changes, the build will run again, and the resulting Orchid site will be in 
    `build/docs/orchid` unless the output directory has been changed.
4) `./mvn orchid:serve` - Sets up a development server and watches files for changes. The site can be viewed at 
    `localhost:8080` (or the closest available port).
4) `./mvn orchid:deploy` - Runs the Orchid build, then deploys it using Orchid's [deployment pipeline](https://orchid.netlify.com/wiki/user-manual/deployment/publication-pipeline)
    You can create and run your own deployment scripts, create a release on Github from changelogs, or publish the site 
    directly to Github Pages or Netlify.
    
### kscript

If you're using Orchid to build a standalone site (not integrated as the docs for another project in the same repo), a 
full Gradle or Maven setup may be a bit overkill. Instead, you may use a tool like 
[kscript](https://github.com/holgerbrandl/kscript) to bootstrap and run Orchid yourself with a more minimalistic project 
structure. The basic API below is specifically created for kscript, but can be easily adapted for other JVM scripting
tools, or used like a library and started from another application.

```kotlin
@file:MavenRepository("kotlinx", "https://kotlin.bintray.com/kotlinx")
@file:MavenRepository("jitpack", "https://jitpack.io")

@file:DependsOn("io.github.javaeden.orchid:OrchidAll:{{site.version}}")

import com.eden.orchid.Orchid
import com.eden.orchid.StandardModule

val flags = HashMap<String, Any>()

// Theme is required
flags["theme"] = "{theme}"

// The following properties are optional
flags["version"] = "{{site.version}}"
flags["baseUrl"] = "{baseUrl}"                         // a baseUrl prepended to all generated links. Defaults to '/'
flags["srcDir"]  = "path/to/new/source/directory"      // defaults to './src'
flags["destDir"] = "path/to/new/destination/directory" // defaults to './site'
flags["runTask"] = "build"                             // specify a default task to run when not supplied on the command line

val modules = listOf(StandardModule.builder()
        .args(args) // pass in the array of command-line args and let Orchid parse them out
        .flags(flags) // pass a map with any additional args
        .build()
)
Orchid.getInstance().start(modules)
```

You can now start Orchid directly with its CLI, using the following commands:

1) `kscript ./path/to/scriptlet.kts <task> [--<flag> <flag value>]` - Runs an Orchid task by name. Additional parameters
    may be specified after the task name like `--theme Editorial`, which take precedence over the default values 
    specified in the scriptlet. The default tasks are:
    1) `build` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in 
        `build/docs/orchid` unless the output directory has been changed. You can then view the site by starting any 
        HTTP file server in the root of the output directory, or deploy this folder directly to your webserver.
    2) `.watch` - Runs the Orchid build task a single time, then begins watching the source directory for changes. 
        Anytime a file is changes, the build will run again, and the resulting Orchid site will be in 
        `build/docs/orchid` unless the output directory has been changed.
    3) `serve` - Sets up a development server and watches files for changes. The site can be viewed at `localhost:8080` 
        (or the closest available port).
    4) `deploy` - Runs the Orchid build, then deploys it using Orchid's [deployment pipeline](https://orchid.netlify.com/wiki/user-manual/deployment/publication-pipeline)
        You can create and run your own deployment scripts, create a release on Github from changelogs, or publish the
        site directly to Github Pages or Netlify.
2) `kscript ./path/to/scriptlet.kts help` - Print out basic usage and all available tasks and command-line options. 

### sbt

Your sbt project should look something like this:

     amazeballs/
          |
          |—— build.sbt
          |
          |—— src/
          |    |
          |    +—— main/
          |          |
          |          +—— scala/
          |          |     |
          |          |     +—— Amazeballs.scala
          |          |
          |          +—— orchid/
          |                 |
          |                 +—— resources/  <== ORCHID SOURCE FILES GO HERE
          |                         |
          |—— project/              +- homepage.md
                 |
                 +—— build.properties
                 |
                 +—— plugins.sbt
		 

If you wish to integrate orchid into an `sbt` project, you'll use the Orchid sbt plugin. To install it
in your project, you'll need to ensure that at least the following is included in your `project/plugins.sbt`:

```
resolvers += Resolver.jcenterRepo // hosts Orchid and its components

addSbtPlugin( "io.github.javaeden.orchid" % "sbt-orchid" % "{{site.version}}" )
```

(You will usually want to include a bit more than this in `project/plugins.sbt`. The much richer `project/plugins.sbt` example [below](#rich-pluginssbt-example) is a better starting point.)

Then place the source files for your Orchid site in `src/main/orchid/resources`.

Now, on the sbt command line you can run:
1. `orchidBuild` - Runs the Orchid build task a single time then completes. The resulting Orchid site will be in 
   `target/orchid` unless the `orchidDestination` setting has been customized. You can then view the site by starting any 
    HTTP file server in the root of the output directory, or deploy this folder directly to your webserver.
2. `orchidWatch` - Runs the Orchid build task a single time, then begins watching the source directory for changes. 
   Anytime a file is changes, the build will run again, and the resulting Orchid site will be in 
   `target/orchid` unless the `orchidDestination` setting has been customized.
3. `orchidServe` - Sets up a development server and watches files for changes. The site can be viewed at `localhost:8080` 
   (or the closest available port).
4. `orchidDeploy` - Runs the Orchid build, then deploys the generated site using Orchid's [deployment pipeline](https://orchid.netlify.com/wiki/user-manual/deployment/publication-pipeline)
    You can create and run your own deployment scripts, create and release on Github from changelogs, or publish the
    site directly to Github Pages or Netlify.

#### sbt configuration

In your project's `build.sbt` file, you will usually want to configure an Orchid theme. That's just:
```
orchidTheme := "BsDoc"
```

However, for this to work, you will need to make sure the theme and any other features
your site relies upon are available to the build. Orchid offers a very rich feature set, made available via distinct, dynamically loaded dependencies.
In order to use these fetures, you'll want to add them as dependies *of the build, not your project*.

The easiest way to do this is just include these dependencies in your `project/plugins.sbt` file.
Below is a very rich example `project/plugins.sbt` file. You can use any of the main Orchid features
simply by uncommenting the associated dependencies. For the `BsDoc` theme to be made available, for example,
you'd want to uncomment the line containing `libraryDependencies += orchidComponent( "OrchidBsDoc" )`.

##### rich `plugins.sbt` example

```scala
resolvers += Resolver.jcenterRepo // hosts Orchid and its components

val OrchidVersion = "{{site.version}}"

addSbtPlugin("io.github.javaeden.orchid" % "sbt-orchid" % OrchidVersion)

/*
 *  Add desired Orchid components to the build
 */
 
def orchidComponent( name : String ) = "io.github.javaeden.orchid" % name % OrchidVersion

/*
 *  The plugin includes OrchidCore already as a dependency,
 *  but explicitly specifying it helps ensure version consistency
 *  with other components.
 */
 
libraryDependencies += orchidComponent( "OrchidCore" )

/*
 *  Uncomment the components you desire
 */

/* Themes -- see https://orchid.netlify.com/themes */
/* Don't forget to set 'orchidTheme' in build.sbt! */

// libraryDependencies += orchidComponent( "OrchidBsDoc" )
// libraryDependencies += orchidComponent( "OrchidCopper" )
// libraryDependencies += orchidComponent( "OrchidEditorial" )
// libraryDependencies += orchidComponent( "OrchidFutureImperfect" )

/* Plugins -- see https://orchid.netlify.com/plugins */

// libraryDependencies += orchidComponent( "OrchidPages" )
// libraryDependencies += orchidComponent( "OrchidPosts" )
// libraryDependencies += orchidComponent( "OrchidPluginDocs" )

// libraryDependencies += orchidComponent( "OrchidAsciidoc" )
// libraryDependencies += orchidComponent( "OrchidAzure" )
// libraryDependencies += orchidComponent( "OrchidBible" )
// libraryDependencies += orchidComponent( "OrchidBitbucket" )
// libraryDependencies += orchidComponent( "OrchidChangelog" )
// libraryDependencies += orchidComponent( "OrchidDiagrams" )
// libraryDependencies += orchidComponent( "OrchidForms" )
// libraryDependencies += orchidComponent( "OrchidGithub" )
// libraryDependencies += orchidComponent( "OrchidGitlab" )
// libraryDependencies += orchidComponent( "OrchidGroovydoc" )
// libraryDependencies += orchidComponent( "OrchidJavadoc" )
// libraryDependencies += orchidComponent( "OrchidKSS" )
// libraryDependencies += orchidComponent( "OrchidKotlindoc" )
// libraryDependencies += orchidComponent( "OrchidNetlify" )
// libraryDependencies += orchidComponent( "OrchidNetlifyCMS" )
// libraryDependencies += orchidComponent( "OrchidPresentations" )
// libraryDependencies += orchidComponent( "OrchidSearch" )
// libraryDependencies += orchidComponent( "OrchidSwagger" )
// libraryDependencies += orchidComponent( "OrchidSwiftdoc" )
// libraryDependencies += orchidComponent( "OrchidSyntaxHighlighter" )
// libraryDependencies += orchidComponent( "OrchidTaxonomies" )
// libraryDependencies += orchidComponent( "OrchidWiki" )
// libraryDependencies += orchidComponent( "OrchidWritersBlocks" )

```
