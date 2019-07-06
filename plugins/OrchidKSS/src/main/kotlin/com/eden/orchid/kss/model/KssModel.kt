package com.eden.orchid.kss.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kss.pages.KssPage

class KssModel(
    var sections: MutableMap<String?, List<KssPage>>
) : OrchidGenerator.Model{

    override val allPages: List<OrchidPage>
        get() = sections.values.flatten()

    fun getSectionRoot(sectionKey: String?): List<KssPage> {
        val section = if(!EdenUtils.isEmpty(sectionKey)) sectionKey else null
        return sections[section]!!.filter { it.parent == null }
    }
}
