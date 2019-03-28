---
description: 'Create a Release on Github from your Changelog.'
---

## Description

The `githubReleases` Publisher allows you to create a git tag and a Release on Github with notes from your latest 
changelog entry.

To use the `githubReleases` publisher, you'll need to provide Orchid with a `githubToken` containing a Personal Access 
Token from Github. Since PATs are confidential and allow anyone who has it complete access to your account, you should 
set this as an environment variable and add it to your Gradle orchid config from that variable rather than committing it 
to source control.

{% highlight 'groovy' %}
orchid {
    ...
    githubToken = "${System.getenv('GITHUB_TOKEN')}" 
    // or 'githubToken' in an environment variable without this line
}
{% endhighlight %}

The release will be created on the repo specified in the publisher config, specified at `repo` as `username/repo`. The 
account the PAT was created for must have write acess to this repo. 

## Example Usage

{% highlight 'yaml' %}
services:
  publications:
    stages:
      githubReleases:
        repo: 'JavaEden/Orchid'
{% endhighlight %}

## API Documentation

{% docs className='com.eden.orchid.github.publication.GithubReleasesPublisher' tableClass='table' tableLeaderClass='hidden' %}
