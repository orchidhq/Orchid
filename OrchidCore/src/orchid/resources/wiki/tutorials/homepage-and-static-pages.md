---
---

## Introduction

In the previous tutorial, we learned about how to create a new Orchid project and how to start it from the command line.
In this tutorial we will start to add our first content to our homepage and learn about the most basic, but one of the
most flexible plugins: Static Pages.

Before continuing, make sure you have followed along with the first tutorial and have started your local Orchid server 
with `gradle orchidServe`. We will be building on that example in this tutorial.

You can follow along with this tutorial on your own, or find the source for this in the 
[OrchidTutorials repository](https://github.com/JavaEden/OrchidTutorials/tree/master/02). 

## Your Homepage

Looking at your first Orchid site, you'll notice that it is a bit plain. It's just an empty page with no menus, no 
content, nothing interesting at all. But let's change that!

Every Orchid site includes a Homepage. This is first page that visitors will see when they go to the root of your 
website, and is always created even if you don't include any other plugins in your Orchid build. Let's start by adding a
new file in your project at `src/orchid/resources/homepage.md` and adding the following content to it. 

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

# Adding Static Pages

## Example: Small Business

Having a homepage on your site is great and all, but you've probably got a lot more content that you want to show. There 
are many ways you can add additional pages to your Orchid site, but the easiest way is with the Static Pages plugin. 

Let's say you are running a small business which has multiple locations throughout Texas: one in Houston, one in Dallas,
and one in Austin. Each location should have its own page which lists its name, address, phone number, and business 
hours. You want these pages to be in your website at `/locations/{locationName}`, and also to have an "index" page 
showing all locations so each one can be easily located. So you want your site to have the following pages:

- `/`
- `/locations`
- `/locations/houston`
- `/locations/dallas`
- `/locations/austin`

Let's see how we can use the Static Pages plugin to make this site.

## Adding Location Pages

We already have our Homepage set up at `/`, so let's go ahead and set up our Houston location page first. We'll start by
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

## Business Hours

M-F: 6am - 9pm
Sa: 6am - 10pm
Su: Closed
{% endhighlight %}

While we're at it, lets go ahead and do similar things for our Dallas and Austin locations as well. Copy the
`houston.md` file to `dallas.md` and `austin.md`, change the content inside these files, and in just a moment Orchid
will have rebuilt your site and included these new pages for us. 

You can now view the pages for each business location at 
[http://localhost:8080/locations/houston](http://localhost:8080/locations/houston), 
[http://localhost:8080/locations/dallas](http://localhost:8080/locations/dallas), and 
[http://localhost:8080/locations/austin](http://localhost:8080/locations/austin)!

## Adding Location Index Page

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

# Conclusion

Your Orchid site is starting to look pretty good, but lets review what we did:

1. Orchid keeps most of its files in `src/orchid/resources/`. One special file in that folder, `homepage.md` can be 
    added so that Orchid can build its front page with it. 
2. You can add any files you want to `src/orchid/resources/pages`, and Orchid will copy them over to your final site in 
    that same structure. You can also name special files here `index`, and they will be kept at the index of that folder 
    in the final site rather than in a sub-directory named index, which can be used to create special landing pages. 
3. The homepage and all static pages are processed based on their file extension. Use the `.md` extension to process the
    file as Markdown, and `.ad` to process it as Asciidoc. In fact, you'll find that _any_ page from _any_ plugin will 
    be processed in a similar manner, so that using all plugins will feel very similar.
    
Stay tuned for more tutorials on how to use Orchid. In the next tutorial, I will show you how to improve this small 
business by making the links dynamic, and also adding breadcrumbs and a customizing the locations page content to 
improve consistency across your site and make it easier to navigate.