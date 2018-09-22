package com.eden.orchid.api.options;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.utilities.OrchidExtensionsKt;

import static com.eden.orchid.utilities.OrchidExtensionsKt.*;

public interface Descriptive {

    default String getDescriptiveName() {
        return getDescriptiveName(getClass());
    }

    default String getDescription() {
        return getDescription(getClass());
    }

    default String getDescriptionSummary() {
        return getDescriptionSummary(getClass());
    }

    static String getDescriptiveName(Class<?> describingClass) {
        Description annotation = describingClass.getAnnotation(Description.class);
        return (annotation != null && !EdenUtils.isEmpty(annotation.name()))
                ? annotation.name()
                : to(from(describingClass.getSimpleName(), OrchidExtensionsKt::camelCase), OrchidExtensionsKt::titleCase);
    }

    static String getDescription(Class<?> describingClass) {
        Description annotation = describingClass.getAnnotation(Description.class);
        return (annotation != null && !EdenUtils.isEmpty(annotation.value()))
                ? annotation.value()
                : "";
    }

    static String getDescriptionSummary(Class<?> describingClass) {
        String description = getDescription(describingClass);
        return (description.indexOf(".") > 0)
                ? description.substring(0, description.indexOf(".")+1)
                : description;
    }

}
