package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.annotation.Nullable;

public interface TemplateGlobal<T> {

    String key();

    T get(OrchidContext context, @Nullable OrchidPage orchidPage);

}
