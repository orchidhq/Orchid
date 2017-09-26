package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;

public final class ResourcesDirectoryOption implements OrchidFlag {

    @Override
    public String getFlag() {
        return "resourcesDir";
    }

    @Override
    public String getDescription() {
        return "the directory containing your content files";
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
