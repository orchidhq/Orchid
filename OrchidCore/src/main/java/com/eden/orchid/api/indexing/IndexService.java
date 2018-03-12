package com.eden.orchid.api.indexing;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.ImplementedBy;

import java.util.Collection;
import java.util.List;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@ImplementedBy(IndexServiceImpl.class)
public interface IndexService extends OrchidService {

    String[] locateParams = new String[] {"itemId", "collectionId", "collectionType"};

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

    default void addCollections(List<? extends OrchidCollection> collections) {
        getService(IndexService.class).addCollections(collections);
    }

    default List<? extends OrchidCollection> getCollections() { return getService(IndexService.class).getCollections(); }

    default Object find(String collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).find(collectionType, collectionId, itemId);
    }

    default List<?> findAll(String collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).findAll(collectionType, collectionId, itemId);
    }

    default OrchidPage findPage(String collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).findPage(collectionType, collectionId, itemId);
    }

}
