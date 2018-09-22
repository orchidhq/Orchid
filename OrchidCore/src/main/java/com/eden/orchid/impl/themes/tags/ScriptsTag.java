package com.eden.orchid.impl.themes.tags;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Description;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Description(value = "Render all script tags to the page.", name = "Scripts")
public final class ScriptsTag extends TemplateTag {

    @Inject
    public ScriptsTag() {
        super("scripts", Type.Simple, true);
    }

    @Override
    public String[] parameters() {
        return new String[]{};
    }

}