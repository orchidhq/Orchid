---
title: 'March 2019 Update: March Madness'
description: March 2019 Orchid News
tags: 
  - Orchid News
---

I love watching basketball, which makes March one of the best months of the year as the NCAA National Championship, March Madness, takes place. My top pick, Michigan State, has already defeated Duke and is well on their way to becoming the National Champions!

It has also been quite a "mad" month for Orchid too, with lots of big changes in progress. From being able to publish your site to Gitlab and Bitbucket, to integration with Github Wikis, and a re-theming of the documentation site all underway, it's safe to say that I've been quite busy!

{% snippet 'newsPostIntro' %}

# On Github

As of the time of writing, Orchid is at 175 stars on Github. And this month, in particular, I have been absolutely blown away at the growth on Github. With 4 pull requests by 3 contributors, numerous issues opened and questions asked across Github, Twitter, and Gitter, and more than FOUR TIMES as many downloads on Bintray this month as last month, Orchid is seeing unprecedented growth that I could never have imagined. 

Thank you all so much, I certainly could not keep working this hard without your incredible support! But these successes are entirely because of you, and I love to give credit where it is due, so let's drill into this a bit deeper.

## Contributions

- [@alejandrohdezma](https://github.com/alejandrohdezma) helped out to fix a bug using the wrong FontAwesome icon ([#239](https://github.com/orchidhq/orchid/pull/239))
- [@ Sumo99](https://github.com/Sumo99) got rid of the last of Lombok, paving the way for a pure-Kotlin future! They also helped remove Google Plus social links now that the service is officially dea. ([#243](https://github.com/orchidhq/orchid/pull/243) and [#249](https://github.com/orchidhq/orchid/pull/249))
- [@ dkowis](https://github.com/dkowis) fixed a broken documentation link ([#252](https://github.com/orchidhq/orchid/pull/252))

## 4x Downloads Increase

The 31-day period ending February 28th saw a bit shy of 700 downloads.

![February downloads](https://thepracticaldev.s3.amazonaws.com/i/24lbbw6euqnbd2mgkpvn.png)

The stats show that downloads really started to take off around the end of the month, which is when I published the tutorial on how to use Orchid to document a Kotlin project. I would highly recommend you check it out if you haven't already.

{{ anchor('How to Document a Kotlin Project') }}

Meanwhile, The 31-day period ending March 31st saw more than 2600 downloads! And questions from y'all on how to use it have increased to match, and I'm so excited to be able to help solve your problems with Orchid!

![March downloads](https://thepracticaldev.s3.amazonaws.com/i/pk0bsb0byg2d857xetng.png)

All of these statistics are freely available on Bintray, [go here](https://bintray.com/beta/#/orchidhq/orchid/OrchidCore?tab=statistics) to check it out for yourself.

# What's New?

Orchid is currently at version [0.16.7](https://github.com/orchidhq/orchid/releases/tag/0.16.7). There have been no major changes since last month, mostly just a series of bugfixes on the Copper theme and minor usability improvements.

# Coming Soon

## New Docs

Orchid's docs are getting a reboot! When I started work on Orchid, Bootstrap was the only CSS framework I knew of. I had no idea there were so many great options out there, and ultimately I have come to really enjoy Bulma for its simplicity and flexibility. 

And so, I've been building the "Copper" theme, based on Bulma, to serve as the home for all of Orchid's own documentation, and also of its supplemental libraries. Here's a preview:

[![New Docs](https://thepracticaldev.s3.amazonaws.com/i/svynj5ko32iwvpgfuuo4.png)](https://orchid.run/)

Also coming with the new theme will be a major overhaul in the _content_ on the docs site, as I continue to iterate upon the docs and figure out the best way to present the information to you. 

## More Integrations

From the very beginning, Orchid was created to be infinitely flexible, able to work with a wide variety of different systems and content structures, but until now it has been fairly closely tied to just Netlify and GitHub. I myself regularly use Microsft Azure and BitBucket at work and want to use Orchid in those places, and I can imagine y'all do as well.

So work is currently under way to make it easier to integrate Orchid into those different Git platforms, so you do not need to change your current processes to fully utilize Orchid's power! Soon, you'll be able to use the native Wikis on Github, Bitbucket, Gitlab, and Azure DevOps as a headless CMS, and you'll also be able to deploy directly to their static hosting platforms and create releases!

# Get Involved

You don't have to be an expert in Java, Kotlin, Orchid, or anything else to help out the Orchid project. There are a number of ways you can contribute right now:

- fixing typos and improving the clarity of documentation articles
- converting Java classes to Kotlin

However, I am currently looking for more skilled help with a couple specific areas:

- I'm looking for people who currently use the features of the git platforms I'm imtegrating with, to help build their integrations:
    - GitLab
        - [Wiki adapter](https://github.com/orchidhq/orchid/blob/features/integrations/integrations/OrchidGitlab/src/main/kotlin/com/eden/orchid/gitlab/wiki/GitlabWikiAdapter.kt)
        - [GitLab Pages Publisher](https://github.com/orchidhq/orchid/blob/features/integrations/integrations/OrchidGitlab/src/main/kotlin/com/eden/orchid/gitlab/publication/GitlabPagesPublisher.kt)
    - Bitbucket
        - [Wiki adapter](https://github.com/orchidhq/orchid/blob/features/integrations/integrations/OrchidBitbucket/src/main/kotlin/com/eden/orchid/bitbucket/wiki/BitbucketWikiAdapter.kt)
        - [Bitbucket Cloud Publisher](https://github.com/orchidhq/orchid/blob/features/integrations/integrations/OrchidBitbucket/src/main/kotlin/com/eden/orchid/bitbucket/publication/BitbucketCloudPublisher.kt)
    - Azure DevOps
        - [Wiki adapter](https://github.com/orchidhq/orchid/blob/features/integrations/integrations/OrchidAzure/src/main/kotlin/com/eden/orchid/azure/wiki/AzureWikiAdapter.kt)

I would also love the help of a designer to make a really great home page and a new logo for Orchid.

Please [reach out to me](https://www.caseyjbrooks.com/contact/) if you're interested in contributing to the project for any of these specific issues, I (and the whole community!) would really appreciate it!

---

{% snippet 'newsPostFooter' %}
