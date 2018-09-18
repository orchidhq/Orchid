package com.eden.orchid.api.server.admin;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.Descriptive;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.utilities.OrchidExtensionsKt;

import java.util.Collection;

import static com.eden.orchid.utilities.OrchidExtensionsKt.from;
import static com.eden.orchid.utilities.OrchidExtensionsKt.to;

/**
 * Admin Lists add collections of items to be displayed within the admin area for the purposes of self-documentation and
 * discovery.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public interface AdminList extends Descriptive {

    String getKey();

    Class<?> getListClass();

    Collection<Class<?>> getItems();

    boolean isImportantType();

    default String getDescriptiveName() {
        Description annotation = getListClass().getAnnotation(Description.class);
        return (annotation != null && !EdenUtils.isEmpty(annotation.name()))
                ? annotation.name()
                : to(from(getListClass().getSimpleName(), OrchidExtensionsKt::camelCase), OrchidExtensionsKt::titleCase);
    }

    default String getDescription() {
        Description annotation = getListClass().getAnnotation(Description.class);
        return (annotation != null && !EdenUtils.isEmpty(annotation.value()))
                ? annotation.value()
                : "";
    }
}
