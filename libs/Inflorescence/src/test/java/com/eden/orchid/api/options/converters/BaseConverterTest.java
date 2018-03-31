package com.eden.orchid.api.options.converters;

import com.eden.orchid.api.options.Extractor;
import com.eden.orchid.api.options.OptionExtractor;
import org.json.JSONObject;

import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class BaseConverterTest {

    private Extractor extractor;

    public void setupTest(Set<OptionExtractor> extractors) {
        extractor = new Extractor(extractors, null) {
            @Override
            protected <T> T getInstance(Class<T> clazz) {
                return null;
            }
        };
    }

    public void testExtractOption(
            final Object underTest,
            final String optionName,
            final Object sourceValue,
            final Object expectedOriginalValue,
            final Object expectedExtractedValue) throws Throwable {

        final JSONObject options = new JSONObject();
        if(sourceValue != null) {
            if(sourceValue.toString().equals("_nullValue")) {
                options.put(optionName, (String) null);
            }
            else {
                options.put(optionName, sourceValue);
            }
        }

        assertThat(underTest.getClass().getField(optionName).get(underTest), is(equalTo(expectedOriginalValue)));
        extractor.extractOptions(underTest, options);
        assertThat(underTest.getClass().getField(optionName).get(underTest), is(equalTo(expectedExtractedValue)));
    }

}
