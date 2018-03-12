package com.eden.orchid.impl.relations;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Relation;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

@Getter @Setter
public class PageRelation extends Relation<OrchidPage> {

    @Option
    @Description("The Id of an item to look up.")
    private String itemId;

    @Option
    @Description("The type of collection the item is expected to come from.")
    private String collectionType;

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    private String collectionId;

    @Inject
    public PageRelation(OrchidContext context) {
        super(context);
    }

    @Override
    public OrchidPage load() {
        return context.findPage(collectionType, collectionId, itemId);
    }

}
