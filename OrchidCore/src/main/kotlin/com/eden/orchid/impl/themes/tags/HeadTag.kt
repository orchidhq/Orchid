package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description

// Template Section Tabs
//----------------------------------------------------------------------------------------------------------------------

@Description(value = "Render content into the HTML `<head>`.", name = "Head")
class HeadTag : TemplateTag("head", Type.Simple, true)

@Description(value = "Render content into the start and end of HTML `<body>`.", name = "Body")
class BodyTag : TemplateTag("body", Type.Content, true)

@Description(value = "Render the main page components and page content.", name = "Page")
class PageTag : TemplateTag("page", Type.Simple, true)

// Deprecated Template Section Tabs
//----------------------------------------------------------------------------------------------------------------------

@Description(value = "Render all style tags to the page.", name = "Styles")
@Deprecated("The `{% styles %}` tag is deprecated and now does nothing, as it is now included as part of the `{% head %}` tag.")
class StylesTag : TemplateTag("styles", Type.Simple, true)

@Description(value = "Render all script tags to the page.", name = "Scripts")
@Deprecated("The `{% scripts %}` tag is deprecated, and has been replaced by `{% body %}...{% endbody %}` tag.")
class ScriptsTag : TemplateTag("scripts", Type.Simple, true)
