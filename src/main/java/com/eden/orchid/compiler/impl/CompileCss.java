package com.eden.orchid.compiler.impl;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.compiler.AssetCompiler;
import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;

@AutoRegister
public class CompileCss implements AssetCompiler {

    @Override
    public String getKey() {
        return "css";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"scss", "css"};
    }

    @Override
    public String getDestExtension() {
        return "css";
    }

    @Override
    public String getSourceDir() {
        return "assets/css";
    }

    @Override
    public String getDestDir() {
        return "css";
    }

    @Override
    public String compile(String extension, String input) {
        if(extension.equals("scss")) {
            Compiler compiler = new Compiler();
            Options options = new Options();

            try {
                Output output = compiler.compileString(input, options);
                return output.getCss();
            }
            catch (CompilationException e) {
                e.printStackTrace();
                return "";
            }
        }
        else {
            return input;
        }
    }
}
