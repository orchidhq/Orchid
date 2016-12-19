package com.eden.orchid.compilers;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.OrchidUtils;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SiteCompilers {

    // Generic compilers to be dynamically used based on file extension
    public static Map<Integer, Compiler> compilers = new TreeMap<>(Collections.reverseOrder());
    public static Map<Integer, ContentCompiler> contentCompilers = new TreeMap<>(Collections.reverseOrder());
    public static Map<Integer, PreCompiler> precompilers = new TreeMap<>(Collections.reverseOrder());

    /**
     * Gets a compiler instance by its class. Compilers are ordered by priority and will return the compiler with the
     * highest priority.
     *
     * @param compilerClass  the class of the compiler to find
     * @return  the compiler if it was found, null otherwise
     */
    public static Compiler getCompiler(Class<? extends Compiler> compilerClass) {
        for(Map.Entry<Integer, Compiler> compiler : compilers.entrySet()) {

            Clog.d("Getting compiler: #{$1}:[#{$2 | className}]", compiler.getKey(), compiler.getValue());

            if(compiler.getValue().getClass().equals(compilerClass)) {
                return compiler.getValue();
            }
        }

        return null;
    }

    /**
     * Gets a compiler instance by its class. Compilers are ordered by priority and will return the compiler with the
     * highest priority.
     *
     * @param compilerClass  the class of the compiler to find
     * @return  the compiler if it was found, null otherwise
     */
    public static ContentCompiler getContentCompiler(Class<? extends ContentCompiler> compilerClass) {
        for(Map.Entry<Integer, ContentCompiler> compiler : contentCompilers.entrySet()) {

            Clog.d("Getting content compiler: #{$1}:[#{$2 | className}]", compiler.getKey(), compiler.getValue());

            if(compiler.getValue().getClass().equals(compilerClass)) {
                return compiler.getValue();
            }
        }

        return null;
    }

    /**
     * Gets a compiler instance by its class. Compilers are ordered by priority and will return the compiler with the
     * highest priority.
     *
     * @param compilerClass  the class of the compiler to find
     * @return  the compiler if it was found, null otherwise
     */
    public static PreCompiler getPrecompiler(Class<? extends PreCompiler> compilerClass) {
        for(Map.Entry<Integer, PreCompiler> compiler : precompilers.entrySet()) {

            Clog.d("Getting precompiler: #{$1}:[#{$2 | className}]", compiler.getKey(), compiler.getValue());

            if(compiler.getValue().getClass().equals(compilerClass)) {
                return compiler.getValue();
            }
        }

        return null;
    }

    /**
     * Gets a compiler instance that can compile the target extension. Compilers are ordered by priority and will return
     * the compiler with the highest priority.
     *
     * @param extension  the extension to find a matching compiler
     * @return  the compiler if it was found, null otherwise
     */
    public static Compiler getCompiler(String extension) {
        for(Map.Entry<Integer, Compiler> compiler : compilers.entrySet()) {
            if(OrchidUtils.acceptsExtension(extension, compiler.getValue().getSourceExtensions())) {
                return compiler.getValue();
            }
        }

        return null;
    }

    /**
     * Attempts to find a compiler that can compile the target file extension. If a suitable compiler can be found, it
     * will compile the source using the optional parameters passed in as varargs, and return the compiled string and
     * its resulting file extension.
     *
     * @param extension  the extension to find a matching compiler
     * @param source  the source text to compile
     * @param data  optional data to pass to the compiler
     * @return  the compiled source with its output file extension if a compiler could be found, the uncompiled source text and extension otherwise
     */
    public static Pair<String, String> compile(String extension, String source, Object... data) {
        Compiler compiler = getCompiler(extension);

        return (compiler != null)
                ? new Pair<>(compiler.getOutputExtension(), compiler.compile(extension, source, data))
                : new Pair<>(extension, source);
    }

    /**
     * Attempts to find a compiler that can compile the target file. If a suitable compiler can be found, it
     * will compile the source using the optional parameters passed in as varargs, and return the compiled string and
     * its resulting file extension.
     *
     * @param source  the source file to read and then compile
     * @param data  optional data to pass to the compiler
     * @return  the compiled source with its output file extension if the file exists and the compiler can be found, null otherwise
     */
    public static Pair<String, String> compile(File source, Object... data) {
        if(source != null && source.exists() && source.isFile()) {
            try {

                String sourceContents = IOUtils.toString(new FileInputStream(source), "UTF-8");
                return compile(FilenameUtils.getExtension(source.getName()), sourceContents, data);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
