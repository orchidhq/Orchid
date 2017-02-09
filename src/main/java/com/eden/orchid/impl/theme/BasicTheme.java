package com.eden.orchid.impl.theme;

import com.eden.orchid.Theme;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.api.resources.OrchidResources;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class BasicTheme extends Theme {

    @Inject
    public BasicTheme(OrchidResources resources, Set<OrchidCompiler> compilers, Set<OrchidPreCompiler> preCompilers) {
        super(resources, compilers, preCompilers);
    }
}
