package com.eden.orchid.javadoc;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;

import javax.inject.Inject;

public class JavadocResourceSource extends DefaultResourceSource {

    @Inject
    public JavadocResourceSource(OrchidContext context) {
        super(context);
        setPriority(10);
    }
}
