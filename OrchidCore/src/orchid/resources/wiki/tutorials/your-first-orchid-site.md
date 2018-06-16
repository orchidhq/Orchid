---
tutorialId: '01'
---

## Introduction

So you've decided to give Orchid a try? Great! Follow along with this guide to get your first Orchid site set up, and
learn the basics of what you can do with Orchid.

You can follow along with this tutorial on your own, or find the source for this in the 
[OrchidTutorials repository](https://github.com/JavaEden/OrchidTutorials/tree/master/{{tutorialId}}). 

## First Steps

Orchid is a tool build in Java and Kotlin, and is designed to be run from Gradle. Gradle is a build tool that uses 
Groovy scripts to configure your build. While it is a super-powerful tool, it can be pretty complex to use and configure
on its own, which is why Orchid has an official Gradle plugin, which makes it easy for you to get up an running with 
Orchid, even if you aren't familiar with Gradle. 

### Install Gradle

To start, we'll need to install Gradle. If you're on a Mac, installing Gradle is easily done with Homebrew.

{% highlight 'bash' %}
# Install Homebrew
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

# Use Homebrew to install Gradle 
brew install gradle
{% endhighlight %}

If you're on another operating system, you can find the steps to install it for your system on Gradle's 
[installation page](https://gradle.org/install/).

### Create a Gradle project

Now that you've got Gradle installed, let's go to an empty directory where you want to work on your Orchid site.

{% highlight 'bash' %}
cd ~/Documents/personal/orchid
{% endhighlight %}

In this empty folder, run the following command, which will initialize a Gradle project for you and set up all the files 
and folders it needs.

{% highlight 'bash' %}
gradle init --type java-library
{% endhighlight %}

This will make your project look like:

{% highlight 'bash' %}
|__.gradle/
|__gradle/
|__gradlew
|__build.gradle
|__gradlew.bat
|__settings.gradle
|__src
|____test/
|____main/
{% endhighlight %}

### Add Orchid to your Gradle project

Now that we've got our Gradle project set up, let configure it to be able to run Orchid. First, we'll need to open up
`build.gradle` and find the `plugins` section. 

{% highlight 'groovy' %}
plugins {
    // Apply the java-library plugin to add support for Java Library
    id 'java-library'
}
{% endhighlight %}

Let's add the `com.eden.orchidPlugin` plugin to this block.

{% highlight 'groovy' %}
plugins {
    // Apply the java-library plugin to add support for Java Library
    id 'java-library'
    id 'com.eden.orchidPlugin' version '{{site.version}}'
}
{% endhighlight %}

By itself, the Orchid Gradle Plugin isn't quite enough to get Orchid running. We'll also need to tell Gradle:

1) The Orchid plugins and themes we want to include in our site
2) Where it can find those plugins and themes
3) Some basic configuration values so Orchid can run properly

To do this, add the following lines to at the end of `build.gradle`.

{% highlight 'groovy' %}
// 1. Include all official Orchid plugins and themes
dependencies {
    orchidCompile "io.github.javaeden.orchid:OrchidAll:0.8.10"
}

// 2. Get Orchid from Jcenter, Bintray, and Jitpack
repositories {
    jcenter()
    maven { url 'https://dl.bintray.com/javaeden/Orchid/' }
    maven { url 'https://jitpack.io' }
}

// 3. Use the 'BsDoc' theme, and view the site locally at 'http://localhost:8080'
orchid { 
    theme = "BsDoc"
    baseUrl = "http://localhost:8080"
}
{% endhighlight %}

### Run Orchid

You now have everything you need to run Orchid and view the site. Go back to your terminal and run the following command
to start Orchid and have it host your site with its local HTTP server so you can preview it.

{% highlight 'bash' %}
gradle orchidServe
{% endhighlight %} 

