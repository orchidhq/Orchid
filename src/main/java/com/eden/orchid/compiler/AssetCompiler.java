package com.eden.orchid.compiler;

public interface AssetCompiler {

    String getKey();

    String[] getSourceExtensions();
    String getSourceDir();

    String getDestExtension();
    String getDestDir();

    String compile(String extension, String input);
}