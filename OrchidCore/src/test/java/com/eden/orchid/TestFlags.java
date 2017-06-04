package com.eden.orchid;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.OrchidFlags;
import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TestFlags {

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    private Map<String, String[]> flagsMap;
    private List<OrchidFlag> flagParsers;

    @Before
    public void setupTest() {
        flagsMap = new HashMap<>();
        flagsMap.put("-one",   new String[] {"-one",   "valueOne"});
        flagsMap.put("-two",   new String[] {"-two",   "valueTwo"});
        flagsMap.put("-three", new String[] {"-three", "valueThree"});
        flagsMap.put("-four",  new String[] {"-four",  "valueFour"});
        flagsMap.put("-five",  new String[] {"-five",  "17"});

        flagParsers = new ArrayList<>();
        flagParsers.add(new OrchidFlag() {
            @Override
            public String getFlag() {
                return "one";
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        flagParsers.add(new OrchidFlag() {
            @Override
            public String getFlag() {
                return "two";
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        flagParsers.add(new OrchidFlag() {
            @Override
            public String getFlag() {
                return "three";
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        flagParsers.add(new OrchidFlag() {
            @Override
            public String getFlag() {
                return "five";
            }

            @Override
            public String getDescription() {
                return null;
            }

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

// Run Actual Tests
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testParsingFlags() throws Throwable {
        AbstractModule module = OrchidFlags.getInstance(flagParsers).parseFlags(flagsMap);

        // verify that the options get parsed correctly
        assertThat(OrchidFlags.getInstance().getString("one"),   is(equalTo("valueOne")));
        assertThat(OrchidFlags.getInstance().getString("two"),   is(equalTo("valueTwo")));
        assertThat(OrchidFlags.getInstance().getString("three"), is(equalTo("valueThree")));
        assertThat(OrchidFlags.getInstance().getString("four"),  isEmptyOrNullString()); // "four" has no parser for it
        assertThat(OrchidFlags.getInstance().getInteger("five"), is(equalTo(17))); // "five"'s parser returned an int

        // Test that the parser correctly creates a module that is capable of injecting the parsed flags
        Injector injector = Guice.createInjector(module);

        TestInjectionOneClass oneClass = injector.getInstance(TestInjectionOneClass.class);
        assertThat(oneClass.value, is(equalTo("valueOne")));
        TestInjectionTwoClass twoClass = injector.getInstance(TestInjectionTwoClass.class);
        assertThat(twoClass.value, is(equalTo(17)));

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
