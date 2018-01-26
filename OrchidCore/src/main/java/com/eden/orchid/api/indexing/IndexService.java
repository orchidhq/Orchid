package com.eden.orchid.api.indexing;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.theme.pages.OrchidPage;

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

    default void addCollections(List<? extends OrchidCollection> collections) {
        getService(IndexService.class).addCollections(collections);
    }

    default List<? extends OrchidCollection> getCollections() { return getService(IndexService.class).getCollections(); }

    default List<?> findAllInCollection(String itemId) {
        return getService(IndexService.class).findAllInCollection(itemId);
    }

    default List<?> findAllInCollection(String collectionType, String itemId) {
        return getService(IndexService.class).findAllInCollection(collectionType, itemId);
    }

    default List<?> findAllInCollection(String collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).findAllInCollection(collectionType, collectionId, itemId);
    }

    default <T> List<T> findAllInCollection(Class<? extends OrchidCollection<T>> collectionType, String itemId) {
        return getService(IndexService.class).findAllInCollection(collectionType, itemId);
    }

    default <T> List<T> findAllInCollection(Class<? extends OrchidCollection<T>> collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).findAllInCollection(collectionType, collectionId, itemId);
    }

    default Object findInCollection(String itemId) {
        return getService(IndexService.class).findInCollection(itemId);
    }

    default Object findInCollection(String collectionType, String itemId) {
        return getService(IndexService.class).findInCollection(collectionType, itemId);
    }

    default Object findInCollection(String collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).findInCollection(collectionType, collectionId, itemId);
    }

    default <T> T findInCollection(Class<? extends OrchidCollection<T>> collectionType, String itemId) {
        return getService(IndexService.class).findInCollection(collectionType, itemId);
    }

    default <T> T findInCollection(Class<? extends OrchidCollection<T>> collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).findInCollection(collectionType, collectionId, itemId);
    }

}
