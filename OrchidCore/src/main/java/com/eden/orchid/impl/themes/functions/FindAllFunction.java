package com.eden.orchid.impl.themes.functions;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Description(value = "Get all Page objects matching a query.", name = "Find all")
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

    @Option("page") @IntDefault(0)
    @Description("Paginate results starting at this page.")
    public int pageIndex;

    @Option @IntDefault(0)
    @Description("Paginate results using this as a page size")
    private int pageSize;

    @Inject
    public FindAllFunction(OrchidContext context) {
        super("findAll", false);
        this.context = context;
    }

    @Override
    public String[] parameters() {
        List<String> params = new ArrayList<>(Arrays.asList(IndexService.locateParams));
        params.add("page");
        params.add("pageSize");
        return params.toArray(new String[0]);
    }

    @Override
    public Object apply() {
        if(pageIndex > 0 && pageSize > 0) {
            return context.findAll(collectionType, collectionId, itemId, pageIndex, pageSize);
        }
        else {
            return context.findAll(collectionType, collectionId, itemId);
        }
    }

}
