package com.eden.orchid.api.compilers;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles the conversion of source content from one language or format to another. Specific OrchidCompilers or
 * OrchidParsers can be added seamlessly by injecting into the appropriate set in your Module.
 *
 * @since v1.0.0
 * @orchidApi services
 */
@ImplementedBy(CompilerServiceImpl.class)
public interface CompilerService extends OrchidService {

    /**
     * Get the file extension that is used by default by the Precompiler.
     *
     * @return the file extensions used by by the precompiler by default
     *
     * @since v1.0.0
     * @see OrchidCompiler
     */
    default String getDefaultPrecompilerExtension() {
        return getService(CompilerService.class).getDefaultPrecompilerExtension();
    }

    /**
     * Get the file extensions that can be converted using the registered OrchidCompilers.
     *
     * @return the file extensions that can be processed
     *
     * @since v1.0.0
     * @see OrchidCompiler
     */
    default Set<String> getCompilerExtensions() {
        return getService(CompilerService.class).getCompilerExtensions();
    }

    /**
     * For a given input extension, determine the language it will become after it has been compiled
     *
     * @param extension the input file extension
     * @return the file extension the input will become after being compiled against this input extension
     *
     * @since v1.0.0
     */
    default String getOutputExtension(String extension) {
        return getService(CompilerService.class).getOutputExtension(extension);
    }

    /**
     * Get the file extensions that can be converted using the registered OrchidParsers.
     *
     * @return the file extensions that can be processed
     *
     * @since v1.0.0
     * @see OrchidParser
     */
    default Set<String> getParserExtensions() {
        return getService(CompilerService.class).getParserExtensions();
    }

    /**
     * Finds an OrchidParser capable of evaluating input with a given file extension.
     *
     * @param extension the extension representing content that needs to be parsed
     * @return an appropriate OrchidParser if once was found that matches the extension, null otherwise
     *
     * @since v1.0.0
     * @see OrchidParser
     */
    default OrchidParser parserFor(String extension) {
        return getService(CompilerService.class).parserFor(extension);
    }

    /**
     * Compiles input against a given Compiler identified by file extension. Additional data may be passed in that
     * individual Compilers may use to render into the resulting output.
     *
     * @param resource the source resource where the content is coming from
     * @param extension the extension to find a Compiler for
     * @param input the input to compile
     * @param data additional data to pass to the OrchidCompiler
     * @return the compiled output data if an appropriate Compiler could be found, otherwise the unprocessed input
     *
     * @since v1.0.0
     * @see OrchidCompiler
     */
    default String compile(@Nullable OrchidResource resource, String extension, String input, Object data) {
        return getService(CompilerService.class).compile(resource, extension, input, data);
    }

    /**
     * Compiles input against a given Parser identified by file extension.
     *
     * @param extension the extension to find a Parser for
     * @param input the input to parse
     * @return the data represented by the input if an appropriate Parser could be found, otherwise an empty JSONObject
     *
     * @since v1.0.0
     * @see OrchidParser
     */
    default Map<String, Object> parse(String extension, String input) {
        return getService(CompilerService.class).parse(extension, input);
    }

    /**
     * Compiles input against a given Parser identified by file extension.
     *
     * @param extension the extension to find a Parser for
     * @param input the input to parse
     * @return the data represented by the input if an appropriate Parser could be found, otherwise an empty JSONObject
     *
     * @since v1.0.0
     * @see OrchidParser
     */
    default String serialize(String extension, Object input) {
        return getService(CompilerService.class).serialize(extension, input);
    }

    /**
     * Extract the data embedded within some given content, returning the data that was extracted as well as the content
     * after the embedded data has been removed.
     *
     * @param input the input content to parse
     * @return a pair representing the content after removal of embedded content, and the content that was embedded
     *
     * @since v1.0.0
     * @see OrchidPrecompiler
     */
    default EdenPair<String, Map<String, Object>> getEmbeddedData(String extension, String input) {
        return getService(CompilerService.class).getEmbeddedData(extension, input);
    }

    /**
     * Many file types need to be processed and rendered as a binary stream rather than being decoded into a String.
     * This method returns a List of the current known binary file extensions, so it can be determined how to process
     * a given file type.
     *
     * @return the current known binary file extensions
     *
     * @since v1.0.0
     */
    default List<String> getBinaryExtensions() {
        return getService(CompilerService.class).getBinaryExtensions();
    }

    /**
     * Determines if a given file extension is known to require processing as a binary stream rather than an encoded
     * String.
     *
     * @return true if the extension represents a binary file type, false otherwise
     *
     * @since v1.0.0
     */
    default boolean isBinaryExtension(String extension) {
        return getService(CompilerService.class).isBinaryExtension(extension);
    }

    /**
     * Normally, a filename like `index.php.peb` would be read as being compiled with the `peb` extension, but instead
     * of rendering to the normal `html` output extension, use `php`. However, for a filename file `index.min.js`, the
     * `min` should be ignored from this process and kept in the filename, as it is a common convention to use `min.js`
     * to denote a compiled/minified Javascript asset.
     *
     * This method returns a List of the current known ignored output extensions
     *
     * @return the current known ignored output file extensions
     *
     * @since v1.0.0
     */
    default List<String> getIgnoredOutputExtensions() {
        return getService(CompilerService.class).getIgnoredOutputExtensions();
    }

    /**
     * Determines if a given file extension should be ignored when considering 'intermediate' dots in a filename.
     *
     * @return true if the extension represents an ignored output file type, false otherwise
     *
     * @since v1.0.0
     */
    default boolean isIgnoredOutputExtension(String extension) {
        return getService(CompilerService.class).isIgnoredOutputExtension(extension);
    }

}
