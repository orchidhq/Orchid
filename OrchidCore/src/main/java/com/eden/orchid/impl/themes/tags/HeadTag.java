package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
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