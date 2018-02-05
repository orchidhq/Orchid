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

    Collection getItems();

    default String getRowTemplate() {
        return getKey();
    }

    default String getItemId(Object item) {
        return item.getClass().getSimpleName();
    }

    default Object getItem(String id) {
        for(Object item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
