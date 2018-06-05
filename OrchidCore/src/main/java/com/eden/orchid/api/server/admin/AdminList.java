package com.eden.orchid.api.server.admin;

import java.util.Collection;

/**
 * Admin Lists add collections of items to be displayed within the admin area for the purposes of self-documentation and
 * discovery.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public interface AdminList {

    String getKey();

    Class<?> getListClass();

    Collection<Class<?>> getItems();

    boolean isImportantType();
}
