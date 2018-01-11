package com.eden.orchid.api.options.globals;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.TemplateGlobal;
import com.eden.orchid.api.site.OrchidSite;

public class SiteGlobal implements TemplateGlobal<OrchidSite> {

    @Override
    public String key() {
        return "site";
    }

    @Override
    public OrchidSite get(OrchidContext context) {
        return context.getSite();
    }
}
