package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Description;

import javax.inject.Inject;

@Description(value = "All the default SEO tags that need to be in the HTML `<head>`.", name = "Head")
public final class HeadTag extends TemplateTag {

    @Inject
    public HeadTag() {
        super("head", Type.Simple, true);
    }

    @Override
    public String[] parameters() {
        return new String[] {};
    }

}