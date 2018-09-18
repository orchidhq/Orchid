package com.eden.orchid.api.options;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.utilities.OrchidExtensionsKt;

import static com.eden.orchid.utilities.OrchidExtensionsKt.*;

public interface Descriptive {

    default String getDescriptiveName() {
        Description annotation = getClass().getAnnotation(Description.class);
        return (annotation != null && !EdenUtils.isEmpty(annotation.name()))
                ? annotation.name()
                : to(from(getClass().getSimpleName(), OrchidExtensionsKt::camelCase), OrchidExtensionsKt::titleCase);
    }

    default String getDescription() {
        Description annotation = getClass().getAnnotation(Description.class);
        return (annotation != null && !EdenUtils.isEmpty(annotation.value()))
                ? annotation.value()
                : "";
    }

}
