package com.eden.orchid.api.indexing;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@Singleton
public final class IndexServiceImpl implements IndexService {

    private OrchidContext context;

    private OrchidRootInternalIndex internalIndex;
    private OrchidRootExternalIndex externalIndex;
    private OrchidCompositeIndex compositeIndex;

    private List<OrchidCollection> collections;

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void clearIndex() {
        this.internalIndex = new OrchidRootInternalIndex();
        this.externalIndex = new OrchidRootExternalIndex();
        this.collections = new ArrayList<>();
    }

    @Override
    public OrchidIndex getIndex() {
        return internalIndex;
    }

    @Override
    public OrchidRootInternalIndex getInternalIndex() {
        return internalIndex;
    }

    @Override
    public OrchidRootExternalIndex getExternalIndex() {
        return externalIndex;
    }

    @Override
    public OrchidCompositeIndex getCompositeIndex() {
        return compositeIndex;
    }

    @Override
    public void mergeIndices(OrchidIndex... indices) {
        this.compositeIndex = new OrchidCompositeIndex("composite");
        for (OrchidIndex index : indices) {
            if (index != null) {
                this.compositeIndex.mergeIndex(index);
            }
        }
    }

    @Override
    public List<OrchidPage> getGeneratorPages(String generator) {
        return internalIndex.getGeneratorPages(generator);
    }

    public void addChildIndex(String key, OrchidInternalIndex index) {
        this.internalIndex.addChildIndex(key, index);
    }

    public void addExternalChildIndex(OrchidIndex index) {
        this.externalIndex.addChildIndex(index);
    }

    public OrchidIndex createIndex(String rootKey, Collection<? extends OrchidPage> pages) {
        OrchidIndex index = new OrchidInternalIndex(rootKey);

        for (OrchidPage page : pages) {
            OrchidReference ref = new OrchidReference(page.getReference());
            index.addToIndex(ref.getPath(), page);
        }

        return index;
    }

// Collections
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void addCollections(List<? extends OrchidCollection> collections) {
        this.collections.addAll(collections);
    }

    @Override
    public List<OrchidCollection> getCollections() {
        return collections;
    }

    @Override
    public Object find(String collectionType, String collectionId, String itemId) {
        Object object;
        if(!EdenUtils.isEmpty(collectionId) && !EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            object = findInCollection(collectionType, collectionId, itemId);
        }
        else if(!EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            object = findInCollection(collectionType, itemId);
        }
        else if(!EdenUtils.isEmpty(itemId)) {
            object = findInCollection(itemId);
        }
        else {
            object = null;
        }

        return object;
    }

    @Override
    public List<?> findAll(String collectionType, String collectionId, String itemId) {
        List<?> objects;
        if(!EdenUtils.isEmpty(collectionId) && !EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            objects = findAllInCollection(collectionType, collectionId, itemId);
        }
        else if(!EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            objects = findAllInCollection(collectionType, itemId);
        }
        else if(!EdenUtils.isEmpty(itemId)) {
            objects = findAllInCollection(itemId);
        }
        else {
            objects = null;
        }

        return objects;
    }

    @Override
    public OrchidPage findPage(String collectionType, String collectionId, String itemId) {
        Object object = find(collectionType, collectionId, itemId);
        if(object instanceof OrchidPage) {
            return (OrchidPage) object;
        }

        return null;
    }

// Helpers
//----------------------------------------------------------------------------------------------------------------------

    private List<?> findAllInCollection(String itemId) {
        return collections
                .stream()
                .flatMap(o -> ((List<?>) o.find(itemId)).stream())
                .collect(Collectors.toList());
    }

    private List<?> findAllInCollection(String collectionType, String itemId) {
        return collections
                .stream()
                .filter(collection -> collection.getCollectionType().equals(collectionType))
                .flatMap(o -> ((List<?>) o.find(itemId)).stream())
                .collect(Collectors.toList());
    }

    private List<?> findAllInCollection(String collectionType, String collectionId, String itemId) {
        Optional<? extends OrchidCollection> foundCollection = collections
                .stream()
                .filter(collection -> collection.getCollectionType().equals(collectionType))
                .filter(collection -> collection.getCollectionId().equals(collectionId))
                .findFirst();

        if (foundCollection.isPresent()) {
            return foundCollection.get().find(itemId);
        }

        return new ArrayList<>();
    }

    private Object findInCollection(String itemId) {
        return OrchidUtils.first(findAllInCollection(itemId));
    }

    private Object findInCollection(String collectionType, String itemId) {
        return OrchidUtils.first(findAllInCollection(collectionType, itemId));
    }

    private Object findInCollection(String collectionType, String collectionId, String itemId) {
        return OrchidUtils.first(findAllInCollection(collectionType, collectionId, itemId));
    }

}
