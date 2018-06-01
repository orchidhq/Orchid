---
title: Anonymous Form Demo
templates: page
skipTaxonomy: true
components:
  - type: pageContent
  - type: form
    form: 
      title: 'Tell me a joke'
      attributes:
        data-netlify: true
      fields:
        question:
          label: 'Question'
          type: 'text'
          span: 'full'
          required: true
          order: 1
        answer:
          label: 'Answer'
          type: 'textarea'
          span: 'full'
          required: true
          order: 1
---

This page demonstrates an example of a form that was defined directly in the Form Component's configuration in this 
page's Front Matter. There is no difference between creating a form anonymously, like this one, and creating one like 
normal as a form file, except that it is easier to share a form file throughout your site. However, anonymous forms are
great for forms that are more complex and only are used on a single page.