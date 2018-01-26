package com.eden.orchid.api.indexing;

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
    public List<?> findAllInCollection(String itemId) {
        return collections
                .stream()
                .flatMap(o -> ((List<?>) o.find(itemId)).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<?> findAllInCollection(String collectionType, String itemId) {
        return collections
                .stream()
                .filter(collection -> collection.getCollectionType().equals(collectionType))
                .flatMap(o -> ((List<?>) o.find(itemId)).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<?> findAllInCollection(String collectionType, String collectionId, String itemId) {
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

    @Override
    public <T> List<T> findAllInCollection(Class<? extends OrchidCollection<T>> collectionType, String itemId) {
        return collections
                .stream()
                .filter(collection -> collectionType.isAssignableFrom(collection.getClass()))
                .flatMap(o -> ((List<T>) o.find(itemId)).stream())
                .collect(Collectors.toList());
    }

    @Override
    public <T> List<T> findAllInCollection(Class<? extends OrchidCollection<T>> collectionType, String collectionId, String itemId) {
        Optional<? extends OrchidCollection> foundCollection = collections
                .stream()
                .filter(collection -> collectionType.isAssignableFrom(collection.getClass()))
                .filter(collection -> collection.getCollectionId().equals(collectionId))
                .findFirst();

        if (foundCollection.isPresent()) {
            return foundCollection.get().find(itemId);
        }

        return new ArrayList<>();
    }

    @Override
    public Object findInCollection(String itemId) {
        return OrchidUtils.first(findAllInCollection(itemId));
    }

    @Override
    public Object findInCollection(String collectionType, String itemId) {
        return OrchidUtils.first(findAllInCollection(collectionType, itemId));
    }

    @Override
    public Object findInCollection(String collectionType, String collectionId, String itemId) {
        return OrchidUtils.first(findAllInCollection(collectionType, collectionId, itemId));
    }

    @Override
    public <T> T findInCollection(Class<? extends OrchidCollection<T>> collectionType, String itemId) {
        return OrchidUtils.first(findAllInCollection(collectionType, itemId));
    }

    @Override
    public <T> T findInCollection(Class<? extends OrchidCollection<T>> collectionType, String collectionId, String itemId) {
        return OrchidUtils.first(findAllInCollection(collectionType, collectionId, itemId));
    }
}
