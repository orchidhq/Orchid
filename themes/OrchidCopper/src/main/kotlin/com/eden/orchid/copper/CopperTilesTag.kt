package com.eden.orchid.copper

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.OrchidMenu
import com.eden.orchid.api.theme.pages.OrchidExternalPage
import com.eden.orchid.api.theme.pages.OrchidPage
import javax.inject.Inject

class CopperTilesTag : TemplateTag("tiles", Type.Simple, true) {

    @Option
    lateinit var tileMenu: OrchidMenu

    @Option
    lateinit var tiles: List<AncestorTile>

    override fun parameters(): Array<String> {
        return arrayOf("tiles", "tileMenu")
    }

    override fun onPostExtraction() {
        for (tile in tiles) {
            tile.tag = this
        }
    }
}

@Deprecated("This has been replaced with an OrchidMenu. Configure tileMenu instead.")
class AncestorTile : OptionsHolder {
    @Option
    lateinit var parentNodes: List<ParentTile>

    @Option
    lateinit var title: String

    var tag: CopperTilesTag? = null
        set(value) {
            field = value
            for (tile in parentNodes) {
                tile.tag = this.tag
            }
        }
}

@Deprecated("This has been replaced with an OrchidMenu. Configure tileMenu instead.")
class ParentTile : OptionsHolder {

    @Option
    lateinit var childrenNodes: List<ChildTile>

    @Option
    lateinit var width: String

    @Option
    var vertical: Boolean = false

    var tag: CopperTilesTag? = null
        set(value) {
            field = value
            for (tile in childrenNodes) {
                tile.tag = this.tag!!
            }
        }
}

@Deprecated("This has been replaced with an OrchidMenu. Configure tileMenu instead.")
class ChildTile : OptionsHolder {

    @Option
    lateinit var link: String

    @Option
    lateinit var title: String

    @Option
    lateinit var subtitle: String

    @Option
    var excerpt: String = ""
        get() = linkedPage?.content?.let {
            if (it.length > 240) {
                it.replace("(<.*?>)|(&.*?;)|([ ]{2,})".toRegex(), "").substring(0, 240) + "..."
            } else {
                it
            }
        } ?: field

    @Option
    lateinit var color: String

    @Option
    var bold: Boolean = false

    lateinit var tag: CopperTilesTag

    val linkedPage: OrchidPage? by lazy {
        if (link.toLowerCase().let { it.startsWith("http://") || it.startsWith("https://") }) {
            null
        } else {
            tag.context.findPage(null, null, link)
        }
    }

    fun hasLinkUrl(): Boolean {
        if (!EdenUtils.isEmpty(link)) {
            if (link.toLowerCase().let { it.startsWith("http://") || it.startsWith("https://") }) {
                return true
            } else {
                return linkedPage != null
            }
        }
        return false
    }

    fun linkIsExternal(): Boolean {
        return if(link.toLowerCase().let { it.startsWith("http://") || it.startsWith("https://") }) {
            true
        }
        else {
            linkedPage != null && linkedPage is OrchidExternalPage
        }
    }

    fun getLinkUrl(): String = linkedPage?.link ?: link

}
