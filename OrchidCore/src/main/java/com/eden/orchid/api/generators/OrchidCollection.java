package com.eden.orchid.api.generators;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A Collection represents a set of local resources which are consumed by a generator. Inspired by (and used for) the
 * Netlify CMS, Collections can also be used for other descriptive purposes, and are an opt-in feature of Generators.
 *
 * @since v1.0.0
 * @extensible classes
 */
public abstract class OrchidCollection<T> {

    @Getter protected final OrchidGenerator generator;
    @Getter protected final String collectionType;
    @Getter protected final String collectionId;
    @Getter protected final List<T> items;

    @Getter @Setter protected String label;

    public abstract List<T> find(String id);

    public OrchidCollection(OrchidGenerator generator, String collectionType, String collectionId, List<T> items) {
        this.generator = generator;
        this.collectionType = collectionType;
        this.collectionId = collectionId;
        this.items = items;
    }
}
