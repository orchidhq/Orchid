---
title: 'November 2018 Update: The Biggest Update Yet!'
description: November 2018 Orchid News
tags: 
  - Orchid News
---

Orchid has had a great month, y'all! 

As Orchid is getting more popular, there is a growing need to keep the community up-to-date on all the happenings around Orchid. This post is the first in a new monthly series where I will share Orchid's progress during the previous month! Follow along with this series to stay on top of Orchid's newest features, track adoption on Github, and see who's using Orchid!

{% snippet 'newsPostIntro' %}

# On Github

Orchid just hit 100 stars at the end of October, and it's up to 108 as of the time of writing. Thanks for all the support, I'm amazed that this little side-project of mine has been so popular! Keep the sharing going!

The Orchid repo also received contributions from the following people, thanks so much for the help!

- [Andrey Mochalov](https://github.com/epidemia)
- [Yaroslav Matveychuk](https://github.com/yaroslavm)
- [Bastien Jansen](https://github.com/bjansen)

# New Features

Orchid [0.13.0](https://github.com/orchidhq/orchid/releases/tag/0.13.0) and [0.14.0](https://github.com/orchidhq/orchid/releases/tag/0.14.0) were released around Thanksgiving, and together mark the biggest Orchid release to-date! This includes:

- Update to Kotlin 1.3
- Wikis can now generate PDFs for offline documentation. See documentation [here](https://orchid.run/plugins/orchid-wiki-feature#offline-documentation).
- A new Asset Manager improves build times, and intelligently renders your image assets. It even includes basic image manipulation for automated thumbnail generation! Learn more about media management [here](https://orchid.run/wiki/user-manual/content-management/media).

# Who's Using Orchid?

Orchid now powers the documentation for two really incredible open-source projects, go check them out!

- Pebble is the main template engine used by Orchid, and [pebbletemplates.io](https://pebbletemplates.io/) was officially launched on November 11. Some of the most notable Orchid features it uses are:
  - [Editorial](https://orchid.run/themes/orchid-editorial-theme) theme
  - [Changelogs](https://orchid.run/plugins/orchid-changelog-feature) and [automated Github releases](https://orchid.run/wiki/user-manual/publication/github-releases)
  - [Wiki](https://orchid.run/plugins/orchid-wiki-feature) with offline documentation
  - [Javadocs](https://orchid.run/plugins/orchid-javadoc-feature) Java source code documentation
  - Deployment to [Github Pages](https://orchid.run/wiki/user-manual/publication/github-pages)
- Strikt is a new assertion library for Kotlin, and [strikt.io](https://strikt.io/) went live October 19th using the following Orchid features:
  - A fully-custom theme, including custom template tags
  - [Changelogs](https://orchid.run/plugins/orchid-changelog-feature)
  - [Wiki](https://orchid.run/plugins/orchid-wiki-feature)
  - [Kotlindocs](https://orchid.run/plugins/orchid-kotlindoc-feature) Kotlin source code documentation
  - Deployment to [Github Pages](https://orchid.run/wiki/user-manual/publication/github-pages)

In addition, my personal website at [caseyjbrooks.com](https://www.caseyjbrooks.com/) got a major facelift. It previously used the [FutureImperfect](https://orchid.run/themes/orchid-future-imperfect-theme) theme, but I have been working on creating a custom theme based on Bulma, with some very helpful advice from @aspittel's [Profile Advice Thread](https://dev.to/aspittel/portfolio-advice-thread-56g9). When the theme is finished, it will be merged into the main Orchid repo, so you can use it for your Orchid sites too!

---

{% snippet 'newsPostFooter' %}
