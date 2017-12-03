package com.eden.orchid.api.compilers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidService;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

/**
 * Handles the conversion of source content from one language or format to another. Specific OrchidCompilers or
 * OrchidParsers can be added seamlessly by injecting into the appropriate set in your Module.
 *
 * @since v1.0.0
 */
public interface CompilerService extends OrchidService {

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
     * Finds an OrchidCompiler capable of evaluating input with a given file extension.
     *
     * @param extension the extension representing content that needs to be compiled
     * @return an appropriate OrchidCompiler if once was found that matches the extension, null otherwise
     *
     * @since v1.0.0
     * @see OrchidCompiler
     */
    default OrchidCompiler compilerFor(String extension) {
        return getService(CompilerService.class).compilerFor(extension);
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
     * @param extension the extension to find a Compiler for
     * @param input the input to compile
     * @return the compiled output data if an appropriate Compiler could be found, otherwise the unprocessed input
     *
     * @since v1.0.0
     * @see OrchidCompiler
     */
    default String compile(String extension, String input) {
        return getService(CompilerService.class).compile(extension, input);
    }

    /**
     * Compiles input against a given Compiler identified by file extension. Additional data may be passed in that
     * individual Compilers may use to render into the resulting output.
     *
     * @param extension the extension to find a Compiler for
     * @param input the input to compile
     * @param data additional data to pass to the OrchidCompiler
     * @return the compiled output data if an appropriate Compiler could be found, otherwise the unprocessed input
     *
     * @since v1.0.0
     * @see OrchidCompiler
     */
    default String compile(String extension, String input, Object data) {
        return getService(CompilerService.class).compile(extension, input, data);
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
    default JSONObject parse(String extension, String input) {
        return getService(CompilerService.class).parse(extension, input);
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
    default EdenPair<String, JSONElement> getEmbeddedData(String input) {
        return getService(CompilerService.class).getEmbeddedData(input);
    }

    /**
     * When processing the input content, it is common to "inject" values into its contents which are made available
     * to the main compiler that can't normally handle such operations. An example would be to render the site version
     * or build timestamp into a plain-text content that does not have an associated OrchidCompiler.
     *
     * @param input the input content to precompile
     * @return the resulting content, which may then be passed to its associated OrchidCompiler
     *
     * @since v1.0.0
     * @see OrchidPrecompiler
     */
    default String precompile(String input) {
        return getService(CompilerService.class).precompile(input);
    }

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
     * @see OrchidPrecompiler
     */
    default String precompile(String input, Object data) {
        return getService(CompilerService.class).precompile(input, data);
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

}
