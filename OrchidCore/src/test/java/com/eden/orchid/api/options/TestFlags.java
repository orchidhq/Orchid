package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups={"options", "unit"})
public class TestFlags {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    private Map<String, String[]> flagsMap;
    private List<OrchidFlag> flagParsers;

    static class TestInjectionOneClass {
        public final String value;

        @Inject
        public TestInjectionOneClass(@Named("one") String value) {
            this.value = value;
        }
    }

    static class TestInjectionTwoClass {
        public final int value;

        @Inject
        public TestInjectionTwoClass(@Named("five") int value) {
            this.value = value;
        }
    }

    static class TestInjectionThreeClass {
        public final String value;

        @Inject
        public TestInjectionThreeClass(@Named("five") String value) {
            this.value = value;
        }
    }

    static class TestInjectionSixClass {
        public final String value;

        @Inject
        public TestInjectionSixClass(@Named("six") String value) {
            this.value = value;
        }
    }

    static class TestInjectionSevenClass {
        public final int value;

        @Inject
        public TestInjectionSevenClass(@Named("seven") int value) {
            this.value = value;
        }
    }

    @BeforeMethod
    public void setupTest() {
        flagsMap = new HashMap<>();
        flagsMap.put("-one",   new String[] {"-one",   "valueOne"});
        flagsMap.put("-two",   new String[] {"-two",   "valueTwo"});
        flagsMap.put("-three", new String[] {"-three", "valueThree"});
        flagsMap.put("-four",  new String[] {"-four",  "valueFour"});
        flagsMap.put("-five",  new String[] {"-five",  "17"});

        flagParsers = new ArrayList<>();
        flagParsers.add(new OrchidFlag("one", false, null) {});
        flagParsers.add(new OrchidFlag("two", false, null) {});
        flagParsers.add(new OrchidFlag("three", false, null) {});
        flagParsers.add(new OrchidFlag("five", false, null) {
            @Override
            public Object parseFlag(String[] options) {
                return Integer.parseInt(options[1]);
            }

            @Override
            public FlagType getFlagType() {
                return FlagType.INTEGER;
            }
        });
        flagParsers.add(new OrchidFlag("six", true, "valueSix") {});
        flagParsers.add(new OrchidFlag("seven", true, 14) {
            @Override
            public Object parseFlag(String[] options) {
                return Integer.parseInt(options[1]);
            }

            @Override
            public FlagType getFlagType() {
                return FlagType.INTEGER;
            }
        });
    }

// Run Actual Tests
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testParsingFlags() throws Throwable {
        OrchidFlags.instance = new OrchidFlags(flagParsers);
        AbstractModule module = OrchidFlags.getInstance().parseFlags(flagsMap);

        // verify that the options get parsed correctly
        assertThat(OrchidFlags.getInstance().getString("one"),    is(equalTo("valueOne")));
        assertThat(OrchidFlags.getInstance().getString("two"),    is(equalTo("valueTwo")));
        assertThat(OrchidFlags.getInstance().getString("three"),  is(equalTo("valueThree")));
        assertThat(OrchidFlags.getInstance().getString("four"),   isEmptyOrNullString()); // "four" has no parser for it
        assertThat(OrchidFlags.getInstance().getInteger("five"),  is(equalTo(17))); // "five"'s parser returned an int
        assertThat(OrchidFlags.getInstance().getString("six"),    is(equalTo("valueSix")));
        assertThat(OrchidFlags.getInstance().getInteger("seven"), is(equalTo(14)));

        // Test that the parser correctly creates a module that is capable of injecting the parsed flags
        Injector injector = Guice.createInjector(module);

        TestInjectionOneClass oneClass = injector.getInstance(TestInjectionOneClass.class);
        assertThat(oneClass.value, is(equalTo("valueOne")));
        TestInjectionTwoClass twoClass = injector.getInstance(TestInjectionTwoClass.class);
        assertThat(twoClass.value, is(equalTo(17)));

        TestInjectionSixClass sixClass = injector.getInstance(TestInjectionSixClass.class);
        assertThat(sixClass.value, is(equalTo("valueSix")));
        TestInjectionSevenClass sevenClass = injector.getInstance(TestInjectionSevenClass.class);
        assertThat(sevenClass.value, is(equalTo(14)));

        boolean threwError = false;
        try {
            injector.getInstance(TestInjectionThreeClass.class);
        }
        catch (ConfigurationException e) {
            threwError = true;
        }

        if(!threwError) {
            throw new Exception("No error was thrown when injecting the wrong type");
        }
    }
}
