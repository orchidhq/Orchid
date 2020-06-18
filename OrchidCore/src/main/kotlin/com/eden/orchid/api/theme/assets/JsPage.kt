package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.ExternalResource
import com.eden.orchid.api.resources.resource.InlineResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils

private const val ASSET_DESCRIPTION = "The resource to load as an extra script"
private const val ATTRS_DESCRIPTION = "Arbitrary attributes to apply to this element when rendered to page"
private const val ASYNC_DESCRIPTION = "Adds the `async` attribute to the script tag"
private const val DEFER_DESCRIPTION = "Adds the `defer` attribute to the script tag"
private const val MODULE_DESCRIPTION = "Adds the `module` attribute to the script tag"
private const val NOMODULE_DESCRIPTION = "Adds the `nomodule` attribute to the script tag"
private const val INLINED_DESCRIPTION = "Inlines the contents of this script directly into the page instead of being referenced from a URL."
private const val DOWNLOAD_DESCRIPTION = "If the resource is external, download it and serve it from the built site so the site doesn't depend on other servers being available."

interface JsPageAttributes {
    var attrs: MutableMap<String, String>
    var async: Boolean
    var defer: Boolean
    var module: Boolean
    var nomodule: Boolean
    var inlined: Boolean
    var download: Boolean
}

@Description(value = "A Javascript static asset.", name = "JS Asset")
class JsPage(
    origin: AssetManagerDelegate,
    resource: OrchidResource,
    key: String,
    title: String
) : AssetPage(origin, resource, key, title), JsPageAttributes {

    @Option
    @Description(ATTRS_DESCRIPTION)
    override lateinit var attrs: MutableMap<String, String>

    @Option
    @Description(ASYNC_DESCRIPTION)
    override var async: Boolean = false

    @Option
    @Description(DEFER_DESCRIPTION)
    override var defer: Boolean = false

    @Option
    @Description(MODULE_DESCRIPTION)
    override var module: Boolean = false

    @Option
    @Description(NOMODULE_DESCRIPTION)
    override var nomodule: Boolean = false

    @Option
    @Description(INLINED_DESCRIPTION)
    override var inlined: Boolean = false

    @Option
    @BooleanDefault(true)
    @Description(DOWNLOAD_DESCRIPTION)
    override var download: Boolean = true

    fun applyAttributes(config: JsPageAttributes) {
        this.attrs = config.attrs
        this.async = config.async
        this.defer = config.defer
        this.module = config.module
        this.nomodule = config.nomodule
        this.inlined = config.inlined
        this.download = config.download
    }

    override fun configureReferences() {
        reference = OrchidReference(resource.reference)// copy reference so we can update it
        reference.isUsePrettyUrl = false

        if (resource is ExternalResource) {
            // mark the user's request to download the resource offline
            resource.download = download

            if (resource.shouldDownload) {
                // if the resource should actually should be downloaded offline, then update our reference to point to
                // the offline file, and apply the prefix as needed
                reference.baseUrl = context.baseUrl
                if (origin.prefix != null) {
                    reference.path = OrchidUtils.normalizePath(origin.prefix) + "/" + reference.path
                }
            } else {
                // keep it referencing the external location, don't change anything
            }
        } else {
            // it's just a local file, apply the prefix as needed
            if (origin.prefix != null) {
                reference.path = OrchidUtils.normalizePath(origin.prefix) + "/" + reference.path
            }
        }
    }

    override val shouldInline: Boolean get() {
        return when {
            resource is InlineResource -> true
            resource is ExternalResource && resource.shouldDownload && inlined -> true
            resource !is ExternalResource && inlined -> true
            else -> false
        }
    }

    override fun renderAssetToPage(): String? {
        return if (shouldInline) {
            (applyStartTag()
                    + applyModuleNoModule()
                    + renderAttrs(attrs)
                    + applyInlineScript()
                    + applyCloseTag())
        } else {
            (applyStartTag()
                    + applyAsyncDefer()
                    + applyModuleNoModule()
                    + renderAttrs(attrs)
                    + applyScriptSource()
                    + applyCloseTag())
        }
    }

    private fun applyAsyncDefer(): String? {
        var tagString = ""
        if (async) {
            tagString += " async"
        }
        if (defer) {
            tagString += " defer"
        }
        return tagString
    }

    private fun applyModuleNoModule(): String? {
        var tagString = ""
        // if tagged as module, cannot be a `nomodule`
        if (module) {
            tagString += " type=\"module\""
        } else if (nomodule) {
            tagString += " nomodule"
        }
        return tagString
    }

    private fun applyScriptSource(): String? {
        return " src=\"${this.link}\">"
    }

    private fun applyInlineScript(): String? {
        return ">\n${resource.compileContent(context, this)}"
    }

    private fun applyStartTag(): String? {
        return "<script"
    }

    private fun applyCloseTag(): String? {
        return "</script>"
    }
}

class ExtraJs : OptionsHolder, JsPageAttributes {

    @Option
    @Description(ASSET_DESCRIPTION)
    lateinit var asset: String

    @Option
    @Description(ATTRS_DESCRIPTION)
    override lateinit var attrs: MutableMap<String, String>

    @Option
    @Description(DOWNLOAD_DESCRIPTION)
    override var async: Boolean = false

    @Option
    @Description(DOWNLOAD_DESCRIPTION)
    override var defer: Boolean = false

    @Option
    @Description(DOWNLOAD_DESCRIPTION)
    override var module: Boolean = false

    @Option
    @Description(DOWNLOAD_DESCRIPTION)
    override var nomodule: Boolean = false

    @Option
    @Description(INLINED_DESCRIPTION)
    override var inlined: Boolean = false

    @Option
    @BooleanDefault(true)
    @Description(DOWNLOAD_DESCRIPTION)
    override var download: Boolean = true

    override fun toString(): String {
        return "ExtraJs(asset='$asset', async=$async, defer=$defer, module=$module, nomodule=$nomodule, inlined=$inlined, download=$download)"
    }
}
