---
title: Soooo... What's the Deal with Orchid?
description: May 2021 Orchid News
tags: 
  - Orchid News
---

So a lot of you have been asking what's going on with Orchid. It's been nearly a year since I've pushed any activity to
the git repo. I haven't been active on Orchid's Gitter channel or on its issue tracker. And with JCenter shutting down,
where Orchid's artifacts are currently published, it seems like I've completely abandoned Orchid. 

This really was not my intention, and I don't want that to be the future of Orchid either. The simple answer to all this
is that my personal life got unexpectedly busy over the past year, and I just haven't had time to keep up with my open 
source projects that I'm not directly using for my job. But despite that, I do intend to revive Orchid, though I will
need some help in doing so. 

For the rest of this post, I'd like to share a bit of what's been going on in my personal life that have made my 
open-source contributions challenging, and what I envision for the future of Orchid. Or if you don't care about that, 
you can skip down to [the future of orchid](#the-future-of-orchid) or
[Orchid's proposed timeline](#orchids-proposed-timeline).

## What's Happening

I'll avoid going too deeply into details here, but I do want to describe the highlights of the past year for me:

1) Last April, I bought my first house. It's an older house, and I've had to put a lot of work into fixing it up.
2) In May, I adopted two adorable puppies, a Golden Retriever, and a similarly-sized mutt from a rescue. Yes, two puppies 
    at the same time. Everyone told me I was crazy. A year later, I think I'd have to agree with them. I love them to
    death, but wow is it exhausting raising and training two energetic puppies at once.
3) In June, I hit a large rock with my car in Colorado, destroying the transmission and taking nearly 2 months to fix. I 
    live in Texas. So I was without a car for 2 months, dealing with a terrible mechanic that took way too long to get 
    my car fixed, and driving to Colorado and back twice. Although I did get to drive a brand-new Ford Mustang as my 
    rental back home for the first trip (just a small silver lining on that situation).
4) In December, my wife became pregnant with our first child, a baby girl due in August.
4) And of course, the COVID situation has just exacerbated all these things. Even though I live in Texas, where we've 
    been mostly back to normal for most of the pandemic, there's a lot that is still made more challenging than it 
    needed to be because of the unnecessary restrictions.
   
On top of all of that, I'll just be honest and say that I've also been feeling a bit burned-out from open-source 
development. With as much time as I had spent on Orchid and my other projects, it was starting to feel like it was all 
in vain, that I was spending a lot of time helping folks get set up to use Orchid, and then they didn't stick with it. 
Add to that the JCenter shut-down, a completely rewritten version of Dokka (which would need a complete rewrite of the 
current integration to still be used by Orchid for later versions of Kotlin), and the fact that I had stopped using 
Orchid at work, and I was just continually losing heart and feeling like there was no way I could ever continue to keep
up with this little side-project of mine. 

OVer the last year, I've come to realize just how many people are actively using Orchid despite those bad interactions, 
and that is a huge encouragement to me. It's the main reason why I'm writing this article, and why I don't just want to 
let Orchid die. There are too many people that depend on it continuing to live, people that I'll never interact with 
personally, and I don't want to let you down because I know there are no other good options for those of you who have
chosen Orchid as the tool for your documentation sites.

So with all this, I never had any intention of abandoning Orchid. Orchid is my "baby", a project I've built from the 
ground up over years, and in many ways has helped me learn to a great degree the skills I use at my job every day. But I 
simply kept having things pop up over the year that are more important, that are demanding much more of my time, and 
things that have made it hard for me to get back into this project. Quite frankly, this is not an apology. This is 
simply how life goes sometimes, especially in open-source development. For a project as ambitious as Orchid, it's just 
becoming something larger than I can manage entirely by myself. I don't want to abandon Orchid, but it's now at a point
where I need much more support from the larger Orchid community to help push this forward and keep it the best tool for 
Java and Kotlin project documentation.

## The Future of Orchid

