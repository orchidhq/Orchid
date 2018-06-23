package com.eden.orchid.api.generators;

import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Stream;

@Description("A Folder Collection represents a collection of OrchidPages that are located as a batch from a " +
        "specified folder in your resources. A page is matched from a Folder Collection with an 'itemId' matching " +
        "the page's title."
)
public class FolderCollection extends OrchidCollection<OrchidPage> {

    @Getter private final String resourceRoot;
    @Getter private final Class<? extends OrchidPage> pageClass;
    @Getter @Setter private boolean canCreate = true;
    @Getter @Setter private boolean canDelete = true;
    @Getter @Setter private String slugFormat = "{slug}";

    public FolderCollection(OrchidGenerator generator, String collectionId, List<OrchidPage> items, Class<? extends OrchidPage> pageClass, String resourceRoot) {
        this(generator.getKey(), collectionId, items, pageClass, resourceRoot);
    }

    public FolderCollection(String collectionType, String collectionId, List<OrchidPage> items, Class<? extends OrchidPage> pageClass, String resourceRoot) {
        super(collectionType, collectionId, items);
        this.resourceRoot = resourceRoot;
        this.pageClass = pageClass;
    }

    public Stream<OrchidPage> find(String id) {
        return getItems()
                .stream()
                .filter(page -> page.getTitle().equals(id));
    }

}
