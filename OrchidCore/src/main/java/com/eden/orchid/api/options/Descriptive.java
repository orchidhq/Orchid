package com.eden.orchid.api.options;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.annotations.Description;

public interface Descriptive {

    default String getDescription() {
        return (this.getClass().getAnnotation(Description.class) != null && !EdenUtils.isEmpty(this.getClass().getAnnotation(Description.class).value()))
                ? this.getClass().getAnnotation(Description.class).value()
                : "";
    }

}
