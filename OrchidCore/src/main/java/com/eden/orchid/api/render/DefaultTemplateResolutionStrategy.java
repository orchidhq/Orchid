package com.eden.orchid.api.render;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DefaultTemplateResolutionStrategy extends TemplateResolutionStrategy {

    private final Provider<OrchidContext> context;

    @Inject
    public DefaultTemplateResolutionStrategy(Provider<OrchidContext> context) {
        super(100);
        this.context = context;
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
            templateNames.addAll(page.getTemplates());
        }
        templateNames.add(page.getKey());
        templateNames.add("page");

        return expandTemplateList(templateNames, "pages");
    }

    public List<String> getComponentTemplate(OrchidComponent component) {
        List<String> templateNames = new ArrayList<>();
        if(!EdenUtils.isEmpty(component.getTemplates())) {
            templateNames.addAll(component.getTemplates());
        }
        templateNames.add(component.getType());

        return expandTemplateList(templateNames, "components");
    }

    public List<String> expandTemplateList(final List<String> templates, final String templateBase) {
        String themePreferredExtension = context.get().getTheme().getPreferredTemplateExtension();
        String defaultExtension = context.get().getSite().getDefaultTemplateExtension();

        return templates
                .stream()
                .filter(OrchidUtils.not(EdenUtils::isEmpty))
                .distinct()
                .flatMap(template -> Stream.of(
                        "templates/" + template,
                        "templates/" + template + "." + themePreferredExtension,
                        "templates/" + template + "." + defaultExtension,
                        "templates/" + templateBase + "/" + template,
                        "templates/" + templateBase + "/" + template + "." + themePreferredExtension,
                        "templates/" + templateBase + "/" + template + "." + defaultExtension
                        ).distinct()
                )
                .collect(Collectors.toList());
    }
}
