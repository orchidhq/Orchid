package com.eden.orchid;

import com.eden.orchid.api.ApiModule;
import com.eden.orchid.api.registration.ClasspathModuleInstaller;
import com.eden.orchid.api.registration.FlagsModule;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.impl.ImplModule;
import com.google.inject.AbstractModule;
import lombok.Builder;

@IgnoreModule
@Builder
public final class StandardModule extends AbstractModule {

    private final String[] args;

    @Builder.Default private boolean includeCoreApi = true;
    @Builder.Default private boolean includeCoreImpl = true;
    @Builder.Default private boolean includeFlags = true;
    @Builder.Default private boolean includeClasspath = true;

    @Override
    protected void configure() {
        if(includeCoreApi) {
            install(new ApiModule());
        }
        if(includeCoreImpl) {
            install(new ImplModule());
        }
        if(includeFlags) {
            if(args == null) {
                throw new IllegalStateException("A mapping of flags must be provided to use the FlagsModule");
            }
            install(new FlagsModule(args));
        }
        if(includeClasspath) {
            install(new ClasspathModuleInstaller());
        }
    }

}
