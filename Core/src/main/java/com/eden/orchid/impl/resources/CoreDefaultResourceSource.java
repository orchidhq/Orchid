package com.eden.orchid.impl.resources;

import javax.inject.Inject;

public class CoreDefaultResourceSource extends DefaultResourceSource {

    @Inject
    public CoreDefaultResourceSource() {
        setPriority(1);
    }
}
