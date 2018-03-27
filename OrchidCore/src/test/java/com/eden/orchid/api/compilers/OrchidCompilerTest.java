package com.eden.orchid.api.compilers;

import com.caseyjbrooks.clog.Clog;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups={"unit"})
public final class OrchidCompilerTest {

    private OrchidCompiler underTest;

    @BeforeMethod
    public void testSetup() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
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
