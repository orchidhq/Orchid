package com.eden.orchid.compilers;

/**
 * The main compiler for your theme. This will be used to compile your input templates and generate the resulting HTML
 * pages for your site. Each Theme defines one PreCompiler and one ContentCompiler, along with any number of Compilers,
 * and together they will determine exactly how the Theme generates its output files. A Content compiler generally doesn't
 * care about input or output file extensions, and instead just focuses on rendering data into output content.
 */
public interface ContentCompiler {

    /**
     * Compile the input content according to its file extension.
     *
     * @param extension  the file extension representing the input content
     * @param input  the content to be compiled
     * @param data  optional data to be sent to the compiler
     * @return  the compiled content
     */
    String compile(String extension, String input, Object... data);

    /**
     * The priority of the ContentCompiler. Generally, a theme chooses a single ContentCompiler to be used in all cases,
     * but a Theme may change the implementation and choose a ContentCompiler based on priority.
     *
     * @return  the priority of this ContentCompiler
     */
    int priority();
}