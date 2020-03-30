---
title: 'December 2018 Update: Post-Holidays Edition'
description: December 2018 Orchid News
tags: 
  - Orchid News
---

How it is already halfway through January? I apologize this update is so late, the holidays have kept me very busy and time just got away from me. But this is software development, and deadlines slip all the time, right? 😜

{% snippet 'newsPostIntro' %}

# On Github

As of the time of writing, Orchid is at 127 (or 0b1111111 in binary) stars on Github! Thanks for all the support, let's keep it going strong!

I'm also seeing more activity on Orchid's [issue tracker](https://github.com/orchidhq/orchid/issues?q=is%3Aissue+is%3Aopen+sort%3Aupdated-desc), which means people are trying it out! I do my best to answer each issue within a day or so, and this is the best way to get in contact with me if you're having issues or found a bug, as it helps everyone else out as well.

# What's New?

Orchid is now at version [0.15.3](https://github.com/orchidhq/orchid/releases/tag/0.15.0), with most changes being small bugfixes or minor usability improvements.

Most of the month, however, was spent writing new tutorials, to guide you through Orchid's best features. The most up-to-date versions of these tutorials are in [Orchid's documentation site](https://orchid.run/wiki/learn), but you can also find them right here on Dev.to!

{{ anchor('What\'s on the Menu?') }}

{{ anchor('All About Archetypes') }}

{{ anchor('The Amazing, Auto-Documenting Admin Panel') }}

# Coming Soon

As part of Orchid's never-ending quest to be the single best tool for code documentation, I'm excited to say that research into Groovy code docs is underway! This is still very much just in the exploration phase, but I'm hopeful that I will be able to get a significant chunk of it done in the next month or so. Stay tuned here to keep up-to-date with its progress!

I've also been working on a bit of a secret project for my company, a fun little serverless SlackBot that:

- runs in Node.js
- which is written in Kotlin/JS
- and is documented with Orchid
- with product pages and help forms also managed with Orchid
- all assembled with Gradle
- and uses Netlify services for all everything (although Netlify doesn't have a database so I'm using Firebase)

It's not quite finished yet, but you are welcome to preview [CrederaBot](https://crederabot.netlify.com/) whenever you like! You can even install the bot on your own Slack team, but I can't guarantee that I won't delete your data if I need to change the DB schema 😇.

---

{% snippet 'newsPostFooter' %}
