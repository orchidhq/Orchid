package com.eden.orchid.copper

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.OrchidMenu

class CopperTilesTag : TemplateTag("tiles", Type.Simple, true) {

    @Option
    lateinit var tileMenu: OrchidMenu

    override fun parameters() = arrayOf(::tileMenu.name)
}
