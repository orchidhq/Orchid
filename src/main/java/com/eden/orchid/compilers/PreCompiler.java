package com.eden.orchid.compilers;

import com.eden.orchid.JSONElement;
import com.eden.orchid.utilities.OrchidPair;

/**
 * The precompiler is a compiler to be run against files before they are processed by their main compiler. Typically
 * this would be for the purpose of gathering metadata specific to that file, or injecting simple data gathered from
 * site options. The only data that is guaranteed to exist by the time a precompiler is run is that which has been
 * gathered from the SiteOptions scan, so data that has been gathered from a Generator should not be used in the
 * pre-compilation stage. A precompiler is optional, and in the case that no precompiler has been defined, a default
 * precompiler which simple passes through data will be used.
 */
public interface PreCompiler {
    String compile(String input, Object... data);
    OrchidPair<String, JSONElement> getEmbeddedData(String input);
    int priority();
}