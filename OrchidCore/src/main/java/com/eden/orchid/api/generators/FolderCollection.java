package com.eden.orchid.api.generators;

import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class FolderCollection extends OrchidCollection<OrchidPage> {

    @Getter private final String resourceRoot;
    @Getter private final Class<? extends OrchidPage> pageClass;
    @Getter @Setter private boolean canCreate;
    @Getter @Setter private boolean canDelete;

    public FolderCollection(OrchidGenerator generator, String collectionId, List<OrchidPage> items, Class<? extends OrchidPage> pageClass, String resourceRoot) {
        this(generator, generator.getKey(), collectionId, items, pageClass, resourceRoot);
    }

    public FolderCollection(OrchidGenerator generator, String collectionType, String collectionId, List<OrchidPage> items, Class<? extends OrchidPage> pageClass, String resourceRoot) {
        super(generator, collectionType, collectionId, items);
        this.resourceRoot = resourceRoot;
        this.pageClass = pageClass;
    }

    public List<OrchidPage> find(String id) {
        return items
                .stream()
                .filter(page -> page.getReference().getRelativePath().equals(id))
                .collect(Collectors.toList());
    }

}
