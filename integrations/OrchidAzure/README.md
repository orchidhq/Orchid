---
description: Connect your Orchid site to Microsoft Azure services.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300/v1558903280/plugins/azure.jpg
    alt: Azure
    caption: Azure
tags:
    - wiki
---

## About

OrchidAzure connects your [Azure DevOps Wiki](https://azure.microsoft.com/en-us/services/devops/wiki/) to Orchid, making
it simple to publish your knowledge base as a full website. 

## Demo

This plugin is currently still in progress and not all features are available with a demo yet. Please check back later.

## Usage

### Wiki Adapter

> _This feature is still in progress and is not yet available_

OrchidAzure comes with an `azure` Wiki Adapter, to connect to an existing Wiki for your Azure DevOps team and embed it 
as a wiki section. This will clone the wiki repository and convert its contents to an Orchid wiki automatically. The
order of pages in the wiki will match the order set up in the online repo, and a summary generated to match the file
structure.

```yaml
# config.yml
wiki: 
  sections:
    userManual:
      adapter: 
        type: "azure"
        repo: "[Azure DevOps wiki repository URL]"
```
