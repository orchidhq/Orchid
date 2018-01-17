package com.eden.orchid.api.server.admin;

import java.util.Collection;

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
