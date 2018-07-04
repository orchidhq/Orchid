package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ScriptsTag extends TemplateTag {

    @Inject
    public ScriptsTag() {
        super("scripts", false, true);
    }

    @Override
    public String[] parameters() {
        return new String[] {};
    }

}