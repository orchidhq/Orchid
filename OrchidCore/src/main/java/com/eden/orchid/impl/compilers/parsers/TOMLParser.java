package com.eden.orchid.impl.compilers.parsers;

import com.eden.orchid.api.compilers.OrchidParser;
import com.moandjiezana.toml.Toml;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.regex.Pattern;

public final class TOMLParser extends OrchidParser {

    @Inject
    public TOMLParser() {
        super(100);
    }

    @Override
    public String getDelimiter() {
        return Pattern.quote(getDelimiterString());
    }

    @Override
    public String getDelimiterString() {
        return "+";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"tml", "toml"};
    }

    @Override
    public JSONObject parse(String extension, String input) {
        Toml toml = new Toml().read(input);
        return new JSONObject(toml.toMap());
    }
}
