package com.eden.orchid.impl.compilers.parsers;

import com.eden.orchid.api.compilers.OrchidParser;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import javax.inject.Inject;
import java.util.Map;
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
    public Map<String, Object> parse(String extension, String input) {
        Toml toml = new Toml().read(input);
        return toml.toMap();
    }

    @Override
    public String serialize(String extension, Object input) {
        TomlWriter tomlWriter = new TomlWriter();
        return tomlWriter.write(input);
    }
}
