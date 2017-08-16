package com.eden.orchid.api.indexing;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.indexing.OrchidCompositeIndex;
import com.eden.orchid.impl.indexing.OrchidInternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootExternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootInternalIndex;

import java.util.Collection;
import java.util.List;

public interface IndexService extends OrchidService {

    default void clearIndex() {
        getService(IndexService.class).clearIndex();
    }

    default OrchidIndex getIndex() {
        return getService(IndexService.class).getIndex();
    }

    default OrchidRootInternalIndex getInternalIndex() {
        return getService(IndexService.class).getInternalIndex();
    }

    default OrchidRootExternalIndex getExternalIndex() {
        return getService(IndexService.class).getExternalIndex();
    }

    default OrchidCompositeIndex getCompositeIndex() {
        return getService(IndexService.class).getCompositeIndex();
    }

    default void mergeIndices(OrchidIndex... indices) {
        getService(IndexService.class).mergeIndices(indices);
    }

    default List<OrchidPage> getGeneratorPages(String generator) {
        return getService(IndexService.class).getGeneratorPages(generator);
    }

    default void addChildIndex(String key, OrchidInternalIndex index) {
        getService(IndexService.class).addChildIndex(key, index);
    }

    default void addExternalChildIndex(OrchidIndex index) {
        getService(IndexService.class).addExternalChildIndex(index);
    }

    default OrchidIndex createIndex(String rootKey, Collection<? extends OrchidPage> pages) {
        return getService(IndexService.class).createIndex(rootKey, pages);
    }

    default OrchidPage findPageByName(String name) {
        return getService(IndexService.class).findPageByName(name);
    }

    default OrchidPage findPageByPath(String path) {
        return getService(IndexService.class).findPageByPath(path);
    }
}
