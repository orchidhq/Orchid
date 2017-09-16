package com.eden.orchid.kss.parser;

import com.eden.common.util.EdenPair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyleguideSection {

    private static final String MODIFIERS_SECTION_REGEX = "(?sm)(^(.*)(\\s+-\\s+)(.*)$)+";
    private static final String TAGGED_SECTION_REGEX = "(?s)(^\\w*):(.*)";

    private String raw;
    private String filename;
    private List<String> commentSections;

    private Map<String, String> tags;

    @Getter private String name;
    @Getter private String description;
    @Getter private String styleGuideReference;

    @Getter private String modifiersText;
    @Getter private List<Modifier> modifiers;

    public StyleguideSection(String commentText, String filename) {
        this.raw = commentText;
        this.filename = filename;
        this.tags = new HashMap<>();

        // Split comment block into sections, then put into a list so they can be manipulated
        String[] commentSectionsArray = raw.split("\n\n");
        this.commentSections = new ArrayList<>();
        for (String commentSection : commentSectionsArray) {
            this.commentSections.add(commentSection);
        }

        this.name = null;
        this.description = "";

        for (int i = 0; i < commentSectionsArray.length; i++) {
            String section = commentSectionsArray[i];

            // line is Styleguide section. Parse then remove it
            if(isSectionReferenceCommentLine(section)) {
                String cleaned = section.trim().replaceAll("\\.$", "");
                Matcher m = Pattern.compile("Styleguide (.+)").matcher(cleaned);
                if (m.find()) {
                    this.styleGuideReference = m.group(1);
                }
                else {
                    this.styleGuideReference = "";
                }
                this.commentSections.remove(i);
                continue;
            }

            // Line is tagged section, leave for now until needed
            if(isTaggedSection(section)) {
                EdenPair<String, String> taggedSection = parseTaggedSection(section);
                tags.put(taggedSection.first.toLowerCase(), taggedSection.second);
                continue;
            }

            // line is modifiers text
            if (isModifiersSection(section)) {
                this.modifiersText = section;
                modifiers = parseModifiersSection(section);
                continue;
            }

            // We don't have a name, so make this the name first
            if(this.name == null) {
                this.name = section;
            }

            // we already did the name, start adding to the description
            else {
                description += section + "\n";
            }
        }
    }

    public String getMarkup() {
        return getTaggedSection("markup");
    }

    public String getStylesheet() {
        return getTaggedSection("source");
    }

    public String getTaggedSection(String sectionKey) {
        return tags.getOrDefault(sectionKey.toLowerCase(), "");
    }

    public String formatMarkup(Modifier modifier) {
        String markup = getTaggedSection("markup");
        String wrapper = getTaggedSection("wrapper");

        markup = markup.replaceAll("-modifierClass", modifier.className());
        markup = wrapper.replaceAll("-markup", markup);

        return markup;
    }

// Implementation Helpers
//----------------------------------------------------------------------------------------------------------------------

    private boolean isSectionReferenceCommentLine(String section) {
        return KssParser.STYLEGUIDE_PATTERN.matcher(section).find();
    }

    private boolean isTaggedSection(String section) {
        return section.matches(TAGGED_SECTION_REGEX);
    }

    private EdenPair<String, String> parseTaggedSection(String section) {
        Matcher m = Pattern.compile(TAGGED_SECTION_REGEX).matcher(section);
        if(m.find()) {
            return new EdenPair<>(m.group(1), m.group(2));
        }
        return null;
    }

    private boolean isModifiersSection(String section) {
        return section.matches(MODIFIERS_SECTION_REGEX);
    }

    private ArrayList<Modifier> parseModifiersSection(String section) {
        Integer lastIndent = null;
        ArrayList<Modifier> modifiers = new ArrayList<Modifier>();

        if (section == null) { return modifiers; }

        Pattern precedingWhitespacePattern = Pattern.compile("^\\s*");

        for (String modifierLine : section.split("\n")) {
            if ("".equals(modifierLine.trim())) { continue; }
            Matcher m = precedingWhitespacePattern.matcher(modifierLine);
            String match = "";
            if (m.find()) {
                match = m.group();
            }
            int indent = match.length();

            if (lastIndent != null && indent > lastIndent) {
                //?
            }
            else {
                String name = modifierLine.split(" - ")[0].trim();
                String desc = modifierLine.split(" - ")[1].trim();
                if (name != null && desc != null) {
                    Modifier modifier = new Modifier(name, desc);
                    modifiers.add(modifier);
                }
            }

            lastIndent = indent;
        }
        return modifiers;
    }

}
