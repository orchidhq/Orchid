package com.eden.orchid;

import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.ContentCompiler;
import com.eden.orchid.compilers.PreCompiler;

public interface Theme {
    /**
     * Get the class of the precompiler required for this theme.
     *
     * @return  the class of the required precompiler
     */
    Class<? extends PreCompiler> getPrecompilerClass();

    /**
     * Get the class of the content compiler required for this theme.
     *
     * @return  the class of the required content compiler
     */
    Class<? extends ContentCompiler> getContentCompilerClass();

    /**
     * Get an array of classes defining additional compilers required to use this theme.
     *
     * @return  Get a list of compilers that are required to use this theme
     */
    Class<? extends Compiler>[] getRequiredCompilers();
}
