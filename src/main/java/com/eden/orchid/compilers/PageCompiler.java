package com.eden.orchid.compilers;

public interface PageCompiler extends Compiler {
    String compile(String extension, String input, Object... data);
    String getOutputExtension();
    String[] getSourceExtensions();
    int priority();
}