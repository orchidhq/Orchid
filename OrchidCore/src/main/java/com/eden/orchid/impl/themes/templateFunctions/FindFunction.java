package com.eden.orchid.impl.themes.templateFunctions;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
@Getter @Setter
public final class FindFunction extends TemplateFunction {

    private final OrchidContext context;

    @Option @StringDefault("")
    private String itemId;

    @Option @StringDefault("")
    private String collectionType;

    @Option @StringDefault("")
    private String collectionId;

    @Inject
    public FindFunction(OrchidContext context) {
        super("find");
        this.context = context;
    }

    @Override
    public String[] parameters() {
        return new String[] {"itemId", "collectionType", "collectionId"};
    }

    @Override
    public Object apply(Object input) {
        if(!EdenUtils.isEmpty(collectionId) && !EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            return context.findAllInCollection(collectionType, collectionId, itemId);
        }
        else if(!EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            return context.findAllInCollection(collectionType, itemId);
        }
        else if(!EdenUtils.isEmpty(itemId)) {
            return context.findAllInCollection(itemId);
        }
        else {
            return new ArrayList<>();
        }
    }

}
