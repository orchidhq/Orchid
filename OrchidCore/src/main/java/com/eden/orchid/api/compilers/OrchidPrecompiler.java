package com.eden.orchid.api.compilers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.registration.Prioritized;

/**
 * The precompiler is a compiler to be run against files before they are sent to their appropriate OrchidCompiler.
 * Generally, this is used to extract data embedded within the file, returning the extracted data and the content after
 * it has had its embedded data removed so that the resulting content can be compiled with special options specific to
 * that file.
 *
 * @since v1.0.0
 */
public abstract class OrchidPrecompiler extends Prioritized {

    /**
     * Initialize the OrchidPrecompiler with a set priority. Currently does nothing but is in place to allow for
     * implementation to be chosen at runtime.
     *
     * @param priority priority
     *
     * @since v1.0.0
     */
    public OrchidPrecompiler(int priority) {
        super(priority);
    }

    /**
     * Evaluate a given input String to determine whether it should be precompiled with this OrchidPrecompiler or not.
     *
     * @param input the input to evaluate
     * @return whether this precompiler should continue processing this input
     *
     * @since v1.0.0
     */
    public abstract boolean shouldPrecompile(String input);

    /**
     * Extract the data embedded within some given content, returning the data that was extracted as well as the content
     * after the embedded data has been removed.
     *
     * @param input the input content to parse
     * @return a pair representing the content after removal of embedded content, and the content that was embedded
     *
     * @since v1.0.0
     */
    public abstract EdenPair<String, JSONElement> getEmbeddedData(String input);

    /**
     * When processing the input content, it is common to "inject" values into its contents which are made available
     * to the main compiler that can't normally handle such operations. An example would be to render the site version
     * or build timestamp into a plain-text content that does not have an associated OrchidCompiler.
     *
     * @param input the input content to precompile
     * @param data the data to render into the input content
     * @return the resulting content, which may then be passed to its associated OrchidCompiler
     *
     * @since v1.0.0
     */
    public abstract String precompile(String input, Object... data);

}