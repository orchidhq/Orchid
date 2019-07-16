package com.eden.orchid.api.compilers;

import com.eden.orchid.testhelpers.OrchidUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public final class OrchidCompilerTest extends OrchidUnitTest {

    private OrchidCompiler underTest;

    @BeforeEach
    public void setUp() {
        underTest = new OrchidCompiler(100) {
            @Override public String compile(String extension, String input, Map<String, Object> data) { return ""; }
            @Override public String getOutputExtension() { return ""; }
            @Override public String[] getSourceExtensions() { return new String[] { "" }; }
        };
    }

    @Test
    public void getCompilerExtensions() throws Throwable {
        assertThat(underTest.getPriority(), is(equalTo(100)));
    }
}