So Orchid is now at a point where I really need support from the community to keep it alive. I am now set up to publish
it to MavenCentral, and am actively working on migrating all of Orchid's [Copper Leaf](https://github.com/copper-leaf)
dependencies there first. Once all of those are re-published to MavenCentral, only then will I be able to publish a new 
version of Orchid there as well. This is step 1, and is something I have to do as the maintainer and publisher of those
libraries, but is something I am actively (albeit, slowly) working on. But for anyone concerned about the May 1 Bintray
deadline, that is just a deadline for publishing new artifacts to Bintray. You will still be able to download 
existing dependencies from JCenter indefinitely, so all your old builds referencing Orchid on JCenter will still work.

That said, there are a few changes that need to be made to the Orchid project, its repository, and its community, and 
this is where you can help out:

1) I do not plan on migrating old versions of Orchid to MavenCentral. But new versions will be published there. 
2) Since they will be published under a different groupId as I release them to MavenCentral (`io.github.copper-leaf`), I
    would like to take the opportunity to rename the artifacts to the standard Java artifact naming convention as well
   (`kebob-case`, such as `orchid-core` and `orchid-posts`).
3) I think now marks as good of a time as any to release Orchid as version 1.0.0, moving out of the pre-release stage, 
    and trying to  follow more strict semantic versioning.
4) I want to minimize the dependencies on 3rd-party services, to keep everything in GitHub as much as possible:
    - Shut down Gitter channel and use Github Discussions instead
    - Replace Travis CI and AppVeyor with GitHub Actions for automated builds and releases
    - Publish Orchid's documentation to GitHub Pages, instead of Netlify
    - Remove Codacy for code coverage reports, ang generate the code coverage badge manually with the docs site 
      ([#285](https://github.com/orchidhq/Orchid/issues/285))

## Orchid's proposed timeline

With all that said, here's a rough timeline for getting things back moving for Orchid. It's a long timeline because my 
life is no less hectic now than it has been for the last year, but it does give me some specific dates that I will 
really try to keep. 

Several of these tasks I need to do myself, but I will mark which ones can be done by members of the community 
with `**`:

- By May 15st, get Gitter channel closed and everyone moved over to GitHub Discussions instead  
- By May 31st, get all Copper Leaf libraries re-published to MavenCentral 
- ** By June 30st, update all of Orchid's dependencies to their latest versions, and only reference libraries that are 
    available on MavenCentral.
- ** By July 15th, update the names of all Orchid artifacts to use `kebob-case`. This includes updating all 
    documentation to match.
- ** By July 31st, get version Orchid 1.0.0 released to MavenCentral. As a minimum, it will not include any changes from 
    0.21.1 beyond updated dependencies, but I would like to get some of the outstanding PRs and minor defects fixed for
    that release as well. In addition, I would like to remove the legacy sourcedoc code before this release as well.
    - This will need some testing for the Maven and SBT plugins in particular. As I do not use either of those myself, I 
        will definitely need community members to help ensure those get published correctly.
- ** By August 31, get CI updated to use GitHub Actions instead of Travis CI and AppVeyor
- By August 31, get docs site built on GitHub Actions and publishing to GitHub Pages instead of Netlify.

In addition, there are a handful of tasks that I would like to get done at any point in this timeline, but would also 
really appreciate the help of the community in getting these tasks accomplished:

- Update all JS and CSS assets to their latest versions for all themes and plugins
- A new theme that is simple to use, has few options, and kinda just works out of the box. A theme that is as easy to 
    pick up and run with (both for site creators and for users browsing the documentation) as MkDocs Material, for
    example.
- Replace the CLI and much of the orchid "bootstrapping" with [clikt](https://ajalt.github.io/clikt/)
- Convert all non-trivial hand-written JS assets to Kotlin-JS subprojects. This is mostly concerning the widgets for 
    _plugins_ (search, Netlify CMS), not the JS needed by a theme.
- Fixing the bugs on the issue trackers for the main repo, and the starter/tutorial repos
- Making the option-parsing mechanism more type-safe: requiring specific value types, throwing errors when properties
    at unknown keys are given, etc. ideally moving away from a reflection-based approach and using something more like
    a Kotlin DSL/property delegate to pull values from a map.
- Generally improving documentation to make it simpler and easier to understand
