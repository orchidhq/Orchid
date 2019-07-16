package com.eden.orchid.api.options;

import com.eden.orchid.api.options.annotations.FlagAliases;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.testhelpers.OrchidUnitTest;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

public class TestFlags extends OrchidUnitTest {

// Test Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TestFlagsClass extends OrchidFlag {

        @Option
        @FlagAliases("o")
        public String one;

        @Option
        public String two;

        @Option
        public String three;

        @Option
        public int five;

        @Option @StringDefault("valueSix")
        public String six;

        @Option @IntDefault(14)
        public int seven;

    }

// Test Setup
//----------------------------------------------------------------------------------------------------------------------

    private String[] args;
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

    @BeforeEach
    public void setupTest() {
        List<String> args = new ArrayList<>();
        args.addAll(Arrays.asList("-o", "valueOne"));
        args.addAll(Arrays.asList("--two", "valueTwo"));
        args.addAll(Arrays.asList("--three", "valueThree"));
        args.addAll(Arrays.asList("--five", "17"));
        this.args = new String[args.size()];
        args.toArray(this.args);

        flagParsers = new ArrayList<>();
        flagParsers.add(new TestFlagsClass());
    }

// Run Actual Tests
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testParsingFlags() throws Throwable {
        OrchidFlags.instance = new OrchidFlags(flagParsers);

        OrchidModule module = OrchidFlags.getInstance().parseFlags(OrchidUtils.parseCommandLineArgs(args));

        // verify that the options get parsed correctly
        assertThat(OrchidFlags.getInstance().getFlagValue("one"),   is(equalTo("valueOne")));
        assertThat(OrchidFlags.getInstance().getFlagValue("two"),   is(equalTo("valueTwo")));
        assertThat(OrchidFlags.getInstance().getFlagValue("three"), is(equalTo("valueThree")));
        assertThat(OrchidFlags.getInstance().getFlagValue("four"),  isEmptyOrNullString()); // "four" has no parser for it
        assertThat(OrchidFlags.getInstance().getFlagValue("five"),  is(equalTo(17))); // "five"'s parser returned an int
        assertThat(OrchidFlags.getInstance().getFlagValue("six"),   is(equalTo("valueSix")));
        assertThat(OrchidFlags.getInstance().getFlagValue("seven"), is(equalTo(14)));

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

        threwError = false;
        try {
            List<String> args = new ArrayList<>();
            args.addAll(Arrays.asList(this.args));
            args.addAll(Arrays.asList("--four", "valueFour"));
            this.args = new String[args.size()];
            args.toArray(this.args);
            OrchidFlags.getInstance().parseFlags(OrchidUtils.parseCommandLineArgs(this.args));
        }
        catch (IllegalArgumentException e) {
            threwError = true;
        }
        if(!threwError) {
            throw new Exception("No error was thrown when passing a flag that is not registered.");
        }
    }
}
