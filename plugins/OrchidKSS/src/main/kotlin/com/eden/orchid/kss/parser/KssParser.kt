package com.eden.orchid.kss.parser

import clog.Clog
import com.eden.orchid.api.resources.resource.OrchidResource
import java.util.TreeMap
import java.util.regex.Pattern

class KssParser(cssResources: List<OrchidResource>) {

    val sections = HashMap<String, StyleguideSection>()

    init {
        for (cssResource in cssResources) {
            addKssBlocks(cssResource.content, cssResource.reference.title)
        }

        buildStyleguideHierarchy()
    }

    private fun addKssBlocks(kssString: String, fileName: String) {
        val blocks = CommentParser.parse(kssString)

        for (block in blocks) {
            if (hasStyleguideReference(block)) {
                val section = StyleguideSection(block, fileName)

                if(sections.containsKey(section.styleGuideReference)) {
                    Clog.w("Duplicate Styleguide section: ${section.styleGuideReference}")
                }

                sections.put(section.styleGuideReference, section)
            }
        }
    }

    private fun hasStyleguideReference(block: String): Boolean {
        val styleguideLine = block.split("\n\n").last()
        return STYLEGUIDE_PATTERN.matcher(styleguideLine).find()
    }

    private fun buildStyleguideHierarchy() {
        if (sections.size == 0) {
            return
        }

        val maxLevels: Int = sections
                .values
                .maxByOrNull { it.styleGuidePath.size }!!
                .styleGuidePath.size
        val minLevels: Int  = sections
                .values
                .maxByOrNull { it.styleGuidePath.size }!!
                .styleGuidePath.size

        val sectionDepths = TreeMap<Int, MutableList<StyleguideSection>>()
        for (section in sections.values) {
            if (!sectionDepths.containsKey(section.depth)) {
                sectionDepths.put(section.depth, ArrayList())
            }

            sectionDepths[section.depth]!!.add(section)
        }

        for (i in minLevels..maxLevels) {
            if (!sectionDepths.containsKey(i)) {
                continue
            }
            if (!sectionDepths.containsKey(i - 1)) {
                continue
            }

            for (sectionAtDepth in sectionDepths[i]!!) {
                for (possibleParent in sectionDepths[i - 1]!!) {
                    if (possibleParent.isParentOf(sectionAtDepth)) {
                        sectionAtDepth.parent = possibleParent
                        break
                    }
                }
            }
        }

        for (section in sections.values) {
            if (section.depth > 1 && section.parent == null) {
                Clog.w("No parent section defined for section {}", section.styleGuideReference)
            }

            for (possibleChild in sections.values) {
                if (possibleChild.parent != null && possibleChild.parent === section) {
                    section.children.add(possibleChild)
                }
            }
        }
    }

    companion object {
        val STYLEGUIDE_PATTERN = Pattern.compile("(?i)(?<!No )Styleguide [0-9A-Za-z ]+")
    }
}

