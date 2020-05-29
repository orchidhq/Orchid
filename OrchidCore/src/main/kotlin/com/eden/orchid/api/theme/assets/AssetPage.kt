package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.AssetMetadataArchetype
import com.eden.orchid.api.render.RenderService.RenderMode
import com.eden.orchid.api.resources.resource.ExternalResource
import com.eden.orchid.api.resources.resource.InlineResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.debugger
import java.util.HashMap

@Archetype(value = AssetMetadataArchetype::class, key = "assetmeta")
@Description(
    value = "A static asset, like Javascript, CSS, or an image.",
    name = "Asset"
)
open class AssetPage(
    val origin: AssetManagerDelegate,
    resource: OrchidResource,
    key: String?,
    title: String?
) : OrchidPage(
    resource,
    getAssetRenderMode(resource.reference.context, resource.reference),
    key,
    title
) {
    var isRendered = false

    @Option
    @Description("The asset alt text.")
    lateinit var alt: String

    open fun configureReferences() {
        reference = OrchidReference(resource.reference)// copy reference so we can update it
        reference.isUsePrettyUrl = false

        // it's just a local file, apply the prefix as needed
        if (origin.prefix != null) {
            reference.path = OrchidUtils.normalizePath(origin.prefix) + "/" + reference.path
        }
    }

    open val shouldInline: Boolean get() {
        return when {
            resource is InlineResource -> true
            else -> false
        }
    }

    open fun renderAssetToPage(): String? {
        return ""
    }

    protected fun renderAttrs(attrs: MutableMap<String, String>): String {
        return attrs.entries.joinToString(separator = " ", prefix = " ") { "${it.key}=\"${it.value}\"" }
    }

    override fun getLink(): String {
        return if (!isRendered) {
            val actualPage = context.assetManager.getActualAsset(origin, this, true)
            check(actualPage.isRendered)
            actualPage.getLink()
        } else {
            super.getLink()
        }
    }

    companion object {
        private fun getAssetRenderMode(context: OrchidContext, reference: OrchidReference): RenderMode {
            return if (context.isBinaryExtension(reference.outputExtension)) {
                RenderMode.BINARY
            } else {
                RenderMode.RAW
            }
        }
    }
}
