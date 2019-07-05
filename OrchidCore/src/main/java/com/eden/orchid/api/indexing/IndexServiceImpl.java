package com.eden.orchid.api.indexing;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.generators.Collectible;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.CacheKt;
import com.eden.orchid.utilities.LRUCache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Description(value = "How Orchid organizes the data it collects.", name = "Index")
public final class IndexServiceImpl implements IndexService, OrchidEventListener {

    private OrchidRootIndex index;
    private List<OrchidCollection> collections;
    private final LRUCache<CollectionSearchCacheKey, Object> collectionSearchCache;

    @Inject
    public IndexServiceImpl() {
        this.collectionSearchCache = new LRUCache<>();
    }

    @Override
    public void initialize(OrchidContext context) {
    }

    @Override
    public OrchidRootIndex getIndex() {
        return this.index;
    }

    @Override
    public void clearIndex() {
        index = new OrchidRootIndex("index");
        collections = new ArrayList<>();
    }

// Collections
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void addCollections(List<? extends OrchidCollection> collections) {
        this.collections.addAll(collections);
    }

    @Override
    public Object find(String collectionType, String collectionId, String itemId) {
        final CollectionSearchCacheKey key = new CollectionSearchCacheKey(collectionType, collectionId, itemId);
        return CacheKt.computeIfAbsent(collectionSearchCache, key, () -> {
            return filterCollections(getCollections(collectionType, collectionId), itemId).findFirst().orElse(null);
        });
    }

    @Override
    public OrchidPage findPage(String collectionType, String collectionId, String itemId) {
        Object object = find(collectionType, collectionId, itemId);
        if (object instanceof OrchidPage) {
            return (OrchidPage) object;
        }
        return null;
    }

    @Override
    public OrchidPage findPageOrDefault(String collectionType, String collectionId, String itemId, OrchidPage defaultPage) {
        final OrchidPage foundPage;
        if (!EdenUtils.isEmpty(collectionType) || !EdenUtils.isEmpty(collectionId) || !EdenUtils.isEmpty(itemId)) {
            foundPage = findPage(collectionType, collectionId, itemId);
        } else {
            foundPage = null;
        }
        return (foundPage != null) ? foundPage : defaultPage;
    }

    @Override
    public List<?> findAll(String collectionType, String collectionId, String itemId) {
        return optionallyFilterCollections(getCollections(collectionType, collectionId), itemId).collect(Collectors.toList());
    }

