package com.eden.orchid.api.indexing;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.GlobalCollection;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;

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

    private final Set<GlobalCollection> globalCollections;

    @Getter private OrchidRootIndex internalIndex;
    @Getter private OrchidRootIndex externalIndex;
    @Getter private OrchidRootIndex compositeIndex;

    @Getter private List<OrchidCollection> collections;

    @Inject
    public IndexServiceImpl(Set<GlobalCollection> globalCollections) {
        this.globalCollections = Collections.unmodifiableSet(globalCollections);
    }

    @Override
    public void initialize(OrchidContext context) {

    }

    @Override
    public void clearIndex() {
        internalIndex = new OrchidRootIndex("internal");
        externalIndex = new OrchidRootIndex("external");
        compositeIndex = new OrchidRootIndex("composite");
        globalCollections.forEach(GlobalCollection::clear);
        collections = new ArrayList<>(globalCollections);
    }

    @Override
    public void buildCompositeIndex() {
        compositeIndex.addChildIndex(internalIndex.getOwnKey(), internalIndex);
        compositeIndex.addChildIndex(externalIndex.getOwnKey(), externalIndex);
    }

// Collections
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void addCollections(List<? extends OrchidCollection> collections) {
        this.collections.addAll(collections);
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
