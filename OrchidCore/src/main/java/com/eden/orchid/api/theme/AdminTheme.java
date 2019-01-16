package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;

import javax.inject.Inject;

/**
 * @since v1.0.0
 * @orchidApi extensible
 */
@Description(value = "A collection of assets and templates used to render your admin panel.", name = "Admin Themes")
public abstract class AdminTheme extends AbstractTheme {

    @Inject
    public AdminTheme(OrchidContext context, String key, int priority) {
        super(context, key, priority);
    }

}
