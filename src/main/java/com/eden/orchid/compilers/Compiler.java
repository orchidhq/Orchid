package com.eden.orchid.compilers;

public interface Compiler {
    String compile(String extension, String input, Object... data);
    String getOutputExtension();
    String[] getSourceExtensions();
    int priority();
}
