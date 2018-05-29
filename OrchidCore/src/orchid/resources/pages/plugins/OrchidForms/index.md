---
official: true
title: Orchid Forms
description: Engage your users with embeddable, fully customizable forms.
images:
  - src: http://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974798/plugins/forms.jpg
    alt: Forms
    caption: Photo by Gemma Evans on Unsplash
---

### About Orchid Forms

Orchid Forms allows you to create form definitions and embed them anywhere as an Orchid Component. While handling form 
submissions is typically something you need your own server for, HTML forms can actually be quite useful on static sites
when combined with services like [Netlify Form Handling](https://www.netlify.com/docs/form-handling/), 
[Staticman](https://staticman.net/) or [AWS Lambda Functions](https://aws.amazon.com/lambda/). This plugin allows you to 
separate the concerns for the display and fields of your forms from the handling of the form data, so you can focus on 
what matters most: engaging your audience.

### Creating Forms

There are multiple ways in which you can set up forms. All pages in the `forms/` directory will create a form with a 
`key` matching the filename of the form definition file. You can then use this `key` to reference that form definition
in the `form` Component:

{% highlight 'yaml' %}
---
components:
  - type: form
    form: contact # uses definition from forms/contact.yml
---
{% endhighlight %}

The files used for these form definitions can either be a data-type file (such as YML or TOML), or a content-type file 
(such as Markdown or Pebble), with the form definition in the file's Front Matter. In the case that the form is a data-
type file, the form is just the definition, and the action is set to whatever is in the form definition.
 
But content-type form definitions will generate a page intended as the redirection target after submission. If no 
`action` is set in the form definition, then the URL of the generated page will be set as the `action` of the form. It
will also add a hidden field `__onSubmit` with a `value` of this page, so form handlers can then redirect to this URL.

Alternatively, you may add a form definition directly in the `form` component, for situations where you only need the 
form to be used once. There is no difference between a form indexed by a page, or created inline with the component, 
except that anonymous forms cannot be referenced by other components.

### Form Field Definition

Orchid Forms comes with a basic `contact` form already set up for you, which includes `name`, `email`, `subject`, and 
`message` fields. All you have to do is set the `form` property on the `form` component to `contact`, and you're done.

But in most cases, you will want to customize your forms. The format used to configure forms is inspired by 
[October CMS](https://octobercms.com/docs/backend/forms#form-fields), albeit much simpler and less powerful. Take the 
default `config` form as an example:

{% highlight 'yaml' %}
{{ load('forms/contact.yml', false) }}
{% endhighlight %}

The `fields` property contains a map of field definitions, where each key is the `name` attribute of an `input` in the 
form. Each field must have a `type`, which typically matches one of the HTML5 input `types`, but field types are 
extensible and new types may be added by plugins as needed. You can set the `span` of each field to a number to span
that many columns in a 12-column grid, or you may use `full` to span 12 columns, `left` to span 6, floated left, `right`
to spand 6, floated right, or `auto`, which is the same as `left`. 

You can set arbitrary attributes to the `form` element by setting `attributes` to a map of key, value pairs. These 
values will be added to the form, and can be used for things like marking the form as a Netlify-enabled form (which is
done by default in the standard `contact` form). 