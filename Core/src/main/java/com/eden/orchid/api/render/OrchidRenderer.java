package com.eden.orchid.api.render;

import com.eden.orchid.api.resources.OrchidPage;

public interface OrchidRenderer {
    boolean renderTemplate(OrchidPage page, String template);
    boolean renderString(OrchidPage page, String extension, String content);
    boolean renderRaw(OrchidPage page);
}
