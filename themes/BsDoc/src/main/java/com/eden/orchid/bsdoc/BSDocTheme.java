package com.eden.orchid.bsdoc;

import com.eden.orchid.Theme;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;

import javax.inject.Inject;
import java.util.Set;

public class BSDocTheme extends Theme {

    @Inject
    public BSDocTheme(OrchidPreCompiler preCompiler, Set<OrchidCompiler> compilers) {
        super(preCompiler, compilers);
    }
}
