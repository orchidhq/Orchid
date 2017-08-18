package com.eden.orchid.api.server.admin;

import java.util.Collection;

public interface AdminList<T> {

    String getKey();

    Collection<T> getItems();

    default String getRowTemplate() {
        return getKey();
    }

    default String getItemId(T item) {
        return item.getClass().getSimpleName();
    }

    default T getItem(String id) {
        for(T item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
