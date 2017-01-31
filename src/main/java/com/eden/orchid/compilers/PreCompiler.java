package com.eden.orchid.compilers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;

/**
 * The precompiler is a compiler to be run against files before they are sent to their appropriate Compiler. Generally,
 * this is used to extract data embedded within the file, returning the extracted data and the content after it has had
 * its embedded data removed so that the resulting content can be compiled with special options specific to that file.
 */
public interface PreCompiler {

    /**
     * Extract the data embedded within some given content, returning the data that was extracted as well as the content
     * after the embedded data has been removed.
     *
     * @param input the input content to parse
     * @return a pair representing the content after removal of embedded content, and the content that was embedded
     */
    EdenPair<String, JSONElement> getEmbeddedData(String input);

    /**
     * The priority of the PreCompiler. Generally, a theme chooses a single PreCompiler to be used in all cases, but
     * a Theme may change the implementation and choose a PreCompiler based on priority.
     *
     * @return the priority of this PreCompiler
     */
    int priority();
}