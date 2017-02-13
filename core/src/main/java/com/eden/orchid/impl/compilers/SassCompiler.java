package com.eden.orchid.impl.compilers;

import com.eden.orchid.api.compilers.OrchidCompiler;
import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;

import javax.inject.Inject;

public class SassCompiler extends OrchidCompiler {

    @Inject
    public SassCompiler() {
        this.priority = 800;
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"scss", "sass"};
    }

    @Override
    public String getOutputExtension() {
        return "css";
    }

    @Override
    public String compile(String extension, String input, Object... data) {
        if(extension.equals("scss")) {
            try {
                return new Compiler().compileString(input, new Options()).getCss();
            }
            catch (CompilationException e) {
                e.printStackTrace();
                return "";
            }
        }
        else if(extension.equals("sass")) {
            try {
                Options options = new Options();
                options.setIsIndentedSyntaxSrc(true);
                return new Compiler().compileString(input, options).getCss();
            }
            catch (CompilationException e) {
                e.printStackTrace();
                return "";
            }
        }

        return input;
    }
}
