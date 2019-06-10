
# Orchid

[![All Platforms Build Status](https://casey-brooks.visualstudio.com/Orchid/_apis/build/status/Orchid-Gradle-CI?branchName=dev "All Platforms Build Status")](https://casey-brooks.visualstudio.com/Orchid/_build/latest?definitionId=2?branchName=dev)
[![Linux and Mac Build Status](https://travis-ci.org/JavaEden/Orchid.svg?branch=dev "Linux and Mac Build Status")](https://travis-ci.org/JavaEden/Orchid)
[![Windows Build status](https://ci.appveyor.com/api/projects/status/0358qdkmfhbqedo1/branch/dev?svg=true "Windows Build status")](https://ci.appveyor.com/project/cjbrooks12/orchid/branch/dev)
[![Current Version](https://api.bintray.com/packages/javaeden/Orchid/OrchidCore/images/download.svg "Current Version") ](https://bintray.com/javaeden/Orchid/OrchidCore/_latestVersion)
[![License: LGPL-3.0](https://img.shields.io/badge/License-LGPL%20v3-blue.svg "Licensed under LGPL-3.0")](http://www.gnu.org/licenses/lgpl-3.0)
[![Codacy Grade](https://api.codacy.com/project/badge/Grade/8bca7e84b6094c03ae1316278cf63ae1 "Codacy Grade")](https://www.codacy.com/app/cjbrooks12/Orchid?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=JavaEden/Orchid&amp;utm_campaign=Badge_Grade)
[![Codacy Coverage](https://api.codacy.com/project/badge/Coverage/8bca7e84b6094c03ae1316278cf63ae1 "Codacy Coverage")](https://www.codacy.com/app/cjbrooks12/Orchid?utm_source=github.com&utm_medium=referral&utm_content=JavaEden/Orchid&utm_campaign=Badge_Coverage)
[![Gitter chat](https://img.shields.io/gitter/room/nwjs/nw.js.svg "Gitter Chat")](https://gitter.im/JavaEden/Orchid)
[![Backers on Open Collective](https://opencollective.com/orchidssg/backers/badge.svg)](#backers) 
[![Sponsors on Open Collective](https://opencollective.com/orchidssg/sponsors/badge.svg)](#sponsors) 

> A beautiful and truly unique documentation engine and static site generator.

![Example Orchid site](https://orchid.netlify.com/assets/media/sample.jpg)

Orchid is a brand-new, general-purpose static site generator for Java and Kotlin, with a focus on extensibility and 
aimed at developers looking to improve their technical documentation. Orchid was born out of a desire for better-looking 
Javadocs and frustration with how difficult is it to manage large Jekyll sites and keep it up-to-date with your code.

Orchid supports a variety of plugins, including a wiki, static pages, blogs, and much more. It aims to have high 
compatibility with many of the existing static site generators, such as Jekyll, Gitbook, and Hugo, so that migration to 
Orchid is painless. And if you can't find a plugin to do what you need, Orchid provides an intuitive way to add your own 
private plugins and a rich API so you can make your site as beautiful and unique as an Orchid.

### Features

- Plugin-driven architecture
- Multi-theme support
- Admin interface to manage your content and gain deep insights into your build
- Link to other Orchid sites
- Powerful and flexible indexing, unique to Orchid
- Full-text static search
- Component-based content management
- Integrated deployment pipeline
- Fully replaces Jekyll, Hugo, Gitbook, Javadocs, and more!

[View the full documentation](https://orchid.netlify.com) or see the quick-start below.

## Table of Contents

1. [Getting Started](#getting-started)
1. [User Manual](#user-manual)
1. [Tutorials](#tutorials)
1. [Example Orchid Sites](#example-orchid-sites)
1. [License](#license)
1. [Contributing](#contributing)
1. [Contact](#contact)

## Getting Started

Orchid integrates with any new or existing Gradle/Maven project. The simplest way to get started is to deploy the 
[starter repo](https://github.com/JavaEden/OrchidStarter) directly to Netlify. Just click the button below to 
automatically clone this repo and deploy it to Netlify. The starter repo includes the 
[Netlify CMS](https://www.netlifycms.org/), so you will be up and publishing content as soon as possible. You will need 
to set the Github user/repo in `src/orchid/resources/config.yml`, but the rest of the CMS config is automatically 
generated based on your current Orchid plugins and configurations. 

[![Deploy to Netlify](https://www.netlify.com/img/deploy/button.svg)](https://app.netlify.com/start/deploy?repository=https://github.com/JavaEden/OrchidStarter)

To run Orchid locally, the only system dependency necessary is a valid Java 8 JDK. Orchid can be integrated with any new
 or existing Gradle or Maven project or bootstrapped manually in any JVM-based scriptlet (such as 
[kscript](https://github.com/holgerbrandl/kscript)). To get started, pick a Bundle (OrchidAll or OrchidBlog) or manually 
choose your desired Orchid plugins. You may pick a bundle to start with and add any number of plugins afterward, both 
official and unofficial.

Orchid can also be integrated into existing projects. The following build tools are currently supported:

- [Gradle](https://orchid.netlify.com/wiki/user-manual/getting-started/quickstart#gradle)
- [Maven](https://orchid.netlify.com/wiki/user-manual/getting-started/quickstart#maven)
- [KScript](https://orchid.netlify.com/wiki/user-manual/getting-started/quickstart#kscript)

## User Manual

Orchid's User Manual will walk you through the main features of Orchid and give you a deeper understanding of each topic
and feature.

[User Manual](https://orchid.netlify.com/wiki/user-manual)

## Tutorials

There are several tutorials designed to walk you through building an Orchid site from scratch. The source for all 
tutorials can also be found in the [OrchidTutorials repository](https://github.com/JavaEden/OrchidTutorials).

[Tutorials](https://orchid.netlify.com/wiki/learn)

## Example Orchid Sites

* [Official Orchid documentation](https://orchid.netlify.com)
* [Clog documentation](https://javaeden.github.io/Clog/)
* [Krow documentation](https://javaeden.github.io/Krow/)
* [caseyjbrooks.com](https://www.caseyjbrooks.com/)
* [Strikt.io](https://strikt.io/)
* [PebbleTemplates.io](https://pebbletemplates.io/)

## Contributors

This project exists thanks to all the people who contribute. 

<a href="https://github.com/JavaEden/Orchid/graphs/contributors"><img src="https://opencollective.com/orchidssg/contributors.svg?width=890&button=false" /></a>

## Backers

Thank you to all our backers! 🙏 [[Become a backer](https://opencollective.com/orchidssg#backer)]

<a href="https://opencollective.com/orchidssg#backers" target="_blank"><img src="https://opencollective.com/orchidssg/backers.svg?width=890"></a>

## Sponsors

Support this project by becoming a sponsor. Your logo will show up here with a link to your website. [[Become a sponsor](https://opencollective.com/orchidssg#sponsor)]

<a href="https://opencollective.com/orchidssg/sponsor/0/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/0/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/1/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/1/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/2/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/2/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/3/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/3/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/4/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/4/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/5/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/5/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/6/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/6/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/7/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/7/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/8/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/8/avatar.svg"></a>
<a href="https://opencollective.com/orchidssg/sponsor/9/website" target="_blank"><img src="https://opencollective.com/orchidssg/sponsor/9/avatar.svg"></a>

## License

Orchid is open-source software licensed under the GNU Lesser General Public License (LGPL). Please see
[License.md](https://github.com/JavaEden/Orchid/blob/dev/License.md)

## Contributing

Please see [CONTRIBUTING.md](https://github.com/JavaEden/Orchid/blob/dev/.github/CONTRIBUTING.md)

## Contact

Orchid is being actively developed by Casey Brooks. Please open an issue here with questions or bug/feature requests, or
you can reach me directly at cjbrooks12@gmail.com.
