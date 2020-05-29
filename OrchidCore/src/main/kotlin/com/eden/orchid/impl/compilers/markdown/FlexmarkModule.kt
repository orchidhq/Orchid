package com.eden.orchid.impl.compilers.markdown

import com.eden.orchid.api.registration.OrchidModule
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension
import com.vladsch.flexmark.ext.aside.AsideExtension
import com.vladsch.flexmark.ext.attributes.AttributesExtension
import com.vladsch.flexmark.ext.enumerated.reference.EnumeratedReferenceExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.toc.TocExtension
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.misc.Extension

class FlexmarkModule : OrchidModule() {

    override fun configure() {
        addToSet(
            Extension::class.java,
            AsideExtension.create(),
            AttributesExtension.create(),
            EnumeratedReferenceExtension.create(),
            StrikethroughSubscriptExtension.create(),
            TablesExtension.create(),
            TaskListExtension.create(),
            TocExtension.create(),
            AnchorLinkExtension.create()
        )
        addToSet(MutableDataSet::class.java)
    }
}
