package com.eden.orchid;

import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.ContentCompiler;
import com.eden.orchid.compilers.PreCompiler;
import com.eden.orchid.compilers.SiteCompilers;
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

    public void generate(RootDoc rootDoc, Object... data) {
        Path file = Paths.get(Orchid.query("options.d") + "/index.html");
        try {
            String compiledContent = compile("html", OrchidUtils.getResourceFileContents("assets/layouts/index.html"), Orchid.getRoot());
            Files.write(file, compiledContent.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String compile(String extension, String input, Object... data) {
        return SiteCompilers.getContentCompiler(getContentCompilerClass()).compile(extension, input, data);

    }

    public String preCompile(String input, Object... data) {
        return SiteCompilers.getPrecompiler(getPrecompilerClass()).compile(input, data);
    }
}
