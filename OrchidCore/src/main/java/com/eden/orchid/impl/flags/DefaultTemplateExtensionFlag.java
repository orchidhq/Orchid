package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("Set the default extension to look for when loading templates. Themes set the extension they prefer, " +
        "but this value is used as a fallback. The default value is `peb` for Pebble.")
public final class DefaultTemplateExtensionFlag extends OrchidFlag {

    public DefaultTemplateExtensionFlag() {
        super("defaultTemplateExtension", true, "peb");
    }

}