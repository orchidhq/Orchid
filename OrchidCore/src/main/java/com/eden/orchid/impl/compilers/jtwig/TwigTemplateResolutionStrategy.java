package com.eden.orchid.impl.compilers.jtwig;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwigTemplateResolutionStrategy extends TemplateResolutionStrategy {

    @Override
    public List<String> getPageTemplate(OrchidPage page, String... templates) {
        return Arrays
                .stream((!EdenUtils.isEmpty(templates)) ? templates : new String[]{page.getType(), "page"})
                .filter(OrchidUtils.not(EdenUtils::isEmpty))
                .distinct()
                .flatMap(template -> Stream.of(
                        template,
                        "templates/" + template + "-" + page.getType() + ".twig",
                        "templates/" + template + ".twig",
                        "templates/pages/" + template + "-" + page.getType() + ".twig",
                        "templates/pages/" + template + ".twig")
                )
                .collect(Collectors.toList());
    }

    public List<String> getComponentTemplate(OrchidComponent component, String... templates) {
        return Arrays
                .stream((!EdenUtils.isEmpty(templates)) ? templates : new String[]{component.getAlias()})
                .filter(OrchidUtils.not(EdenUtils::isEmpty))
                .distinct()
                .flatMap(template -> Stream.of(
                        template,
                        template + ".twig",
                        "components/" + template + ".twig")
                )
                .collect(Collectors.toList());
    }
}
