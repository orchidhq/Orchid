package com.eden.orchid.impl.compilers.jtwig;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TwigTemplateResolutionStrategy extends TemplateResolutionStrategy {

    @Inject
    public TwigTemplateResolutionStrategy() {
        super(100);
    }

    @Override
    public List<String> getPageLayout(OrchidPage page) {
        List<String> templateNames = new ArrayList<>();
        if(!EdenUtils.isEmpty(page.getLayout())) {
            templateNames.add(page.getLayout());
        }
        if(page.getGenerator() != null && !EdenUtils.isEmpty(page.getGenerator().getLayout())) {
            templateNames.add(page.getGenerator().getLayout());
        }
        templateNames.add("index");
        return expandTemplateList(templateNames, "layouts");
    }

    @Override
    public List<String> getPageTemplate(OrchidPage page) {
        List<String> templateNames = new ArrayList<>();
        if(!EdenUtils.isEmpty(page.getTemplates())) {
            for(String templateName : page.getTemplates()) {
                templateNames.add(templateName);
            }
        }
        templateNames.add(page.getKey());
        templateNames.add("page");

        return expandTemplateList(templateNames, "pages");
    }

    public List<String> getComponentTemplate(OrchidComponent component) {
        List<String> templateNames = new ArrayList<>();
        if(!EdenUtils.isEmpty(component.getTemplates())) {
            Collections.addAll(templateNames, component.getTemplates());
        }
        templateNames.add(component.getKey());

        return expandTemplateList(templateNames, "components");
    }

    public List<String> expandTemplateList(final List<String> templates, final String templateBase) {
        return templates
                .stream()
                .filter(OrchidUtils.not(EdenUtils::isEmpty))
                .distinct()
                .flatMap(template -> Stream.of(
                        "templates/" + template,
                        "templates/" + template + ".twig",
                        "templates/" + templateBase + "/" + template,
                        "templates/" + templateBase + "/" + template + ".twig")
                )
                .collect(Collectors.toList());
    }
}
