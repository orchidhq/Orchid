package com.eden.orchid.api.generators;

import com.eden.orchid.api.resources.resource.OrchidResource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ResourceCollection<T> extends OrchidCollection<T> {

    public ResourceCollection(OrchidGenerator generator, String collectionId, List<T> items) {
        this(generator.getKey(), collectionId, items);
    }

    public ResourceCollection(String collectionType, String collectionId, List<T> items) {
        super(collectionType, collectionId, items);
    }

    public Stream<T> find(String id) {
        return getItems()
                .stream()
                .filter(item -> getResource(item).getReference().getRelativePath().equals(id));
    }

    public List<OrchidResource> getResources() {
        return getItems()
                .stream()
                .map(this::getResource)
                .collect(Collectors.toList());
    }

    public abstract OrchidResource getResource(T obj);

    public abstract Class<T> getItemClass();

}
