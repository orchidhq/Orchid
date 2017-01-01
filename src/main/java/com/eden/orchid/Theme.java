package com.eden.orchid;

import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.ContentCompiler;
import com.eden.orchid.compilers.PreCompiler;
import com.eden.orchid.compilers.SiteCompilers;
import com.eden.orchid.utilities.OrchidPair;
import com.sun.javadoc.RootDoc;

import java.util.Map;

public abstract class Theme {
    /**
     * Get the class of the precompiler required for this theme.
     *
     * @return  the class of the required precompiler
     */
    public abstract Class<? extends PreCompiler> getPrecompilerClass();

    /**
     * Get the class of the content compiler required for this theme.
     *
     * @return  the class of the required content compiler
     */
    public abstract Class<? extends ContentCompiler> getContentCompilerClass();

    /**
     * Get an array of classes defining additional compilers required to use this theme.
     *
     * @return  Get a list of compilers that are required to use this theme
     */
    public abstract Class<? extends Compiler>[] getRequiredCompilers();

    public String[] getMissingOptions() {
        return new String[] {};
    }

    public void generateHomepage(RootDoc rootDoc, Object... data) {
        String finalCompiledContent = OrchidUtils.compileLayout("index.html", Orchid.getRoot().toMap());

        OrchidResources.writeFile("", "index.html", finalCompiledContent);
    }

    public String compile(String extension, String input, Object... data) {
        for(Map.Entry<Integer, Compiler> compiler : SiteCompilers.compilers.entrySet()) {
            if(OrchidUtils.acceptsExtension(extension, compiler.getValue().getSourceExtensions())) {
                return compiler.getValue().compile(extension, input, data);
            }
        }

        return input;
    }

    public String compileContent(String extension, String input, Object... data) {
        for(Map.Entry<Integer, ContentCompiler> compiler : SiteCompilers.contentCompilers.entrySet()) {
            if(compiler.getValue().getClass().equals(getContentCompilerClass())) {
                return compiler.getValue().compile(extension, input, data);
            }
        }

        return input;
    }

    public OrchidPair<String, JSONElement> getEmbeddedData(String input) {
        for(Map.Entry<Integer, PreCompiler> compiler : SiteCompilers.precompilers.entrySet()) {
            if(compiler.getValue().getClass().equals(getPrecompilerClass())) {
                return compiler.getValue().getEmbeddedData(input);
            }
        }

        return new OrchidPair<>(input, null);
    }

    public String getOutputExtension(String extension) {
        for(Map.Entry<Integer, Compiler> compiler : SiteCompilers.compilers.entrySet()) {
            if(OrchidUtils.acceptsExtension(extension, compiler.getValue().getSourceExtensions())) {
                return compiler.getValue().getOutputExtension();
            }
        }

        return null;
    }
}
