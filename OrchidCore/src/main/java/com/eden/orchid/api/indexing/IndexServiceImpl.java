package com.eden.orchid.api.indexing;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.GlobalCollection;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private Set<GlobalCollection> globalCollections;

    private List<OrchidCollection> collections;

    @Inject
    public IndexServiceImpl(Set<GlobalCollection> globalCollections) {
        this.globalCollections = Collections.unmodifiableSet(globalCollections);
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void clearIndex() {
        internalIndex = new OrchidRootInternalIndex();
        externalIndex = new OrchidRootExternalIndex();
        globalCollections.forEach(GlobalCollection::clear);
        collections = new ArrayList<>(globalCollections);
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
        return filterCollections(getCollections(collectionType, collectionId), itemId).findFirst().orElse(null);
    }

    @Override
    public OrchidPage findPage(String collectionType, String collectionId, String itemId) {
        Object object = find(collectionType, collectionId, itemId);
        if(object instanceof OrchidPage) {
            return (OrchidPage) object;
        }

        return null;
    }

    @Override
    public List<?> findAll(String collectionType, String collectionId, String itemId) {
        return optionallyFilterCollections(getCollections(collectionType, collectionId), itemId).collect(Collectors.toList());
    }

    @Override
    public List<?> findAll(String collectionType, String collectionId, String itemId, int page, int pageSize) {
        return optionallyFilterCollections(getCollections(collectionType, collectionId), itemId)
                .skip((page-1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

// Helpers
//----------------------------------------------------------------------------------------------------------------------

    private Stream<? extends OrchidCollection> getCollections(String collectionType, String collectionId) {
        Stream<? extends OrchidCollection> stream = collections.stream().filter(Objects::nonNull);

        if(!EdenUtils.isEmpty(collectionType)) {
            stream = stream.filter(collection -> collectionType.equals(collection.getCollectionType()));
        }
        if(!EdenUtils.isEmpty(collectionId)) {
            stream = stream.filter(collection -> collectionId.equals(collection.getCollectionId()));
        }

        return stream;
    }

    private Stream<?> filterCollections(Stream<? extends OrchidCollection> collections, String itemId) {
        return collections.flatMap(o -> (o.findMatches(itemId))).distinct();
    }

    private Stream<?> optionallyFilterCollections(Stream<? extends OrchidCollection> collections, String itemId) {
        if(!EdenUtils.isEmpty(itemId)) {
            return collections.flatMap(o -> (o.findMatches(itemId))).distinct();
        }
        else {
            return collections
                    .map(OrchidCollection::getItems)
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .distinct();
        }
    }

}
