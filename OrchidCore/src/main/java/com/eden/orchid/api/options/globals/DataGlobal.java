package com.eden.orchid.api.options.globals;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.TemplateGlobal;

import java.util.Map;

public class DataGlobal implements TemplateGlobal<Map<String, Object>> {

    @Override
    public String key() {
        return "data";
    }

    @Override
    public Map<String, Object> get(OrchidContext context) {
        return context.getData().toMap();
    }
}
