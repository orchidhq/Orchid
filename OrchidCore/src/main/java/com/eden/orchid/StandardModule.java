package com.eden.orchid;

import com.eden.orchid.api.ApiModule;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.impl.ImplModule;
import com.google.inject.AbstractModule;
import lombok.Builder;

import java.util.Map;

@IgnoreModule
@Builder
public class StandardModule extends AbstractModule {

    private final Map<String, String[]> flags;

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
            if(flags == null) {
                throw new IllegalStateException("A mapping of flags must be provided to use the FlagsModule");
            }
            install(new FlagsModule(flags));
        }
        if(includeClasspath) {
            install(new ClasspathModuleInstaller());
        }
    }

}