    @Override
    public List<?> findAll(String collectionType, String collectionId, String itemId, int page, int pageSize) {
        return optionallyFilterCollections(getCollections(collectionType, collectionId), itemId).skip((page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    @Override
    public List<? extends OrchidCollection> getCollections(List<String> whitelist) {
        if (EdenUtils.isEmpty(whitelist)) return collections;
        return collections.stream().filter(Objects::nonNull).filter(collection -> {
            boolean passes = false;
            for (String filter : whitelist) {
                final String collectionType;
                final String collectionId;
                if (filter.contains(":")) {
                    collectionType = filter.split(":")[0];
                    collectionId = filter.split(":")[1];
                } else {
                    collectionType = filter;
                    collectionId = null;
                }
                if (collection.getCollectionType().equals(collectionType)) {
                    if (collectionId != null) {
                        if (collection.getCollectionId().equals(collectionId)) {
                            passes = true;
                        }
                    } else {
                        passes = true;
                    }
                }
            }
            return passes;
        }).collect(Collectors.toList());
    }

// Helpers
//----------------------------------------------------------------------------------------------------------------------

    private Stream<? extends OrchidCollection> getCollections(String collectionType, String collectionId) {
        Stream<? extends OrchidCollection> stream = collections.stream().filter(Objects::nonNull);
        if (!EdenUtils.isEmpty(collectionType)) {
            stream = stream.filter(collection -> collectionType.equals(collection.getCollectionType()));
        }
        if (!EdenUtils.isEmpty(collectionId)) {
            stream = stream.filter(collection -> collectionId.equals(collection.getCollectionId()));
        }
        return stream;
    }

    private Stream<?> filterCollections(Stream<? extends OrchidCollection> collections, String itemId) {
        return collections
                .flatMap(OrchidCollection::stream)              // 1. stream the collection, which may duplicate items to allow for multiple IDs
                .filter(Objects::nonNull)                       // 2. defensively remove null items in the stream
                .map(it -> (Collectible<?>) it)                 // 3. map each item to the generic interface
                .filter(it -> it.getItemIds().contains(itemId)) // 4. filter the stream to only match the items which match the itemId
                .map(Collectible::getItem)                      // 5. unwrap the matched items to get the object they represent
                .distinct();                                    // 6. make sure each object in the final stream is unique, in case multiple duplicated elements matched the itemId
    }

    private Stream<?> optionallyFilterCollections(Stream<? extends OrchidCollection> collections, String itemId) {
        if (!EdenUtils.isEmpty(itemId)) {
            return filterCollections(collections, itemId);
        } else {
            return collections
                    .flatMap(OrchidCollection::stream)              // 1. stream the collection, which may duplicate items to allow for multiple IDs
                    .filter(Objects::nonNull)                       // 2. defensively remove null items in the stream
                    .map(it -> (Collectible<?>) it)                 // 3. map each item to the generic interface
                    .map(Collectible::getItem)                      // 4. unwrap the matched items to get the object they represent
                    .distinct();                                    // 5. make sure each object in the final stream is unique, in case multiple duplicated elements matched the itemId
        }
    }

// Cache Implementation
//----------------------------------------------------------------------------------------------------------------------

    public static class CollectionSearchCacheKey {
        private final String collectionType;
        private final String collectionId;
        private final String itemId;

        public CollectionSearchCacheKey(final String collectionType, final String collectionId, final String itemId) {
            this.collectionType = collectionType;
            this.collectionId = collectionId;
            this.itemId = itemId;
        }

        public String getCollectionType() {
            return this.collectionType;
        }

        public String getCollectionId() {
            return this.collectionId;
        }

        public String getItemId() {
            return this.itemId;
        }

        @Override
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof IndexServiceImpl.CollectionSearchCacheKey)) return false;
            final IndexServiceImpl.CollectionSearchCacheKey other = (IndexServiceImpl.CollectionSearchCacheKey) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$collectionType = this.getCollectionType();
            final java.lang.Object other$collectionType = other.getCollectionType();
            if (this$collectionType == null ? other$collectionType != null : !this$collectionType.equals(other$collectionType))
                return false;
            final java.lang.Object this$collectionId = this.getCollectionId();
            final java.lang.Object other$collectionId = other.getCollectionId();
            if (this$collectionId == null ? other$collectionId != null : !this$collectionId.equals(other$collectionId))
                return false;
            final java.lang.Object this$itemId = this.getItemId();
            final java.lang.Object other$itemId = other.getItemId();
            if (this$itemId == null ? other$itemId != null : !this$itemId.equals(other$itemId)) return false;
            return true;
        }

        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof IndexServiceImpl.CollectionSearchCacheKey;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $collectionType = this.getCollectionType();
            result = result * PRIME + ($collectionType == null ? 43 : $collectionType.hashCode());
            final java.lang.Object $collectionId = this.getCollectionId();
            result = result * PRIME + ($collectionId == null ? 43 : $collectionId.hashCode());
            final java.lang.Object $itemId = this.getItemId();
            result = result * PRIME + ($itemId == null ? 43 : $itemId.hashCode());
            return result;
        }

        @Override
        public java.lang.String toString() {
            return "IndexServiceImpl.CollectionSearchCacheKey(collectionType=" + this.getCollectionType() + ", collectionId=" + this.getCollectionId() + ", itemId=" + this.getItemId() + ")";
        }
    }

    @On(Orchid.Lifecycle.ClearCache.class)
    public void onClearCache(Orchid.Lifecycle.ClearCache event) {
        collectionSearchCache.clear();
    }

    public List<OrchidCollection> getCollections() {
        return this.collections;
    }
}
