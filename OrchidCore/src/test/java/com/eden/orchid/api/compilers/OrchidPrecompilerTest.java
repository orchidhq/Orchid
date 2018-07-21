package com.eden.orchid.api.compilers;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public final class OrchidPrecompilerTest {

    private OrchidPrecompiler underTest;

    @BeforeEach
    public void testSetup() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
        underTest = new OrchidPrecompiler(100) {
            @Override public EdenPair<String, Map<String, Object>> getEmbeddedData(String extension, String input) { return null; }
            @Override public boolean shouldPrecompile(String extension, String input) { return false; }
        };
    }

    @Test
    public void getCompilerExtensions() throws Throwable {
        assertThat(underTest.getPriority(), is(equalTo(100)));
    }
}
