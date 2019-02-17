package com.eden.orchid.api.compilers;

import com.eden.orchid.testhelpers.BaseOrchidTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public final class OrchidParserTest extends BaseOrchidTest {

    private OrchidParser underTest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        underTest = new OrchidParser(100) {
            @Override public String[] getSourceExtensions() { return new String[] { "" }; }
            @Override public Map<String, Object> parse(String extension, String input) { return new HashMap<>(); }
            @Override public String serialize(String extension, Object input) { return ""; }
        };
    }

    @Test
    public void getCompilerExtensions() throws Throwable {
        assertThat(underTest.getPriority(), is(equalTo(100)));
        assertThat(underTest.getDelimiter(), is(nullValue()));
        assertThat(underTest.getDelimiterString(), is(nullValue()));
    }
}
