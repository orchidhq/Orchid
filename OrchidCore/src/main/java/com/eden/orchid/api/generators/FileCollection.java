package com.eden.orchid.api.generators;

import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;
import java.util.stream.Stream;

@Description("A File Collection represents a collection of OrchidPages that come from individually-specified pages in " +
        "your resources. A page is matched from a File Collection with an 'itemId' matching the page's title."
)
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
