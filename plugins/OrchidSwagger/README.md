---
description: Embed the Swagger UI within any Orchid page.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973700/plugins/swagger.jpg
    alt: Swagger
    caption: Screenshot of Swagger UI
tags:
    - components
---

## About

The OrchidSwagger makes it convenient to embed the [Swagger UI](https://swagger.io/tools/swagger-ui/) in your 
documentation site.

## Demo

- Run [SwaggerTest](https://github.com/orchidhq/orchid/blob/dev/plugins/OrchidSwagger/src/test/kotlin/com/eden/orchid/swagger/SwaggerTest.kt) for demo

## Usage

The OrchidSwagger plugin allows you to add the `swagger` component to any page. This will include the Swagger UI 
Javascript and CSS necessary to use the Swagger UI, you just need to supply the component with the URL of your OpenAPI
JSON definition. Note that, since the Swagger UI fetches the `openApiSource` in the browser, the URL given must not be
blocked by [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS).

```markdown
// pages/api.md
---
components:
  - type: 'swaggerUi'
    openApiSource: 'https://petstore.swagger.io/v2/swagger.json'
---
```
