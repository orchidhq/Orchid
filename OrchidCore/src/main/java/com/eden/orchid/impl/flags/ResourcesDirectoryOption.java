package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("the directory containing your content files")
public final class ResourcesDirectoryOption extends OrchidFlag {

    public ResourcesDirectoryOption() {
        super("resourcesDir", true, null);
    }

}
