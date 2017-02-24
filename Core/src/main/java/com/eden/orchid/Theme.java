package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.utilities.ObservableTreeSet;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

public abstract class Theme extends DefaultResourceSource {

    private Set<OrchidCompiler> compilers;
    private Set<OrchidParser> parsers;
    private OrchidPrecompiler precompiler;

    @Inject
    public Theme(OrchidContext context, OrchidPrecompiler precompiler, Set<OrchidCompiler> compilers, Set<OrchidParser> parsers) {
        super(context);
        this.precompiler = precompiler;
        this.compilers = new ObservableTreeSet<>(compilers);
        this.parsers = new ObservableTreeSet<>(parsers);
    }

    public boolean shouldContinue() {
        return true;
    }

    public OrchidCompiler compilerFor(String extension) {
        return compilers
                .stream()
                .filter(compiler -> Arrays.stream(compiler.getSourceExtensions()).anyMatch(s -> s.equalsIgnoreCase(extension)))
                .findFirst()
                .orElseGet(() -> null);
    }

    public OrchidParser parserFor(String extension) {
        return parsers
                .stream()
                .filter(parser -> Arrays.stream(parser.getSourceExtensions()).anyMatch(s -> s.equalsIgnoreCase(extension)))
                .findFirst()
                .orElseGet(() -> null);
    }

    public String compile(String extension, String input, Object... data) {
        OrchidCompiler compiler = compilerFor(extension);

        return (compiler != null) ? compiler.compile(extension, input, data) : input;
    }

    public JSONElement parse(String extension, String input) {
        OrchidParser parser = parserFor(extension);

        return (parser != null) ? parser.parse(extension, input) : null;
    }

    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        return precompiler.getEmbeddedData(input);
    }

    public String precompile(String input, Object... data) {
        return precompiler.precompile(input, data);
    }

    public String getOutputExtension(String extension) {
        OrchidCompiler compiler = compilerFor(extension);

        return (compiler != null) ? compiler.getOutputExtension() : extension;
    }
}
