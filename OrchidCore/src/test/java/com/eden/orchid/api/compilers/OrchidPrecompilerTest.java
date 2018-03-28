package com.eden.orchid.api.compilers;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Test(groups={"unit"})
public final class OrchidPrecompilerTest {

    private OrchidPrecompiler underTest;

    @BeforeMethod
    public void testSetup() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
        underTest = new OrchidPrecompiler(100) {
            @Override public EdenPair<String, JSONElement> getEmbeddedData(String input) { return null; }
            @Override public boolean shouldPrecompile(String input) { return false; }
        };
    }

    @Test
    public void getCompilerExtensions() throws Throwable {
        assertThat(underTest.getPriority(), is(equalTo(100)));
    }
}
