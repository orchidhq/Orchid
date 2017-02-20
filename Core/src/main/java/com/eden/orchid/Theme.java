package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.utilities.ObservableTreeSet;

import javax.inject.Inject;
import java.util.Set;

public abstract class Theme extends DefaultResourceSource {

    private Set<OrchidCompiler> compilers;
    private OrchidPreCompiler preCompiler;

    @Inject
    public Theme(OrchidPreCompiler preCompiler, Set<OrchidCompiler> compilers) {
        this.preCompiler = preCompiler;
        this.compilers = new ObservableTreeSet<>(compilers);
    }

    public boolean shouldContinue() {
        return true;
    }

    /**
     * A callback fired on the selected theme when it is first set. By this time, Orchid has registered all components
     * and parsed all Options, but has not yet started a OrchidTask.
     */
    public void onThemeSet() {

    }

    public OrchidCompiler compilerFor(String extension) {
        for (OrchidCompiler compiler : compilers) {
            for (String ext : compiler.getSourceExtensions()) {
                if (ext.equalsIgnoreCase(extension)) {
                    return compiler;
                }
            }
        }

        return null;
    }

    public String compile(String extension, String input, Object... data) {
        OrchidCompiler compiler = compilerFor(extension);

        return (compiler != null) ? compiler.compile(extension, input, data) : input;
    }

    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        return preCompiler.getEmbeddedData(input);
    }

    public String getOutputExtension(String extension) {
        OrchidCompiler compiler = compilerFor(extension);

        return (compiler != null) ? compiler.getOutputExtension() : extension;
    }
}
