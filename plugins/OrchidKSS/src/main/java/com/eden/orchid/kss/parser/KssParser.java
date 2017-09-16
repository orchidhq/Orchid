package com.eden.orchid.kss.parser;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.resources.resource.OrchidResource;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class KssParser {
    public static final Pattern STYLEGUIDE_PATTERN = Pattern.compile("(?i)(?<!No )Styleguide [0-9A-Za-z ]+");
    private Map<String, StyleguideSection> sections = new HashMap<>();
    private Map<String, StyleguideSection> mappedSections = new LinkedHashMap<>();

    public KssParser(String... cssStrings) {
        for (String css : cssStrings) {
            addKssBlocks(css, "");
        }

        buildStyleguideHierarchy();
    }

    public KssParser(File... cssDirectories) throws IOException {
        for (File cssDirectory : cssDirectories) {
            if (cssDirectory.exists() && cssDirectory.isDirectory()) {
                Iterator<File> fileIterator = FileUtils.iterateFiles(cssDirectory, new String[]{"css", "less", "scss", "sass"}, true);
                while (fileIterator.hasNext()) {
                    File cssfile = fileIterator.next();
                    addKssBlocks(FileUtils.readFileToString(cssfile), cssfile.getName());
                }
            }
        }

        buildStyleguideHierarchy();
    }

    public KssParser(List<OrchidResource> cssResources) {
        for (OrchidResource cssResource : cssResources) {
            addKssBlocks(cssResource.getContent(), cssResource.getReference().getTitle());
        }

        buildStyleguideHierarchy();
    }

    private void addKssBlocks(String kssString, String fileName) {
        CommentParser parser = new CommentParser(kssString);
        for (String block : parser.blocks()) {
            if (hasStyleguideReference(block)) {
                addStyleguideSection(block, fileName);
            }
        }
    }

    private boolean hasStyleguideReference(String block) {
        String[] lines = block.split("\n\n");
        String styleguideLine = lines[lines.length - 1];
        return STYLEGUIDE_PATTERN.matcher(styleguideLine).find();
    }

    private void addStyleguideSection(String block, String filename) {
        StyleguideSection section = new StyleguideSection(block, filename);
        sections.put(section.getStyleGuideReference(), section);
    }

    public Map<String, StyleguideSection> getStyleguideSections() {
        return sections;
    }

    public StyleguideSection getStyleguideSection(String section) {
        return sections.get(section);
    }

    private void buildStyleguideHierarchy() {
        if(sections.size() == 0) {
            return;
        }

        int maxLevels = sections
                .values()
                .stream()
                .max(Comparator.comparingInt(value -> value.getStyleGuidePath().length))
                .get().getStyleGuidePath().length;
        int minLevels = sections
                .values()
                .stream()
                .min(Comparator.comparingInt(value -> value.getStyleGuidePath().length))
                .get().getStyleGuidePath().length;

        Map<Integer, List<StyleguideSection>> sectionDepths = new TreeMap<>();
        for(StyleguideSection section : sections.values()) {
            if(!sectionDepths.containsKey(section.getDepth())) {
                sectionDepths.put(section.getDepth(), new ArrayList<>());
            }

            sectionDepths.get(section.getDepth()).add(section);
        }

        for (int i = minLevels; i <= maxLevels; i++) {
            if(!sectionDepths.containsKey(i)) {
                continue;
            }
            if(!sectionDepths.containsKey(i - 1)) {
                continue;
            }

            for(StyleguideSection sectionAtDepth : sectionDepths.get(i)) {
                for(StyleguideSection possibleParent : sectionDepths.get(i - 1)) {
                    if(possibleParent.getTerminatingPath().equalsIgnoreCase(sectionAtDepth.getPathAtLevel(i - 1))) {
                        sectionAtDepth.setParent(possibleParent);
                        break;
                    }
                }
            }
        }

        for(StyleguideSection section : sections.values()) {
            if(section.getDepth() > 1 && section.getParent() == null) {
                Clog.w("No parent section defined for section {}", section.getStyleGuideReference());
            }

            for(StyleguideSection possibleChild : sections.values()) {
                if(possibleChild.getParent() != null && possibleChild.getParent() == section) {
                    section.getChildren().add(possibleChild);
                }
            }
        }
    }
}
