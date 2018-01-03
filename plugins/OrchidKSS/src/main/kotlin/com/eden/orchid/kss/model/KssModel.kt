package com.eden.orchid.kss.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.kss.pages.KssPage
import java.util.*
import javax.inject.Singleton

@Singleton
class KssModel {

    lateinit var sections: MutableMap<String?, List<KssPage>>

    fun initialize() {
        sections = LinkedHashMap()
    }

    fun getAllPages(): List<KssPage> {
        val allPages = ArrayList<KssPage>()
        sections.values.map { allPages.addAll(it) }
        return allPages
    }

    fun getSectionRoot(sectionKey: String?): List<KssPage> {
        val section = if(!EdenUtils.isEmpty(sectionKey)) sectionKey else null
        return sections[section]!!.filter { it.parent == null }
    }
}
