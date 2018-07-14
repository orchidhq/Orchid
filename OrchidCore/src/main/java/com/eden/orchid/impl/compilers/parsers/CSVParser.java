package com.eden.orchid.impl.compilers.parsers;

import com.eden.orchid.api.compilers.OrchidParser;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CSVParser extends OrchidParser {

    @Inject
    public CSVParser() {
        super(100);
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"csv", "tsv"};
    }

    @Override
    public Map<String, Object> parse(String extension, String input) {
        List<String[]> allRows;

        if(extension.equalsIgnoreCase("csv")) {
            CsvParserSettings settings = new CsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            CsvParser parser = new CsvParser(settings);

            allRows = parser.parseAll(IOUtils.toInputStream(input, Charset.forName("UTF-8")));
        }
        else {
            TsvParserSettings settings = new TsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            TsvParser parser = new TsvParser(settings);

            allRows = parser.parseAll(IOUtils.toInputStream(input, Charset.forName("UTF-8")));
        }

        List<Map<String, Object>> array = new ArrayList<>();

        String[] cols = allRows.get(0);

        for (int i = 1; i < allRows.size(); i++) {
            Map<String, Object> object = new HashMap<>();

            for (int j = 0; j < cols.length; j++) {
                object.put(cols[j], allRows.get(i)[j]);
            }

            array.add(object);
        }

        Map<String, Object> object = new HashMap<>();
        object.put(OrchidParser.arrayAsObjectKey, array);
        return object;
    }

    @Override
    public String serialize(String extension, Object input) {
        return "";
    }
}
