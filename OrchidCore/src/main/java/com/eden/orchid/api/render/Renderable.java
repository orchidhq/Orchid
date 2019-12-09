package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface Renderable {

    String getTemplateBase();

    default List<String> getPossibleTemplates() {
        return new ArrayList<>();
    }

    default OrchidResource resolveTemplate(OrchidContext context, OrchidPage orchidPage) {
        return OrchidUtils.expandTemplateList(context, getPossibleTemplates(), getTemplateBase())
                .map(template -> context.locateTemplate(template, true))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    default String renderContent(OrchidContext context, OrchidPage orchidPage) {
        OrchidResource resource = resolveTemplate(context, orchidPage);
        if(resource != null) {
            return resource.compileContent(this);
        }
        return "";
    }

}
