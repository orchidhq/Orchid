package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;

import javax.inject.Inject;

import static com.eden.orchid.utilities.OrchidUtils.DEFAULT_PRIORITY;

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

    @Inject
    public AdminTheme(OrchidContext context, String key) {
        super(context, key, DEFAULT_PRIORITY);
    }

}
