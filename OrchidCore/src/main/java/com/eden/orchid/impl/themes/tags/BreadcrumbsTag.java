package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class BreadcrumbsTag extends TemplateTag {

    public String key;

    @Inject
    public BreadcrumbsTag() {
        super("breadcrumbs", false, true);
    }

    @Override
    public String[] parameters() {
        return new String[] {"key"};
    }

}