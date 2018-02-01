package com.eden.orchid.kss.parser

import com.eden.common.util.EdenPair
import com.eden.common.util.EdenUtils
import org.apache.commons.lang3.StringUtils
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class StyleguideSection(val raw: String, val filename: String) {

    val commentSections: MutableList<String> = ArrayList()
    val tags: MutableMap<String, String> = HashMap()

    var name: String = ""
    var description: String = ""
    var styleGuideReference: String = ""
    var styleGuidePath: Array<String> = emptyArray()

    var parent: StyleguideSection? = null
    var children: MutableList<StyleguideSection> = ArrayList()

    var modifiersText: String = ""
    var modifiers: List<Modifier> = ArrayList()

    val markup: String
        get() = getTaggedSection("markup")

    val stylesheet: String
        get() = getTaggedSection("source")

    private val ownWrapper: String
        get() {
            val formattedMarkup = "" + getTaggedSection("wrapper")
            return if (!EdenUtils.isEmpty(formattedMarkup) && formattedMarkup.contains("-markup")) {
                formattedMarkup
            } else {
                "-markup"
            }
        }

    // if we have our own wrapper, add it now
    // if parent has a wrapper, add it now
    val wrapper: String
        get() {
            var formattedMarkup = ownWrapper
            val parentWrapper = if (parent != null) parent!!.wrapper else ""
            if (!EdenUtils.isEmpty(parentWrapper) && parentWrapper.contains("-markup")) {
                formattedMarkup = parentWrapper.replace("-markup".toRegex(), formattedMarkup)
            }

            return formattedMarkup
        }

    val depth: Int
        get() = if (!EdenUtils.isEmpty(styleGuidePath)) styleGuidePath.size else 0

    init {
        // Split comment block into sections, then put into a list so they can be manipulated
        val commentSectionsArray = raw.split("\n\n")

        for (commentSection in commentSectionsArray) {
            this.commentSections.add(commentSection)
        }

        for (i in commentSectionsArray.indices) {
            val section = commentSectionsArray[i]

            // line is Styleguide section. Parse then remove it
            if (isSectionReferenceCommentLine(section)) {
                val cleaned = section.trim().replace("\\.$", "")
                val m = Pattern.compile("Styleguide (.+)").matcher(cleaned)
                this.styleGuideReference = if (m.find()) StringUtils.strip(m.group(1), " .") else "1"
                this.styleGuidePath = this.styleGuideReference.split("\\.".toRegex()).toTypedArray()
                this.commentSections.removeAt(i)
                continue
            }

            // Line is tagged section, leave for now until needed
            if (isTaggedSection(section)) {
                val taggedSection = parseTaggedSection(section)
                tags.put(taggedSection!!.first.toLowerCase(), taggedSection.second)
                continue
            }

            // line is modifiers text
            if (isModifiersSection(section)) {
                this.modifiersText = section
                modifiers = parseModifiersSection(section)
                continue
            }

            // We don't have a name, so make this the name first
            if (EdenUtils.isEmpty(this.name)) {
                this.name = section
            } else {
                description += section + "\n"
            }// we already did the name, start adding to the description
        }
    }

    fun getTaggedSection(sectionKey: String): String {
        return tags.getOrDefault(sectionKey.toLowerCase(), "")
    }

    fun formatMarkup(modifier: Modifier): String {
        // set markup and add modifier class
        val markup = getTaggedSection("markup")
        var formattedMarkup = if (!EdenUtils.isEmpty(markup) && markup.contains("-modifierClass")) {
            markup.replace("-modifierClass".toRegex(), modifier.className())
        } else {
            "<div class='-modifierClass'>Markup</div>".replace("-modifierClass".toRegex(), modifier.className())
        }

        // if we have our own wrapper, add it now
        val wrapper = wrapper
        if (!EdenUtils.isEmpty(wrapper) && wrapper.contains("-markup")) {
            formattedMarkup = wrapper.replace("-markup".toRegex(), formattedMarkup)
        }

        return formattedMarkup
    }

    fun isParentOf(styleguideSection: StyleguideSection): Boolean {
        if (styleguideSection.styleGuidePath.size == styleGuidePath.size + 1) {
            for (i in styleGuidePath.indices) {
                if (!styleguideSection.styleGuidePath[i].equals(styleGuidePath[i], ignoreCase = true)) {
                    return false
                }
            }

            return true
        }

        return false
    }


// Implementation Helpers
//----------------------------------------------------------------------------------------------------------------------

    private fun isSectionReferenceCommentLine(section: String): Boolean {
        return KssParser.STYLEGUIDE_PATTERN.matcher(section).find()
    }

    private fun isTaggedSection(section: String): Boolean {
        return section.matches(TAGGED_SECTION_REGEX.toRegex())
    }

    private fun parseTaggedSection(section: String): EdenPair<String, String>? {
        val m = Pattern.compile(TAGGED_SECTION_REGEX).matcher(section)
        return if (m.find()) {
            EdenPair(m.group(1), m.group(2))
        } else null
    }

    private fun isModifiersSection(section: String): Boolean {
        return section.matches(MODIFIERS_SECTION_REGEX.toRegex())
    }

    private fun parseModifiersSection(section: String?): ArrayList<Modifier> {
        var lastIndent: Int? = null
        val modifiers = ArrayList<Modifier>()

        if (section == null) {
            return modifiers
        }

        val precedingWhitespacePattern = Pattern.compile("^\\s*")

        for (modifierLine in section.split("\n")) {
            if ("" == modifierLine.trim()) {
                continue
            }
            val m = precedingWhitespacePattern.matcher(modifierLine)
            var match = ""
            if (m.find()) {
                match = m.group()
            }
            val indent = match.length

            if (lastIndent != null && indent > lastIndent) {
                //?
            } else {
                val name = modifierLine.split(" - ")[0].trim()
                val desc = modifierLine.split(" - ")[1].trim()
                val modifier = Modifier(name, desc)
                modifiers.add(modifier)
            }

            lastIndent = indent
        }
        return modifiers
    }

    companion object {
        internal val MODIFIERS_SECTION_REGEX = "(?sm)(^(.*)(\\s+-\\s+)(.*)$)+"
        internal val TAGGED_SECTION_REGEX = "(?s)(^\\w*):(.*)"
    }
}


