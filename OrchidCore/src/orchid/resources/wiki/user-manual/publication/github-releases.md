---
---

## Description

The `ghPages` Publisher allows you to upload your site to GithubPages.

{% alert 'info' %}
This publisher expects Git to be installed locally on your system, as it delegates the entire deployment process 
directly to shell commands.
{% endalert %}

To use the `ghPages` publisher, you'll need to provide Orchid with a `githubToken` containing a Personal Access Token
from Netlify. Since PATs are confidential and allow anyone who has it complete access to your account, you should set 
this as an environment variable and add it to your Gradle orchid config from that variable rather than committing it to
source control.

{% highlight 'groovy' %}
orchid {
    ...
    args = ["githubToken ${System.getenv('GITHUB_TOKEN')}"]
}
{% endhighlight %}

After your PAT is set up, you'll need to set up your repo on GitHub with a `gh-pages` branch. Orchid will initialize a 
new local git repo and push it to this branch, overwriting anything currently in that branch. 

The `username` property is the user which issued the PAT, and it is also the organization hosting the repository on 
GitHub. You can authenticate with a different user than is hosting the repository by setting the `repo` as 
`username/repo`.

## Example Usage

{% highlight 'yaml' %}
services:
  publications: 
    stages: 
      - type: ghPages
        username: 'cjbrooks12'
        repo: 'JavaEden/Orchid'
      - type: ghPages
        username: 'cjbrooks12'
        repo: 'cjbrooks12.github.io' # becomes cjbrooks12/cjbrooks12.github.io 
{% endhighlight %}

## API Documentation

{% docs className='com.eden.orchid.impl.publication.GithubPagesPublisher' tableClass='table' tableLeaderClass='hidden' %}
