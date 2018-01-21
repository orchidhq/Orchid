package com.eden.orchid.api.generators;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A Collection represents a set of local resources which are consumed by a generator. Inspired by (and used for) the
 * Netlify CMS, Collections can also be used for other descriptive purposes, and are an opt-in feature of Generators.
 */
public final class OrchidCollection {

    public enum Type {
        Files, Folder
    }

    @Getter private final Type collectionType;
    @Getter private final OrchidGenerator generator;
    @Getter private final String name;
    @Getter private final String label;

    // for Folder collections
    @Getter private final String resourceRoot;
    @Getter private final Class<? extends OrchidPage> collectionPageType;
    @Getter @Setter private boolean canCreate = true;

    // for File collections
    @Getter private final List<OrchidPage> resources;

    public OrchidCollection(OrchidGenerator generator, String label, String resourceRoot, Class<? extends OrchidPage> collectionPageType) {
        this(generator, OrchidUtils.toSlug(label), label, resourceRoot, collectionPageType);
    }

    public OrchidCollection(OrchidGenerator generator, String name, String label, String resourceRoot, Class<? extends OrchidPage> collectionPageType) {
        this.collectionType = Type.Folder;
        this.generator = generator;
        this.name = name;
        this.label = label;
        this.resourceRoot = resourceRoot;
        if(collectionPageType != null) {
            this.collectionPageType = collectionPageType;
        }
        else {
            this.collectionPageType = OrchidPage.class;
        }
        this.resources = null;
    }

    public OrchidCollection(OrchidGenerator generator, String label, List<OrchidPage> resources) {
        this(generator, OrchidUtils.toSlug(label), label, resources);
    }

    public OrchidCollection(OrchidGenerator generator, String name, String label, List<OrchidPage> resources) {
        this.collectionType = Type.Files;
        this.generator = generator;
        this.name = name;
        this.label = label;
        this.resourceRoot = null;
        this.collectionPageType = null;
        this.resources = resources;
    }
}
