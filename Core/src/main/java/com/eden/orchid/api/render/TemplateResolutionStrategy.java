package com.eden.orchid.api.render;

import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.resources.OrchidPage;

import java.util.List;

public abstract class TemplateResolutionStrategy extends Prioritized {

    public abstract List<String> getPageTemplate(OrchidPage page, String... templates);

}
