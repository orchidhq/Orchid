package com.eden.orchid.api.compilers;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups={"unit"})
public final class OrchidPrecompilerTest {

    private OrchidPrecompiler underTest;

    @BeforeMethod
    public void testSetup() {
        Clog.setMinPriority(Clog.Priority.FATAL);
        underTest = new OrchidPrecompiler(100) {
            @Override public EdenPair<String, JSONElement> getEmbeddedData(String input) { return null; }
            @Override public String precompile(String input, Map<String, Object> data) { return ""; }
            @Override public boolean shouldPrecompile(String input) { return false; }
        };
    }

    @Test
    public void getCompilerExtensions() throws Throwable {
        assertThat(underTest.getPriority(), is(equalTo(100)));
    }
}
