package com.eden.orchid.impl.render;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateResolutionStrategyImpl extends TemplateResolutionStrategy {

    @Override
    public List<String> getPageTemplate(OrchidPage page, String... templates) {
        return Arrays
                .stream((!EdenUtils.isEmpty(templates)) ? templates : new String[] {page.getType(), "page"})
                .filter(OrchidUtils.not(EdenUtils::isEmpty))
                .distinct()
                .flatMap(template -> {
                    List<String> newTemplates = new ArrayList<>();
                    newTemplates.add(template);
                    newTemplates.add("templates/" + template + "-" + page.getType() + ".twig");
                    newTemplates.add("templates/" + template + ".twig");
                    newTemplates.add("templates/pages/" + template + "-" + page.getType() + ".twig");
                    newTemplates.add("templates/pages/" + template + ".twig");

                    return newTemplates.stream();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getComponentTemplate(OrchidComponent component, String... templates) {
        return Arrays
                .stream((!EdenUtils.isEmpty(templates)) ? templates : new String[] {component.getAlias()})
                .filter(OrchidUtils.not(EdenUtils::isEmpty))
                .distinct()
                .flatMap(template -> {
                    List<String> newTemplates = new ArrayList<>();
                    newTemplates.add(template);
                    newTemplates.add("templates/" + template + "-" + component.getAlias() + ".twig");
                    newTemplates.add("templates/" + template + ".twig");
                    newTemplates.add("templates/components/" + template + "-" + component.getAlias() + ".twig");
                    newTemplates.add("templates/components/" + template + ".twig");

                    return newTemplates.stream();
                })
                .collect(Collectors.toList());
    }
}
