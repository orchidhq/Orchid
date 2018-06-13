package com.eden.orchid.api.generators;

import java.util.List;
import java.util.stream.Stream;

/**
 * A Collection represents a set of local resources which are consumed by a generator. Inspired by (and used for) the
 * Netlify CMS, Collections can also be used for other descriptive purposes, and are an opt-in feature of Generators.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class GlobalCollection<T> extends OrchidCollection<T> {

    public GlobalCollection(String collectionId) {
        super("global", collectionId, null);
    }

    protected abstract List<T> loadItems();

    public Stream<T> findMatches(String id) {
        if(getItems() == null) {
            setItems(loadItems());
        }

        return find(id);
    }

}
