package com.eden.orchid.impl.themes.functions;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Getter @Setter
public final class LinkFunction extends TemplateFunction {

    private final OrchidContext context;

    @Option
    @Description("The Id of an item to link to.")
    private String itemId;

    @Option
    @Description("The type of collection the item is expected to come from.")
    private String collectionType;

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    private String collectionId;

    @Inject
    public LinkFunction(OrchidContext context) {
        super("link", true);
        this.context = context;
    }

    @Override
    public String[] parameters() {
        return new String[] {"itemId", "collectionId", "collectionType"};
    }

    @Override
    public Object apply(Object input) {
        Object page;

        if(!EdenUtils.isEmpty(collectionId) && !EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            page = context.findInCollection(collectionType, collectionId, itemId);
        }
        else if(!EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            page = context.findInCollection(collectionType, itemId);
        }
        else if(!EdenUtils.isEmpty(itemId)) {
            page = context.findInCollection(itemId);
        }
        else {
            page = null;
        }

        if(page != null && page instanceof OrchidPage) {
            return ((OrchidPage) page).getLink();
        }

        return "";
    }

}
