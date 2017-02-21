package com.eden.orchid.impl.resources;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;

import javax.inject.Inject;

public class CoreLocalResourceSource extends LocalResourceSource {

    @Inject
    public CoreLocalResourceSource(OrchidContext context) {
        super(context);
        setPriority(Integer.MAX_VALUE);
    }

    @Override
    public String getDirectory() {
        String directory;
        if (!EdenUtils.isEmpty(this.context.query("options.resourcesDir"))) {
            directory = this.context.query("options.resourcesDir").toString();
        }
        else {
            directory = "";
        }

        return directory;
    }
}
