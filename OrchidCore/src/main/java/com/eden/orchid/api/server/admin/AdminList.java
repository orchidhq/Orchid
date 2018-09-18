package com.eden.orchid.api.server.admin;

import com.eden.orchid.api.options.Descriptive;

import java.util.Collection;

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
        return Descriptive.getDescriptiveName(getListClass());
    }

    default String getDescription() {
        return Descriptive.getDescription(getListClass());
    }
}
