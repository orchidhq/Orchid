package com.eden.orchid.impl.compilers.parsers;

import com.eden.orchid.api.compilers.OrchidParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public final class YamlParser extends OrchidParser {

    @Inject
    public YamlParser() {
        super(100);
    }

    @Override
    public String getDelimiter() {
        return "-";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"yaml", "yml"};
    }

    @Override
    public JSONObject parse(String extension, String input) {
        try {
            Object yamlData = new Yaml().load(input);

            if(yamlData instanceof Map) {
                return new JSONObject((Map<String, Object>) yamlData);
            }
            else if(yamlData instanceof List) {
                JSONObject object = new JSONObject();
                object.put(OrchidParser.arrayAsObjectKey, new JSONArray((List<Object>) yamlData));
                return object;
            }
        }
        catch (Exception e) {}

        return null;
    }

    @Override
    public String serialize(String extension, Object input) {
        return new Yaml().dump(input);
    }
}
