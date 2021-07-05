package com.eden.orchid.api.compilers;

import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.testhelpers.OrchidUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.io.OutputStream;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public final class OrchidCompilerTest implements OrchidUnitTest {

    private OrchidCompiler underTest;

    @BeforeEach
    public void setUp() {
        underTest = new OrchidCompiler(100) {
            @Override public void compile(OutputStream os, @Nullable OrchidResource resource, String extension, String input, Map<String, Object> data) { }
            @Override public String getOutputExtension() { return ""; }
            @Override public String[] getSourceExtensions() { return new String[] { "" }; }
        };
    }

    @Test
    public void getCompilerExtensions() throws Throwable {
        assertThat(underTest.getPriority(), is(equalTo(100)));
    }
}
