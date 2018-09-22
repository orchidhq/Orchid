package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Description;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Description(value = "Render the main page components and page content.", name = "Page")
public final class PageTag extends TemplateTag {

    @Inject
    public PageTag() {
        super("page", Type.Simple, true);
    }

    @Override
    public String[] parameters() {
        return new String[] {};
    }

}