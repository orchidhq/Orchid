package com.eden.orchid.compiler.impl;

import com.eden.orchid.compiler.AssetCompiler;

public class CompileJs implements AssetCompiler {

    @Override
    public String getKey() {
        return "js";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"js"};
    }

    @Override
    public String getDestExtension() {
        return "js";
    }

    @Override
    public String getSourceDir() {
        return "assets/js";
    }

    @Override
    public String getDestDir() {
        return "js";
    }

    @Override
    public String compile(String extension, String input) {
        return input;
    }
}
