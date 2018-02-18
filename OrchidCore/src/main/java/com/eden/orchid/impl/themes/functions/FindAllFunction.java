package com.eden.orchid.impl.themes.functions;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
@Getter @Setter
public final class FindAllFunction extends TemplateFunction {

    private final OrchidContext context;

    @Option
    @Description("The Id of the items to link to.")
    private String itemId;

    @Option
    @Description("The type of collection the items are expected to come from.")
    private String collectionType;

    @Option
    @Description("The specific Id of the given collection type where the items are expected to come from.")
    private String collectionId;

    @Inject
    public FindAllFunction(OrchidContext context) {
        super("findAll", false);
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
