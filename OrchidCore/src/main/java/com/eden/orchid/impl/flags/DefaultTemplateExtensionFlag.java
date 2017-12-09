package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;

public final class DefaultTemplateExtensionFlag implements OrchidFlag {

    @Override
    public String getFlag() {
        return "defaultTemplateExtension";
    }

    @Override
    public String getDescription() {
        return "Set the default extension to look for when loading templates. Themes set the extension they prefer, " +
                "but this value is used as a fallback. The default value is `peb` for Pebble.";
    }

    @Override
    public Object getDefaultValue() {
        return "peb";
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
