package com.eden.orchid.api.options.globals;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.TemplateGlobal;

public class IndexGlobal implements TemplateGlobal<OrchidIndex> {

    @Override
    public String key() {
        return "index";
    }

    @Override
    public OrchidIndex get(OrchidContext context) {
        return context.getIndex();
    }
}
