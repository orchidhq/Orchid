package com.eden.orchid.api.compilers;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.resources.resource.OrchidResource;

import javax.annotation.Nullable;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A generic compiler which can be used by a Theme to transform content. When a Theme is requested to compile a file of
 * a given type, it searches the list of registered Compilers and picks the one with the highest priority that is able
 * to compile the given file type.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class OrchidCompiler extends Prioritized implements OptionsHolder {

    /**
     * Initialize the OrchidCompiler with a set priority. Compilers with a higher priority are chosen first to process a
     * given input content when multiple Compilers can process the same input extension.
     *
     * @param priority priority
     *
     * @since v1.0.0
     */
    public OrchidCompiler(int priority) {
        super(priority);
    }

    /**
     * Compile content with a particular file extension using the optional provided data.
     *
     * @param extension the file extension that represents the type of data to compile
     * @param input     the content to be compiled
     * @param data      optional data to be passed to the compiler
     * @return the compiled content
     *
     * @since v1.0.0
     */
    public abstract void compile(OutputStream os, @Nullable OrchidResource resource, String extension, String input, Map<String, Object> data);

    /**
     * Gets the file extension representing the type of the output content.
     *
     * @return the output file extension
     *
     * @since v1.0.0
     */
    public abstract String getOutputExtension();

    /**
     * Get the list of file extensions this OrchidCompiler is able to process.
     *
     * @return the file extensions this OrchidCompiler can process
     *
     * @since v1.0.0
     */
    public abstract String[] getSourceExtensions();

}
