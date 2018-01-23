package com.eden.orchid.api.generators;

import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;
import java.util.stream.Collectors;

public class FileCollection extends OrchidCollection<OrchidPage> {

    public FileCollection(OrchidGenerator generator, String collectionId, List<OrchidPage> items) {
        this(generator, generator.getKey(), collectionId, items);
    }

    public FileCollection(OrchidGenerator generator, String collectionType, String collectionId, List<OrchidPage> items) {
        super(generator, collectionType, collectionId, items);
    }

    public List<OrchidPage> find(String id) {
        return items
                .stream()
                .filter(page -> page.getReference().getRelativePath().equals(id))
                .collect(Collectors.toList());
    }

}
