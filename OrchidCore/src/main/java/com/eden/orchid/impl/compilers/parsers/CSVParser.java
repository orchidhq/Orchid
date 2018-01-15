package com.eden.orchid.impl.compilers.parsers;

import com.eden.orchid.api.compilers.OrchidParser;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.List;

public class CSVParser extends OrchidParser {

    @Inject
    public CSVParser() {
        super(100);
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"csv", "tsv"};
    }

    @Override
    public JSONObject parse(String extension, String input) {
        List<String[]> allRows;

        if(extension.equalsIgnoreCase("csv")) {
            CsvParserSettings settings = new CsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            CsvParser parser = new CsvParser(settings);

            allRows = parser.parseAll(IOUtils.toInputStream(input, Charset.defaultCharset()));
        }
        else {
            TsvParserSettings settings = new TsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            TsvParser parser = new TsvParser(settings);

            allRows = parser.parseAll(IOUtils.toInputStream(input, Charset.defaultCharset()));
        }

        JSONArray array = new JSONArray();

        String[] cols = allRows.get(0);

        for (int i = 1; i < allRows.size(); i++) {
            JSONObject object = new JSONObject();

            for (int j = 0; j < cols.length; j++) {
                object.put(cols[j], allRows.get(i)[j]);
            }

            array.put(object);
        }

        JSONObject object = new JSONObject();
        object.put(OrchidParser.arrayAsObjectKey, array);
        return object;
    }
}
