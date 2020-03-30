package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;

public interface TemplateGlobal<T> {

    String key();

    T get(OrchidContext context);

}
