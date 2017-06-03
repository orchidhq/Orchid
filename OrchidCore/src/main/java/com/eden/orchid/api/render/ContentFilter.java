package com.eden.orchid.api.render;

import com.eden.orchid.api.registration.Prioritized;

public abstract class ContentFilter extends Prioritized {
    public abstract String apply(String content);
}
