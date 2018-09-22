package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Description;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Description(value = "Render all style tags to the page.", name = "Styles")
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