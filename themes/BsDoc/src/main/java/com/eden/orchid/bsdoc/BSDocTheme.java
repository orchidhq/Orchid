package com.eden.orchid.bsdoc;

import com.eden.orchid.Theme;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;

import javax.inject.Inject;
import java.util.Set;

public class BSDocTheme extends Theme {

    @Inject
    public BSDocTheme(OrchidContext context, OrchidPreCompiler preCompiler, Set<OrchidCompiler> compilers) {
        super(context, preCompiler, compilers);
    }
}
