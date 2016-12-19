package com.eden.orchid;

import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.ContentCompiler;
import com.eden.orchid.compilers.PreCompiler;
import com.eden.orchid.compilers.SiteCompilers;
import com.eden.orchid.options.SiteOptions;
import com.sun.javadoc.RootDoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    void generate(RootDoc rootDoc, Object... data) {
        Path file = Paths.get(SiteOptions.siteOptions.getString("outputDir") + "/index.html");
        try {
            String compiledContent = SiteCompilers.getContentCompiler(getContentCompilerClass()).compile("html", OrchidUtils.getResourceFileContents("assets/layouts/index.html"), Orchid.root);
            Files.write(file, compiledContent.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
