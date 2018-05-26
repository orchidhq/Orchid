---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}
Orchid comes with a simple, yet flexible and powerful mechanism for deploying your site once it has been built. Rather
than struggling to set up shell scripts to deploy your site, let Orchid do the hard for you. Your site is simple and 
beautiful, letting others see it should be just the same.
{% endblock %}

{% block sectionBody %}
The typical approach to deployment with static sites is to have a CI server, like Travis CI or Netlify, build your site 
and then do something with the result of the build. This works well enough if you only run Orchid from one place, but it
gets tricky if you end up changing CI providers or need to do a quick deploy from your local machine when the CI server
is down. 

Every CI tool has their own custom method of deploying your site to AWS, Github Pages, etc. so you start to get locked 
into that one provider. By moving the deployment process inside the lifecycle of an Orchid build process, you can 
simplify the process and also prevent yourself from getting locked into any one CI tool. 

In addition, Orchid knows much more about your site than the CI server does, and it can use that knowledge to reduce 
the amound of configuration needed. 

## Publication Pipeline

For production builds with Orchid that include a deployment step, run the `orchidDeploy` gradle task instead of 
`orchidBuild`. This will run the normal build as you'd expect, but follow that build with a deploy step after the build 
completes successfully. You can also run a deploy manually by issuing the `{{anchor('deploy', 'DeployCommand')}}` 
command from an interactive session or through the admin panel.

### Setup

Setting up a deployment pipeline is configured in your `config.yml` under the `services.publications.stages` key. It 
follows the same modular configuration you've come to expect and love that is used for components, menus, and forms.

{% highlight 'yaml' %}
services:
  publications:
    stages:
      - type: script
        command: './deployCustom.sh'
        cwd: '#{$0|resourcesDir}/scripts'
      - type: ghPages
        username: 'JavaEden'
        project: 'Orchid'
        branch: 'gh-pages'
        versioned: true
{% endhighlight %}

Running the deployment pipeline is a 2-step process: validation, and execution. 

{% alert 'info' :: compileAs('md') %}
There are currently no publication stage implementations. The above is just an example of the pipeline configuration, 
and a couple examples of the expected, complete configurations for publishers. However, the pipelines API is complete, 
and you can safely build and share your own publisher implementations. 
{% endalert %}

### Validation

Every step in the pipeline is first validated, to make sure that it is set up correctly. While this is not foolproof,
as it is left up to the developer to validate the build phase properly and fully, it can greatly help in reducing the 
chance of a botched deployment. 

During the validation phase, every publisher gets a chance to be validated. If any stage fails validation, the entire 
pipeline will be aborted, and no stages will be executed. This allows you to perform sanity checks on the configuration
to make sure all required fields are accounted for, and even to do other checks such as verify network connectivity. 

### Execution

After all deployment stages have been validated, they will then be executed in the same order. If any pipeline stage 
fails, the entire deployment is aborted. Stages can be given custom ordering, and you can make expectations that a 
previous stage will be executed have have its outputs ready for later stages. This allows the pipeline to be used as a
full post-processing pipeline rather than just a deployment mechanism. One example is to optimize all assets, or to 
compress and uglify the resulting assets, which would be difficult to apply across all pages with the existing indexing
and generating APIs.  

### Dry Publishing

You can have Orchid run a dry deploy, and it will validate all publication stages for you without executing them. This
allows you to quickly and safely ensure that you have your pipeline configured correctly at all stages before attempting
a full deploy. 

When `deploy` is run as an Orchid task (that is, with the `orchidDeploy` gradle task, or 
`orchidRun -PorchidRunTask=deploy`) the pipeline is not dry and is run as a full deploy. 

When `deploy` is run as a command, either from an xinteractive command-line session or submitted through the admin panel, 
it is dry by default. You can issue a command for a full deploy with `deploy -- -dry false` or (`deploy false` for 
short). 

In addition, you can make any single deployment stage run dry, even when the rest aren't, with an option on the stage's
config. Note that this does _not_ override the flag on the entire process, so setting `dry` to false on a dry run will
not force that stage to execute anyway. 

{% highlight 'yaml' %}
services:
  publications:
    stages:
      - type: ghPages
        username: 'JavaEden'
        project: 'Orchid'
        branch: 'gh-pages'
        versioned: true
        dry: true
{% endhighlight %}

## Available Publishers

No publishers are currently available, but publishing to S3, Github Pages, and Netlify will eventually be officially 
supported via plugins. They will be listed in this wiki as they become available.

{% endblock %}
