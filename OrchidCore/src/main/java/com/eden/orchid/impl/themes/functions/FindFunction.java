package com.eden.orchid.impl.themes.functions;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

@Getter @Setter
@Description(value = "Lookup a Page object by a query.", name = "Find")
public final class FindFunction extends TemplateFunction {

    private final OrchidContext context;

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
    public FindFunction(OrchidContext context) {
        super("find", false);
        this.context = context;
    }

    @Override
    public String[] parameters() {
        return IndexService.locateParams;
    }

    @Override
    public Object apply() {
        return context.find(collectionType, collectionId, itemId);
    }

}
