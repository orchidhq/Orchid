package com.eden.orchid.editorial;

import com.eden.orchid.Theme;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.api.resources.OrchidResources;

import javax.inject.Inject;
import java.util.Set;

public class EditorialTheme extends Theme {

    @Inject
    public EditorialTheme(OrchidResources resources, Set<OrchidCompiler> compilers, Set<OrchidPreCompiler> preCompilers) {
        super(resources, compilers, preCompilers);
    }
}
