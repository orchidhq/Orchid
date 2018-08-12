package com.eden.orchid.impl.compilers.frontmatter;

import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Option;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public final class FrontMatterPrecompiler extends OrchidPrecompiler {

    private final OrchidContext context;

    private final List<EdenPair<String, OrchidParser>> delimiters;

    @Option
    public List<CustomDelimiter> customDelimeters;

    @Inject
    public FrontMatterPrecompiler(OrchidContext context, Set<OrchidParser> parsers) {
        super(100);
        this.context = context;

        delimiters = new ArrayList<>();

        for(OrchidParser parser : new TreeSet<>(parsers)) {
            if(parser.getDelimiter() != null) {
                delimiters.add(new EdenPair<>(parser.getDelimiter(), parser));
            }
        }
    }

    @Override
    public EdenPair<String, Map<String, Object>> getEmbeddedData(String extension, String input) {
        EdenPair<Map<String, Object>, Integer> frontMatter = parseFrontMatter(extension, input);

        if(frontMatter.second != 0) {
            return new EdenPair<>(input.substring(frontMatter.second), frontMatter.first);
        }
        else {
            return new EdenPair<>(input, null);
        }
    }

    public boolean shouldPrecompile(String extension, String input) {
        return getFrontMatterHeader(extension, input).isValidHeader;
    }

    private EdenPair<Map<String, Object>, Integer> parseFrontMatter(String extension, String input) {
        FrontMatterHeader header = getFrontMatterHeader(extension, input);

        if(header.isValidHeader) {
            final String frontMatterText = input.substring(header.fmStart, header.fmEnd);

            Map<String, Object> frontMatter = null;

            if(!EdenUtils.isEmpty(header.extension)) {
                frontMatter = context.parse(header.extension, frontMatterText);
            }

            if(frontMatter == null) {
                frontMatter = header.parser.parse("" + header.parser.getDelimiter(), frontMatterText);
            }

            if(frontMatter == null) {
                frontMatter = new HashMap<>();
            }

            return new EdenPair<>(frontMatter, header.contentStart);
        }

        return new EdenPair<>(null, 0);
    }

    private FrontMatterHeader getFrontMatterHeader(String extension, String input) {
        for(EdenPair<String, OrchidParser> delimiter : delimiters) {
            Matcher m = Pattern.compile("^" + delimiter.first + "{3}(\\w+)?$", Pattern.MULTILINE).matcher(input);

            int matches = 0;
            int firstMatchStart = 0;
            int fmStart = 0;
            int fmEnd = 0;

            int contentStart = 0;

            // if we find a match, get the group
            while (m.find()) {
                if (matches == 0) {
                    firstMatchStart = m.start();
                    fmStart = m.end();
                    matches++;
                }
                else if (matches == 1) {
                    fmEnd = m.start();
                    contentStart = m.end();
                    matches++;
                    break;
                }
            }

            if (matches == 2 && firstMatchStart == 0) {
                FrontMatterHeader header = new FrontMatterHeader(true, fmStart, fmEnd, contentStart);

                header.parser = delimiter.second;
                header.delimiter = delimiter.first;

                String parsedType = input.substring(0, header.fmStart).replaceAll(delimiter.first + "{3}", "");

                if(!EdenUtils.isEmpty(parsedType)) {
                    header.extension = parsedType;
                }

                return header;
            }
        }

        if(!EdenUtils.isEmpty(customDelimeters)) {
            for (CustomDelimiter delimiter : customDelimeters) {
                if(EdenUtils.isEmpty(delimiter.fileExtensions) || delimiter.fileExtensions.stream().anyMatch((ext) -> ext.equalsIgnoreCase(extension))) {
                    Matcher mStart = Pattern.compile(delimiter.regex, Pattern.DOTALL).matcher(input);

                    if (mStart.find()) {
                        FrontMatterHeader header = new FrontMatterHeader(true, mStart.start(delimiter.group), mStart.end(delimiter.group), mStart.end());

                        header.parser = context.parserFor(delimiter.parser);
                        header.delimiter = "";

                        String parsedType = delimiter.parser;

                        if (!EdenUtils.isEmpty(parsedType)) {
                            header.extension = parsedType;
                        }

                        return header;
                    }
                }
            }
        }

        return new FrontMatterHeader(false);
    }

    private static class FrontMatterHeader {
        boolean isValidHeader;
        int fmStart;
        int fmEnd;
        int contentStart;

        String delimiter;
        String extension;
        OrchidParser parser;

        FrontMatterHeader(boolean isValidHeader, int fmStart, int fmEnd, int contentStart) {
            this.isValidHeader = isValidHeader;
            this.fmStart = fmStart;
            this.fmEnd = fmEnd;
            this.contentStart = contentStart;
        }

        FrontMatterHeader(boolean isValidHeader) {
            this.isValidHeader = isValidHeader;
        }

        @Override
        public String toString() {
            return "FrontMatterHeader{" +
                    "isValidHeader=" + isValidHeader +
                    ", fmStart=" + fmStart +
                    ", fmEnd=" + fmEnd +
                    ", contentStart=" + contentStart +
                    ", delimiter='" + delimiter + '\'' +
                    ", extension=" + extension +
                    ", parser=" + parser +
                    '}';
        }
    }

    public static class CustomDelimiter implements OptionsHolder {

        @Option
        public String regex;

        @Option
        public int group;

        @Option
        public String parser;

        @Option
        public List<String> fileExtensions;

    }

}

