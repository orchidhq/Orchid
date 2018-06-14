package com.eden.orchid.api.generators;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * A Collection represents a set of local resources which are consumed by a generator. Inspired by (and used for) the
 * Netlify CMS, Collections can also be used for other descriptive purposes, and are an opt-in feature of Generators.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class OrchidCollection<T> {

    @Getter protected final String collectionType;
    @Getter protected final String collectionId;

    @Getter
    private List<T> items;

    public OrchidCollection(String collectionType, String collectionId, List<T> items) {
        this.collectionType = collectionType;
        this.collectionId = collectionId;
        setItems(items);
    }

    public OrchidCollection(OrchidGenerator generator, String collectionId, List<T> items) {
        this(generator.getKey(), collectionId, items);
    }

    protected abstract Stream<T> find(String id);

    public Stream<T> findMatches(String id) {
        return find(id);
    }

    public String getTitle() {
        String collectionTypeTitle = null;
        String collectionIdTitle = null;

        String[] collectionTypeWords = OrchidExtensionsKt.from(collectionType, OrchidExtensionsKt::camelCase);
        collectionTypeWords = OrchidExtensionsKt.with(collectionTypeWords, StringUtils::capitalize);
        collectionTypeTitle = OrchidExtensionsKt.to(collectionTypeWords, OrchidExtensionsKt::titleCase).trim();

        if(collectionId != null) {
            String[] collectionIdWords = OrchidExtensionsKt.from(collectionId, OrchidExtensionsKt::camelCase);
            collectionIdWords = OrchidExtensionsKt.with(collectionIdWords, StringUtils::capitalize);
            collectionIdTitle = OrchidExtensionsKt.to(collectionIdWords, OrchidExtensionsKt::titleCase).trim();
        }

        if(!EdenUtils.isEmpty(collectionIdTitle) && !collectionTypeTitle.equals(collectionIdTitle)) {
            return collectionTypeTitle + " > " + collectionIdTitle;
        }
        else {
            return collectionTypeTitle;
        }
    }

    public void setItems(List<T> items) {
        this.items = (items != null) ? Collections.unmodifiableList(items) : null;
    }

    public void clear() {
        this.items = null;
    }
}
