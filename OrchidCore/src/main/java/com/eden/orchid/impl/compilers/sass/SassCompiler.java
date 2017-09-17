package com.eden.orchid.impl.compilers.sass;

import com.eden.orchid.api.compilers.OrchidCompiler;
import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SassCompiler extends OrchidCompiler {

    private Options options;

    @Inject
    public SassCompiler(SassImporter importer) {
        super(800);

        options = new Options();
        options.getImporters().add(importer);
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
        if (extension.equals("scss")) {
            try {
                options.setIsIndentedSyntaxSrc(false);
                return new Compiler().compileString(input, options).getCss();
            }
            catch (CompilationException e) {
                e.printStackTrace();
                return "";
            }
        }
        else if (extension.equals("sass")) {
            try {
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
