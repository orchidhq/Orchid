---
from: docs.plugin_index
description: Embed the Swagger UI within any Orchid page.
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973700/plugins/swagger.jpg
    alt: Swagger
    caption: Screenshot of Swagger UI
components:
  - type: pageContent
  - type: swaggerUi
    openApiSource: https://cdn.jsdelivr.net/gh/OAI/OpenAPI-Specification@4d5a749c/examples/v2.0/json/petstore.json
tags:
    - components
---

## About

## Demo

## Usage

The OrchidSwagger plugin allows you to add the `swagger` component to any page. This will include the Swagger UI 
Javascript and CSS necessary to use the Swagger UI, you just need to supply the component with the URL of your OpenAPI
JSON definition.