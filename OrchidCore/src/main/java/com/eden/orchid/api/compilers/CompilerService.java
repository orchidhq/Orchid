package com.eden.orchid.api.compilers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import org.json.JSONObject;

import java.util.Set;

public interface CompilerService {

    CompilerService getCompilerService();

    default Set<String> getParserExtensions() {
        return getCompilerService().getParserExtensions();
    }

    default OrchidCompiler compilerFor(String extension) {
        return getCompilerService().compilerFor(extension);
    }

    default OrchidParser parserFor(String extension) {
        return getCompilerService().parserFor(extension);
    }

    default String compile(String extension, String input, Object... data) {
        return getCompilerService().compile(extension, input, data);
    }

    default JSONObject parse(String extension, String input) {
        return getCompilerService().parse(extension, input);
    }

    default EdenPair<String, JSONElement> getEmbeddedData(String input) {
        return getCompilerService().getEmbeddedData(input);
    }

    default String precompile(String input, Object... data) {
        return getCompilerService().precompile(input, data);
    }

    default String getOutputExtension(String extension) {
        return getCompilerService().getOutputExtension(extension);
    }

}
