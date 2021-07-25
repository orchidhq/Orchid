package com.eden.orchid.copper

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.OrchidMenu

@Description(
    "The tiles homepage menu of the Copper theme",
    name = "Copper Tiles"
)
class CopperTilesTag : TemplateTag("tiles", Type.Simple, true) {

    @Option
    lateinit var tileMenu: OrchidMenu

    override fun parameters() = arrayOf(::tileMenu.name)
}
