package com.eden.orchid.api.generators;

import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;
import java.util.stream.Stream;

public class FileCollection extends OrchidCollection<OrchidPage> {

    public FileCollection(OrchidGenerator generator, String collectionId, List<OrchidPage> items) {
        this(generator.getKey(), collectionId, items);
    }

    public FileCollection(String collectionType, String collectionId, List<OrchidPage> items) {
        super(collectionType, collectionId, items);
    }

    public Stream<OrchidPage> find(String id) {
        return getItems()
                .stream()
                .filter(page -> page.getTitle().equals(id));
    }

}
