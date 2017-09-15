package com.eden.orchid.kss.parser;

import com.eden.orchid.api.resources.resource.OrchidResource;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class KssParser {
    public static final Pattern STYLEGUIDE_PATTERN = Pattern.compile("(?i)(?<!No )Styleguide [0-9A-Za-z ]+");
    private Map<String, StyleguideSection> sections = new HashMap<String, StyleguideSection>();

    public KssParser(String... cssStrings) {
        for (String css : cssStrings) {
            addKssBlocks(css, "");
        }
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
    }

    public KssParser(List<OrchidResource> cssResources) {
        for (OrchidResource cssResource : cssResources) {
            addKssBlocks(cssResource.getContent(), cssResource.getReference().getTitle());
        }
    }

    private void addKssBlocks(String kssString, String fileName) {
        CommentParser parser = new CommentParser(kssString);
        for (String block : parser.blocks()) {
            if (hasStyleguideReference(block)) {
                addStyleguideSection(block, "");
            }
        }
    }

    public static boolean hasStyleguideReference(String block) {
        String[] lines = block.split("\n\n");
        String styleguideLine = lines[lines.length - 1];
        return STYLEGUIDE_PATTERN.matcher(styleguideLine).find();
    }

    public void addStyleguideSection(String block, String filename) {
        StyleguideSection section = new StyleguideSection(block, filename);
        sections.put(section.getSectionReference(), section);
    }

    public Map<String, StyleguideSection> getStyleguideSections() {
        return sections;
    }

    public StyleguideSection getStyleguideSection(String section) {
        return sections.get(section);
    }
}
