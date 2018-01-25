package com.eden.orchid.impl.themes.templateFunctions;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class LoadFunction extends TemplateFunction {

    private final OrchidContext context;
    private final StringConverter converter;

    @Option
    @Getter @Setter
    private String resource;

    @Option @BooleanDefault(true)
    @Getter @Setter
    private boolean localResourcesOnly;

    @Inject
    public LoadFunction(OrchidContext context, StringConverter converter) {
        super("load");
        this.context = context;
        this.converter = converter;
    }

    @Override
    public String[] parameters() {
        return new String[] {"resource", "localResourcesOnly"};
    }

    @Override
    public Object apply(Object input) {
        String resourceName = converter.convert((input != null) ? input : resource).second;

        OrchidResource resource = localResourcesOnly ? context.getLocalResourceEntry(resourceName) : context.getResourceEntry(resourceName);

        if(resource != null) {
            return resource.getContent();
        }

        return "";
    }

}