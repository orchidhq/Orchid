package com.eden.orchid.impl.resources;

import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;

import javax.inject.Inject;

public class CoreDefaultResourceSource extends DefaultResourceSource {

    @Inject
    public CoreDefaultResourceSource() {
        setPriority(1);
    }
}
