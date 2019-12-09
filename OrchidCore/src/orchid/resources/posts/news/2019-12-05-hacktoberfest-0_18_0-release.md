---
title: 'Fall 2019 Update: Hacktoberfest, 0.18.0 Release'
description: Fall 2019 Orchid News
tags: 
  - Orchid News
---

It's. Finally. Here. I've been working on and teasing a new major version of Orchid for several months, and it's finally
available! Oh, and Hacktoberfest was really great for Orchid too, as it always is.

This is the official Orchid newsletter, the newest and best documentation site generator. There is a growing need to 
keep the community up-to-date on all the happenings around Orchid, and here I will share Orchid's progress, milestones, 
and future plans! Follow along with this series to stay on top of Orchid's newest features, track adoption on Github, 
and see who's using Orchid!

## On Github

As of the time of writing, Orchid is at 278 stars on Github, thank you so much for all the support! 

Since the last update, Orchid has had several contributions, especially during Hacktoberfest, so a special thanks goes 
out to the following individuals for their help improving this project:

- [spind42](https://github.com/spind42) - Fixed an issue running Orchid from Maven
- [Jean-Michel Fayard](https://github.com/jmfayard) - Added a readme badge for CodeTriage
- [Steve S](https://github.com/singularsyntax) - Added a new Spotify tag to the {{ anchor('OrchidWritersBlocks') }} plugin
- [Steve Waldman](https://github.com/swaldman) - Wrote a plugin to run Orchid from SBT!

## What's New?

The long-awaited version [0.18.0](https://github.com/orchidhq/orchid/releases/tag/0.18.0) has finally arrived! 

This release includes a major cleanup to Orchid's internal APIs, which will help make better plugins moving forward, but 
most importantly introduces the new, completely rewritten code documentation system based on 
[Kodiak](https://github.com/copper-leaf/kodiak). The new system improves consistency of generated docs among all 
languages and enables multi-module documentation. It also paves the way to make it significantly easier to develop 
integrations for other languages, so let me know which languages you would like to see supported by Orchid!

Be sure to check out the {{ anchor('Changelog') }} for the full list of changes, and the 
{{ anchor('Migration Guide', '0.18.0 Migration Guide') }} for help updating to this new version.

In addition, some of you may have noticed a few changes around the Orchid ecosystem! The Orchid repo has been moved to
@orchidhq on Github, and we have a completely redesigned documentation site and new logos. Together with the 0.18.0 
release, this marks the biggest change in the development and ecosystem of Orchid to help cement it is as the 
single-best tool for producing documentation websites.

You can help spread the word about this major milestone by sharing [https://orchid.run](https://orchid.run) and tagging
@OrchidSSG on Twitter!

## Who's Using Orchid?

I periodically search GitHub to find new projects getting set up with Orchid. Here are a couple great examples of 
projects being documented with Orchid:

- [The Alchemist Simulator](http://alchemistsimulator.github.io/)
- [Acorn](https://nhaarman.github.io/acorn/)
- [Camunda](https://camunda.github.io/camunda-rest-client-spring-boot/)

For more examples of sites using Orchid, head on over to our new {{ anchor('Showcase') }}, and feel free to reach out
or submit a PR to add your site to the showcase as well!

---

Are you interested in getting started with Orchid? There simply is no better way to manage all the documentation for 
your project, and I'd love to help you get set up! 

If you have an open-source project that needs docs, are building a new portfolio, or are building any other kind of 
static site, I want to work with you to get you set up with Orchid! Comment on this post, send me a PM here on Dev.to, 
reach out on [Gitter](https://gitter.im/JavaEden/Orchid), or [contact me here](https://www.caseyjbrooks.com/contact/) 
and I will be with you every step of the way.

And as always, let me know if you start using Orchid so I can feature you in the next update!
