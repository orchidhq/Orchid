package com.eden.orchid.compilers;

/**
 * A generic compiler which can be used by a Theme to transform content. When a Theme is requested to compile a file of
 * a given type, it searches the list of registered Compilers and picks the one with the highest priority that is able
 * to compile the given file type.
 */
public interface Compiler {

    /**
     * Compile content with a particular file extension using the optional provided data.
     *
     * @param extension the file extension that represents the type of data to compile
     * @param input     the content to be compiled
     * @param data      optional data to be passed to the compiler
     * @return the compiled content
     */
    String compile(String extension, String input, Object... data);

    /**
     * Gets the file extension representing the type of the output content.
     *
     * @return the output file extension
     */
    String getOutputExtension();

    /**
     * Get the list of file extensions this Compiler is able to process.
     *
     * @return the file extensions this Compiler can process
     */
    String[] getSourceExtensions();

    /**
     * The priority of the compiler. The highest-priority Compiler for a given source extension will be chosen to
     * compile content.
     *
     * @return the priority of the compiler
     */
    int priority();
}
