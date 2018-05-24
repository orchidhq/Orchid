package com.eden.orchid.impl.themes.functions;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
@Getter @Setter
public final class AnchorFunction extends TemplateFunction {

    private final OrchidContext context;

    @Option
    @Description("The title to display in an anchor tag for the given item if found. Otherwise, the title is " +
            "returned directly."
    )
    private String title;

    @Option
    @Description("The Id of an item to link to.")
    private String itemId;

    @Option
    @Description("The type of collection the item is expected to come from.")
    private String collectionType;

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    private String collectionId;

    @Option
    @Description("Custom classes to add to the resulting anchor tag. If no matching item is found, these classes are " +
            "not used."
    )
    private String customClasses;

    @Inject
    public AnchorFunction(OrchidContext context) {
        super("anchor", true);
        this.context = context;
    }

    @Override
    public String[] parameters() {
        List<String> params = new ArrayList<>();
        params.add("title");
        Collections.addAll(params, IndexService.locateParams);
        params.add("customClasses");
        String[] paramsArray = new String[params.size()];
        params.toArray(paramsArray);
        return paramsArray;
    }

    @Override
    public Object apply(Object input) {
        if(EdenUtils.isEmpty(itemId) && !EdenUtils.isEmpty(title)) {
            itemId = title;
        }

        OrchidPage page = context.findPage(collectionType, collectionId, itemId);

        if(page != null) {
            String link = page.getLink();

            if(!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(customClasses)) {
                return Clog.format("<a href=\"#{$1}\" class=\"#{$3}\">#{$2}</a>", link, title, customClasses);
            }
            else if(!EdenUtils.isEmpty(title)) {
                return Clog.format("<a href=\"#{$1}\">#{$2}</a>", link, title);
            }
            else {
                return Clog.format("<a href=\"#{$1}\">#{$1}</a>", link);
            }
        }

        if(!EdenUtils.isEmpty(title)) {
            return title;
        }
        else {
            return "";
        }
    }

}
