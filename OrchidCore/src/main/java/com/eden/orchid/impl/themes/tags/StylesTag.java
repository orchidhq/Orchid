package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class StylesTag extends TemplateTag {

    @Inject
    public StylesTag() {
        super("styles", Type.Simple, true);
    }

    @Override
    public String[] parameters() {
        return new String[] {};
    }

}