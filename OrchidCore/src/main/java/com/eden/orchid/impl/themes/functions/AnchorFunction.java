package com.eden.orchid.impl.themes.functions;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Getter @Setter
public final class AnchorFunction extends TemplateFunction {

    private final OrchidContext context;

    @Option @StringDefault("")
    private String title;

    @Option @StringDefault("")
    private String collectionType;

    @Option @StringDefault("")
    private String collectionId;

    @Option @StringDefault("")
    private String itemId;

    @Option @StringDefault("")
    private String customClasses;

    @Inject
    public AnchorFunction(OrchidContext context) {
        super("anchor");
        this.context = context;
    }

    @Override
    public String[] parameters() {
        return new String[] {"title", "itemId", "collectionId", "collectionType", "customClasses"};
    }

    @Override
    public Object apply(Object input) {
        Object page;

        if(!EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(collectionId) && !EdenUtils.isEmpty(itemId)) {
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
            String link = ((OrchidPage) page).getLink();

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
