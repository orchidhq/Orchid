package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.ListClass;
import com.eden.orchid.api.options.extractors.DoubleOptionExtractor;
import com.eden.orchid.api.options.extractors.FloatOptionExtractor;
import com.eden.orchid.api.options.extractors.IntOptionExtractor;
import com.eden.orchid.api.options.extractors.LongOptionExtractor;
import com.eden.orchid.api.options.extractors.OptionsHolderOptionExtractor;
import com.eden.orchid.api.options.extractors.StringOptionExtractor;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test(groups={"options", "unit"})
public class TestOptions {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    public static class ParentTestOptionsClass implements OptionsHolder {
        @Option
        private String inaccessibleParentStringOption;

        @Option
        private String accessibleParentStringOption;

        public void setAccessibleParentStringOption(String accessibleParentStringOption) {
            this.accessibleParentStringOption = accessibleParentStringOption;
        }

        public String getInaccessibleParentStringOption() {
            return inaccessibleParentStringOption;
        }

        public String getAccessibleParentStringOption() {
            return accessibleParentStringOption;
        }
    }

    public static class TestOptionsClass extends ParentTestOptionsClass implements OptionsHolder {

        @Option
        public String stringOption;

        @Option
        public int intOption;

        @Option
        public double doubleOption;

        @Option
        public InnerTestOptionsClass innerOption;

        @Option
        @ListClass(InnerTestOptionsClass.class)
        public List<InnerTestOptionsClass> innerOptionArray;

        @Option
        private String setterStringOption;

        @Option("primaryKey")
        public String keyedStringOption;

        @Option("primaryKeySetter")
        public String keyedSetterStringOption;

        @Option
        public String stringNotInJson;

        @Option
        public int intNotInJson;

        @Option
        @IntDefault(10)
        public int defaultIntValue;

        @Option
        public double doubleNotInJson;

        @Option
        public String[] stringArrayOption;

        @Option
        public int[] intArrayOption;

        @Option
        public Integer[] integerArrayOption;

        public void setSetterStringOption(String setterStringOption) {
            this.setterStringOption = "some other value";
        }

        public void setKeyedSetterStringOption(String keyedSetterStringOption) {
            this.keyedSetterStringOption = "the setter that will not get hit";
        }

        public void setPrimaryKeySetter(String keyedSetterStringOption) {
            this.keyedSetterStringOption = "the setter that will get hit";
        }
    }

    public static class InnerTestOptionsClass implements OptionsHolder {

        @Option
        public String innerStringOption;
    }

    private JSONObject options;
    private OptionsExtractor extractor;

    @BeforeMethod
    public void setupTest() {
        options = new JSONObject();

        try {
            String s = "{\n" +
                    "  \"stringOption\": \"string value\",\n" +
                    "  \"intOption\": 10,\n" +
                    "  \"doubleOption\": 10.5,\n" +
                    "  \"innerOption\": {\n" +
                    "    \"innerStringOption\": \"inner string value\"\n" +
                    "  },\n" +
                    "  \"innerOptionArray\": [\n" +
                    "    {\"innerStringOption\": \"inner string value 1\"},\n" +
                    "    {\"innerStringOption\": \"inner string value 2\"},\n" +
                    "    {\"innerStringOption\": \"inner string value 3\"}\n" +
                    "  ],\n" +
                    "  \"setterStringOption\": \"json value\",\n" +
                    "  \"primaryKey\": \"primary key value\",\n" +
                    "  \"primaryKeySetter\": \"primary key setter value\",\n" +
                    "  \"inaccessibleParentStringOption\": \"inaccessible value\",\n" +
                    "  \"accessibleParentStringOption\": \"accessible value\",\n" +
                    "  \"stringArrayOption\": [\"one\", \"two\", \"three\"],\n" +
                    "  \"intArrayOption\": [1, 2, 3],\n" +
                    "  \"integerArrayOption\": [4, 5, 6]\n" +
                    "}\n";

            options = new JSONObject(s);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Set<OptionExtractor> extractors = new HashSet<>();

        extractors.add(new StringOptionExtractor());
        extractors.add(new IntOptionExtractor());
        extractors.add(new LongOptionExtractor());
        extractors.add(new FloatOptionExtractor());
        extractors.add(new DoubleOptionExtractor());
        extractors.add(new OptionsHolderOptionExtractor(() -> extractor));

        extractor = new OptionsExtractor(extractors);
    }

    @Test
    public void testOptionsExtraction() {
        TestOptionsClass testOptionsClass = new TestOptionsClass();

        assertThat(testOptionsClass.stringOption, is(equalTo(null)));
        assertThat(testOptionsClass.intOption, is(equalTo(0)));
        assertThat(testOptionsClass.doubleOption, is(equalTo(0.0)));
        assertThat(testOptionsClass.innerOption, is(equalTo(null)));
        assertThat(testOptionsClass.innerOptionArray, is(equalTo(null)));
        assertThat(testOptionsClass.setterStringOption, is(equalTo(null)));
        assertThat(testOptionsClass.keyedStringOption, is(equalTo(null)));
        assertThat(testOptionsClass.keyedSetterStringOption, is(equalTo(null)));
        assertThat(testOptionsClass.defaultIntValue, is(equalTo(0)));
        assertThat(testOptionsClass.getInaccessibleParentStringOption(), is(equalTo(null)));
        assertThat(testOptionsClass.getAccessibleParentStringOption(), is(equalTo(null)));
        assertThat(testOptionsClass.stringArrayOption, is(equalTo(null)));
        assertThat(testOptionsClass.intArrayOption, is(equalTo(null)));
        assertThat(testOptionsClass.integerArrayOption, is(equalTo(null)));

        extractor.extractOptions(testOptionsClass, options);

        assertThat(testOptionsClass.stringOption, is(equalTo("string value")));
        assertThat(testOptionsClass.intOption, is(equalTo(10)));
        assertThat(testOptionsClass.doubleOption, is(equalTo(10.5)));
        assertThat(testOptionsClass.innerOption, is(not(equalTo(null))));
        assertThat(testOptionsClass.innerOption.innerStringOption, is(equalTo("inner string value")));

        assertThat(testOptionsClass.innerOptionArray, is(not(equalTo(null))));
        for (int i = 0; i < testOptionsClass.innerOptionArray.size(); i++) {
            assertThat(testOptionsClass.innerOptionArray.get(i).innerStringOption, is(equalTo("inner string value " + (i+1))));
        }

        assertThat(testOptionsClass.setterStringOption, is(equalTo("some other value")));
        assertThat(testOptionsClass.keyedStringOption, is(equalTo("primary key value")));
        assertThat(testOptionsClass.keyedSetterStringOption, is(equalTo("the setter that will get hit")));
        assertThat(testOptionsClass.defaultIntValue, is(equalTo(10)));
        assertThat(testOptionsClass.getInaccessibleParentStringOption(), is(equalTo(null)));
        assertThat(testOptionsClass.getAccessibleParentStringOption(), is(equalTo("accessible value")));
        assertThat(testOptionsClass.stringArrayOption, is(equalTo(new String[]{"one", "two", "three"})));
        assertThat(testOptionsClass.intArrayOption, is(equalTo(new int[]{1, 2, 3})));
        assertThat(testOptionsClass.integerArrayOption, is(equalTo(new Integer[]{4, 5, 6})));
    }
}
