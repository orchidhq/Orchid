package com.eden.orchid.api.generators;

import com.eden.orchid.api.resources.resource.OrchidResource;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ResourceCollection<T> extends OrchidCollection<T> {

    public ResourceCollection(OrchidGenerator generator, String collectionId, List<T> items) {
        this(generator, generator.getKey(), collectionId, items);
    }

    public ResourceCollection(OrchidGenerator generator, String collectionType, String collectionId, List<T> items) {
        super(generator, collectionType, collectionId, items);
    }

    public List<T> find(String id) {
        return items
                .stream()
                .filter(item -> getResource(item).getReference().getRelativePath().equals(id))
                .collect(Collectors.toList());
    }

    public List<OrchidResource> getResources() {
        return items
                .stream()
                .map(this::getResource)
                .collect(Collectors.toList());
    }

    public abstract OrchidResource getResource(T obj);

    public abstract Class<T> getItemClass();

}
