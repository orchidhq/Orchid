package com.eden.orchid.api.compilers;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.google.inject.ImplementedBy;

import java.util.Map;

/**
 * The precompiler is a compiler to be run against files before they are sent to their appropriate OrchidCompiler.
 * Generally, this is used to extract data embedded within the file, returning the extracted data and the content after
 * it has had its embedded data removed so that the resulting content can be compiled with special options specific to
 * that file.
 *
 * @since v1.0.0
 */
@ImplementedBy(FrontMatterPrecompiler.class)
@Archetype(value = ConfigArchetype.class, key = "services.compilers.precompiler")
public abstract class OrchidPrecompiler extends Prioritized implements OptionsHolder {

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
    public abstract boolean shouldPrecompile(String extension, String input);

    /**
     * Extract the data embedded within some given content, returning the data that was extracted as well as the content
     * after the embedded data has been removed.
     *
     * @param input the input content to parse
     * @return a pair representing the content after removal of embedded content, and the content that was embedded
     *
     * @since v1.0.0
     */
    public abstract EdenPair<String, Map<String, Object>> getEmbeddedData(String extension, String input);

}
