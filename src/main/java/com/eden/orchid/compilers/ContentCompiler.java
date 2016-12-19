package com.eden.orchid.compilers;

/**
 * The main compiler for your theme. This will be used to compile your input templates and generate the resulting HTML
 * pages for your site. Each Orchid implementation must define a content compiler, and only the registered
 * ContentCompiler with the highest priority will be used.
 */
public interface ContentCompiler {
    String compile(String extension, String input, Object... data);
    int priority();
}