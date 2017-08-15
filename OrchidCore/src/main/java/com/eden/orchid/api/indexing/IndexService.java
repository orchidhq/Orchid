package com.eden.orchid.api.indexing;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.indexing.OrchidCompositeIndex;
import com.eden.orchid.impl.indexing.OrchidInternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootExternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootInternalIndex;

import java.util.Collection;
import java.util.List;

public interface IndexService {

    IndexService getIndexService();

    default void clearIndex() {
        getIndexService().clearIndex();
    }

    default OrchidIndex getIndex() {
        return getIndexService().getIndex();
    }

    default OrchidRootInternalIndex getInternalIndex() {
        return getIndexService().getInternalIndex();
    }

    default OrchidRootExternalIndex getExternalIndex() {
        return getIndexService().getExternalIndex();
    }

    default OrchidCompositeIndex getCompositeIndex() {
        return getIndexService().getCompositeIndex();
    }

    default void mergeIndices(OrchidIndex... indices) {
        getIndexService().mergeIndices(indices);
    }

    default List<OrchidPage> getGeneratorPages(String generator) {
        return getIndexService().getGeneratorPages(generator);
    }

    default void addChildIndex(String key, OrchidInternalIndex index) {
        getIndexService().addChildIndex(key, index);
    }

    default void addExternalChildIndex(OrchidIndex index) {
        getIndexService().addExternalChildIndex(index);
    }

    default OrchidIndex createIndex(String rootKey, Collection<? extends OrchidPage> pages) {
        return getIndexService().createIndex(rootKey, pages);
    }

    default OrchidPage findPageByName(String name) {
        return getIndexService().findPageByName(name);
    }

    default OrchidPage findPageByPath(String path) {
        return getIndexService().findPageByPath(path);
    }
}
