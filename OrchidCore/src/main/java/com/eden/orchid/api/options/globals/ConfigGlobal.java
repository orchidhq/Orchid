package com.eden.orchid.api.options.globals;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.TemplateGlobal;

import java.util.Map;

public class ConfigGlobal implements TemplateGlobal<Map<String, Object>> {

    @Override
    public String key() {
        return "config";
    }

    @Override
    public Map<String, Object> get(OrchidContext context) {
        return context.getConfig();
    }
}
