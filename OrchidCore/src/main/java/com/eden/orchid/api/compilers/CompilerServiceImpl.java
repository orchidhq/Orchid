package com.eden.orchid.api.compilers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.ObservableTreeSet;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter @Setter
@Singleton
public final class CompilerServiceImpl implements CompilerService {

    private OrchidContext context;

    private Set<OrchidCompiler> compilers;
    private Set<OrchidParser> parsers;
    private OrchidPrecompiler precompiler;

    private Set<String> parserExtensions;

    private Map<String, OrchidCompiler> compilerMap;
    private Map<String, OrchidParser> parserMap;

    @Inject
    public CompilerServiceImpl(Set<OrchidCompiler> compilers, Set<OrchidParser> parsers, OrchidPrecompiler precompiler) {
        this.precompiler = precompiler;
        this.compilers = new ObservableTreeSet<>(compilers);
        this.parsers = new ObservableTreeSet<>(parsers);
        buildCompilerIndex();
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    private void buildCompilerIndex() {
        this.parserExtensions = new HashSet<>();
        this.compilerMap = new HashMap<>();
        this.parserMap = new HashMap<>();

        for (OrchidParser parser : this.parsers) {
            if (!EdenUtils.isEmpty(parser.getSourceExtensions())) {
                for (String ext : parser.getSourceExtensions()) {
                    parserExtensions.add(ext);
                    parserMap.put(ext, parser);
                }
            }
        }
        for (OrchidCompiler compiler : this.compilers) {
            if (!EdenUtils.isEmpty(compiler.getSourceExtensions())) {
                for (String ext : compiler.getSourceExtensions()) {
                    compilerMap.put(ext, compiler);
                }
            }
        }
    }

    public Set<String> getParserExtensions() {
        return parserExtensions;
    }

    public OrchidCompiler compilerFor(String extension) {
        return compilerMap.getOrDefault(extension, null);
    }

    public OrchidParser parserFor(String extension) {
        return parserMap.getOrDefault(extension, null);
    }

    public String compile(String extension, String input, Object... data) {
        OrchidCompiler compiler = compilerFor(extension);
        return (compiler != null) ? compiler.compile(extension, input, data) : input;
    }

    public JSONObject parse(String extension, String input) {
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
