---
title: Comment Form Demo
template: page
skipTaxonomy: true
components:
  - type: pageContent
  - type: form
    form: comment
menu:
  - type: 'page'
    itemId: 'Orchid Forms'
  - type: 'pageChildren'
    itemId: 'Orchid Forms'
    asSubmenu: true
    submenuTitle: Docs
---

This page demonstrates an example of a form that was created by the end-user, and that has its own submission page. 
Forms that are created from a data file like YAML do not have their own submission page, instead relying on the server
to redirect them. But forms that were set up in the Front Matter of a content file like Markdown generate that page as
the intended target for redirection after submission of the form. 