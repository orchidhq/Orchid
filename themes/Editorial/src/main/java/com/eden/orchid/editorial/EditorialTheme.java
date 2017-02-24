package com.eden.orchid.editorial;

import com.eden.orchid.Theme;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;

import javax.inject.Inject;
import java.util.Set;

public class EditorialTheme extends Theme {

    @Inject
    public EditorialTheme(OrchidContext context, OrchidPrecompiler precompiler, Set<OrchidCompiler> compilers, Set<OrchidParser> parsers) {
        super(context, precompiler, compilers, parsers);
    }
}
