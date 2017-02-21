package com.eden.orchid.editorial;

import com.eden.orchid.Theme;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;

import javax.inject.Inject;
import java.util.Set;

public class EditorialTheme extends Theme {

    @Inject
    public EditorialTheme(OrchidContext context, OrchidPreCompiler preCompiler, Set<OrchidCompiler> compilers) {
        super(context, preCompiler, compilers);
    }
}
