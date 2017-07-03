package com.eden.orchid.impl.compilers.frontmatter;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.utilities.ObservableTreeSet;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class FrontMatterPrecompiler extends OrchidPrecompiler {

    private OrchidContext context;

    private static final String defaultType = "yml";
    private static final String precompilerExtension = "twig";

    private List<EdenPair<String, OrchidParser>> delimiters;

    @Inject
    public FrontMatterPrecompiler(OrchidContext context, Set<OrchidParser> parsers) {
        this.context = context;
        this.priority = 100;

        delimiters = new ArrayList<>();

        for(OrchidParser parser : new ObservableTreeSet<>(parsers)) {
            if(parser.getDelimiter() != null) {
                delimiters.add(new EdenPair<>(parser.getDelimiter(), parser));
            }
        }

        delimiters.add(new EdenPair<>("-", null));
    }

    @Override
    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        EdenPair<JSONObject, Integer> frontMatter = parseFrontMatter(input);

        if(frontMatter.second != 0) {
            return new EdenPair<>(input.substring(frontMatter.second), new JSONElement(frontMatter.first));
        }
        else {
            return new EdenPair<>(input, null);
        }
    }

    @Override
    public String precompile(String input, Object... data) {
        return context.compile(precompilerExtension, input, data);
    }

    public boolean shouldPrecompile(String input) {
        return getFrontMatterHeader(input).isValidHeader;
    }

    private EdenPair<JSONObject, Integer> parseFrontMatter(String input) {
        FrontMatterHeader header = getFrontMatterHeader(input);

        if(header.isValidHeader) {
            final String frontMatterText = input.substring(header.fmStart, header.fmEnd);

            JSONObject frontMatter;

            if(header.parser != null) {
                frontMatter = header.parser.parse("" + header.parser.getDelimiter(), frontMatterText);
            }
            else {
                frontMatter = header.extensions
                        .stream()
                        .map(ext -> context.parse(ext, frontMatterText))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElseGet(JSONObject::new);
            }

            return new EdenPair<>(frontMatter, header.contentStart);
        }

        return new EdenPair<>(null, 0);
    }

    private FrontMatterHeader getFrontMatterHeader(String input) {
        for(EdenPair<String, OrchidParser> delimiter : delimiters) {
            Matcher m = Pattern.compile("^" + delimiter.first + "{3}(\\w+)?$", Pattern.MULTILINE).matcher(input);

            int matches = 0;
            int fmStart = 0;
            int fmEnd = 0;

            int contentStart = 0;

            // if we find a match, get the group
            while (m.find()) {
                if (matches == 0) {
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

            if (matches == 2) {
                FrontMatterHeader header = new FrontMatterHeader(true, fmStart, fmEnd, contentStart);

                if(delimiter.second != null) {
                    header.parser = delimiter.second;
                    header.delimiter = delimiter.first;
                }
                else {
                    header.delimiter = null;

                    String parsedType = input.substring(0, header.fmStart).replaceAll(delimiter.first + "{3}", "");

                    header.extensions = new ArrayList<>();
                    if(!EdenUtils.isEmpty(parsedType)) {
                        header.extensions.add(parsedType);
                    }

                    header.extensions.add(defaultType);
                }

                return header;
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
        List<String> extensions;
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
                    ", extensions=" + extensions +
                    ", parser=" + parser +
                    '}';
        }
    }
}

