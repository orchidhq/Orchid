package com.eden.orchid.javadoc.tester;

import com.google.common.annotations.Beta;
import lombok.Builder;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;


/**
 * This is the javadoc comment for AnnotatedClass.
 *
 * # Which is kind of a big deal. _because it's markdown, you know_.
 *
 * Sometimes people want to write **awesome** comments in markdown, and now they can!
 */
@AnnotationClass
@Beta
@Builder(buildMethodName = "asdfasdf", builderClassName = "dfghdfghdfghdfg")
public class AnnotatedClass extends AbstractClass<String> implements Runnable, CharSequence, Cloneable, InterfaceClass, Readable {

    public int intField = 10;
    public String stringField = "string";

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    @Override
    public void run() {

    }

    @Override
    public int read(CharBuffer cb) throws IOException {
        return 0;
    }

    public class InnerClass {

    }

    public static class StaticInnerClass {
        public static class DeeplyNestedClass {

        }
    }

    public String doesStuff() {
        return "";
    }

    public Map<Map<String, List<Integer>>, AbstractClass<String>> doesStuffAgain() {
        return null;
    }
}
