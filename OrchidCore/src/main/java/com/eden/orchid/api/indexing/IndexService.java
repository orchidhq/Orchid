package com.eden.orchid.api.indexing;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.ImplementedBy;

import java.util.List;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@ImplementedBy(IndexServiceImpl.class)
public interface IndexService extends OrchidService {

    String[] locateParams = new String[] {"itemId", "collectionType", "collectionId"};

    default void clearIndex() {
        getService(IndexService.class).clearIndex();
    }

    default OrchidRootIndex getInternalIndex() {
        return getService(IndexService.class).getInternalIndex();
    }

    default OrchidRootIndex getExternalIndex() {
        return getService(IndexService.class).getExternalIndex();
    }

    default OrchidRootIndex getCompositeIndex() {
        return getService(IndexService.class).getCompositeIndex();
    }

    default void buildCompositeIndex() {
        getService(IndexService.class).buildCompositeIndex();
    }

    default void addCollections(List<? extends OrchidCollection> collections) {
        getService(IndexService.class).addCollections(collections);
    }

    default List<? extends OrchidCollection> getCollections() {
        return getService(IndexService.class).getCollections();
    }

    default Object find(String collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).find(collectionType, collectionId, itemId);
    }

    default OrchidPage findPage(String collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).findPage(collectionType, collectionId, itemId);
    }

    default List<?> findAll(String collectionType, String collectionId, String itemId) {
        return getService(IndexService.class).findAll(collectionType, collectionId, itemId);
    }

    default List<?> findAll(String collectionType, String collectionId, String itemId, int page, int pageSize) {
        return getService(IndexService.class).findAll(collectionType, collectionId, itemId, page, pageSize);
    }

}
