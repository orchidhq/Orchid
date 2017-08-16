package com.eden.orchid.api.compilers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidService;
import org.json.JSONObject;

import java.util.Set;

public interface CompilerService extends OrchidService {

    default Set<String> getParserExtensions() {
        return getService(CompilerService.class).getParserExtensions();
    }

    default OrchidCompiler compilerFor(String extension) {
        return getService(CompilerService.class).compilerFor(extension);
    }

    default OrchidParser parserFor(String extension) {
        return getService(CompilerService.class).parserFor(extension);
    }

    default String compile(String extension, String input, Object... data) {
        return getService(CompilerService.class).compile(extension, input, data);
    }

    default JSONObject parse(String extension, String input) {
        return getService(CompilerService.class).parse(extension, input);
    }

    default EdenPair<String, JSONElement> getEmbeddedData(String input) {
        return getService(CompilerService.class).getEmbeddedData(input);
    }

    default String precompile(String input, Object... data) {
        return getService(CompilerService.class).precompile(input, data);
    }

    default String getOutputExtension(String extension) {
        return getService(CompilerService.class).getOutputExtension(extension);
    }

}