After a few seconds, you should be able to visit [http://localhost:8080](http://localhost:8080) in your browser to see 
your very first Orchid site!

## Your Homepage

You'll notice that your first site is a bit plain. It's just an empty page with no menus, no content, nothing 
interesting at all. But let's change that!

Every Orchid site includes a Homepage. This is first page that visitors will see when they go to your website, and is
always created even if you don't include any other plugins in your Orchid build. Let's start by adding a new file in 
your project at `src/orchid/resources/homepage.md` and adding the following content to it. 

{% highlight 'text' %}
## Hello, Orchid

> You are beautiful, and so is your website.

Let's build something _beautiful and unique_, **together**.
{% endhighlight %}

There we go, that's looking a bit better. We now have a Markdown file, and whatever you write in that file will be 
automatically converted to HTML and embedded inside your theme. 

But this file doesn't have to be Markdown. Orchid knows a lot of different languages, and you are free to use something
else if you wish, such as Asciidoc. By changing the file extension from `.md` to `.ad`, you will instruct Orchid to 
process this file as Asciidoc rather than Markdown. 

Let's do that now. Rename `src/orchid/resources/homepage.md` to `src/orchid/resources/homepage.ad` and change its 
contents to the equivalent in Asciidoc.

{% highlight 'text' %}
== Hello, Orchid

____
You are beautiful, and so is your website.
____

Let's build something _beautiful and unique_, *together*.
{% endhighlight %}

## Adding Static Pages

### Example: Small Business

Having a homepage on your site is great and all, but you've probably got a lot more content that you want to show. There
are many ways you can add additional pages to your Orchid site, but the easiest way is with the Static Pages plugin. 

Let's say you are running a small business which has multiple locations throughout Texas, one in Houston, one in Dallas, 
and one in Austin. Each location should have its own page which lists its address, phone number, and business hours. You 
want these pages to be in your website at `/locations/{locationName}`, and also to have an "index" page showing all 
locations so each one can be easily located. So you want your site to have the following pages:

- `/`
- `/locations`
- `/locations/houston`
- `/locations/dallas`
- `/locations/austin`

Let's see how we can use the Static Pages plugin to make this site.

### Adding Location Pages

We already have our Homepage set up at `/`, so lets go ahead and set up our Houston location page first. Let's start by
making a new file at `src/orchid/resources/pages/locations/houston.md`. This file works just the same as your Homepage's
file. You can write whatever content you want inside it, and it will be converted to HTML and embedded inside your theme
for you. Also, just like the Homepage, you can change its file extension to have Orchid process it as another language, 
if desired.

So let's add the following content to `src/orchid/resources/pages/locations/houston.md`.

{% highlight 'text' %}
## Location

Houston, TX

## Address
 
1234, Example Dr.
Houston, TX, 12345

## Phone

(123) 456-7890
{% endhighlight %}

While we're at it, lets go ahead and do similar things for our Dallas and Austin locations as well. Copy the `houston.md`
file to `dallas.md` and `austin.md`, change the content inside these files, and in just a moment Orchid will have 
rebuilt your site and included these new pages for us. 

You can now view the pages for each business location at 
[http://localhost:8080/locations/houston](http://localhost:8080/locations/houston), 
[http://localhost:8080/locations/dallas](http://localhost:8080/locations/dallas), and 
[http://localhost:8080/locations/austin](http://localhost:8080/locations/austin)!

### Adding Location Index Page

Now that we have a unique page for each of our locations, lets go ahead and create an index page which lists all of our
individual locations. As you may have already guessed, we need to create a new file at 
`src/orchid/resources/pages/locations.md`, and add the content in there. This is because the Static Pages plugin works
by taking _all_ files in your `src/orchid/resources/pages/` directory, and copies them into your final site at that same
path. 

However, if we were to build a really large site with static pages, it might get a bit confusing having the locations
index page in a different folder from the rest of the locations pages. So lets actually move 
`src/orchid/resources/pages/locations.md` to `src/orchid/resources/pages/locations/index.md`. The Static Pages plugin
will copy any file over directly, but for files named `index`, it will keep them at the root of that folder, rather than
making a sub-page in that folder. So `src/orchid/resources/pages/locations/index.md` will become `/locations` instead of
`/locations/index` like the other-named pages would.

We need to get this site out fast so we can start getting people visiting all locations, so let's just hardcode links to
each page for now. Add the following to `locations/index.md`:

{% highlight 'text' %}
## Our Locations

- [Houston](http://localhost:8080/locations/houston)
- [Dallas](http://localhost:8080/locations/dallas)
- [Austin](http://localhost:8080/locations/austin)
{% endhighlight %}

## Conclusion

Congratulations, you now have a basic Orchid site up and running! Let's go back over what we learned:

1) Orchid is a command-line tool that is run through Gradle. Orchid provides a plugin for Gradle so that it is easy to 
    get set up and running Orchid without needing to know much about Gradle.
2) Orchid keeps most of its files in `src/orchid/resources/`. One special file in that folder, `homepage.md` can be     
    added so that Orchid can build its front page with it. 
3) You can add any files you want to `src/orchid/resources/pages`, and Orchid will copy them over to your final site in
    that same structure. You can also name special files here `index`, and they will be kept at the index of that folder
    in the final site rather than in a sub-directory named index, which can be used to create special landing pages. 
4) The homepage and all static pages are processed based on their file extension. Use the `.md` extension to process the
    file as Markdown, and `.ad` to process it as Asciidoc. In fact, you'll find that _any_ page from _any_ plugin will 
    be processed in a similar manner, so that using all plugins will feel very similar.
    
Stay tuned for more tutorials on how to use Orchid. In the next tutorial, I will show you how to improve this small 
business by making the links dynamic, and also adding breadcrumbs and a customizing the locations page content to 
improve consistency across your site and make it easier to navigate.