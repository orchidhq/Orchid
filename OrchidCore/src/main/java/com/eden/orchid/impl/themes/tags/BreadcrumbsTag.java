package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Description;

import javax.inject.Inject;

@Description(value = "Generate the page's breadcrumbs.", name = "Breadcrumbs")
public final class BreadcrumbsTag extends TemplateTag {

    public String key;

    @Inject
    public BreadcrumbsTag() {
        super("breadcrumbs", Type.Simple, true);
    }

    @Override
    public String[] parameters() {
        return new String[] {"key"};
    }

}