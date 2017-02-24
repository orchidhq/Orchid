package com.eden.orchid.bsdoc;

import com.eden.orchid.Theme;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;

import javax.inject.Inject;
import java.util.Set;

public class BSDocTheme extends Theme {

    @Inject
    public BSDocTheme(OrchidContext context, OrchidPrecompiler precompiler, Set<OrchidCompiler> compilers, Set<OrchidParser> parsers) {
        super(context, precompiler, compilers, parsers);
    }
}
