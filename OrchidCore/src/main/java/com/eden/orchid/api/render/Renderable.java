package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface Renderable {

    String getTemplateBase();

    OrchidContext getContext();

    default List<String> getPossibleTemplates() {
        return new ArrayList<>();
    }

    default OrchidResource resolveTemplate() {
        return OrchidUtils.expandTemplateList(getContext(), getPossibleTemplates(), getTemplateBase())
                .map(template -> getContext().locateTemplate(template, true))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    default String renderContent() {
        OrchidResource resource = resolveTemplate();
        if(resource != null) {
            return resource.compileContent(this);
        }
        return "";
    }

}
