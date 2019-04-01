package com.eden.orchid.wiki.adapter

import com.eden.orchid.api.theme.components.ModularType
import com.eden.orchid.wiki.model.WikiSection

interface WikiAdapter : ModularType {

    fun loadWikiPages(section: WikiSection) : WikiSection?

}