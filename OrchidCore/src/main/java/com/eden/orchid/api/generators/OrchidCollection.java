package com.eden.orchid.api.generators;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * A Collection represents a set of local resources which are consumed by a generator. Inspired by (and used for) the
 * Netlify CMS, Collections can also be used for other descriptive purposes, and are an opt-in feature of Generators.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class OrchidCollection<T> {

    @Getter protected final OrchidGenerator generator;
    @Getter protected final String collectionType;
    @Getter protected final String collectionId;
    @Getter protected final List<T> items;

    public abstract List<T> find(String id);

    public OrchidCollection(OrchidGenerator generator, String collectionType, String collectionId, List<T> items) {
        this.generator = generator;
        this.collectionType = collectionType;
        this.collectionId = collectionId;
        this.items = Collections.unmodifiableList(items);
    }

    public String getTitle() {
        String[] collectionTypeWords = OrchidExtensionsKt.from(collectionType, OrchidExtensionsKt::camelCase);
        collectionTypeWords = OrchidExtensionsKt.with(collectionTypeWords, StringUtils::capitalize);
        String collectionTypeTitle = OrchidExtensionsKt.to(collectionTypeWords, OrchidExtensionsKt::titleCase).trim();

        String[] collectionIdWords = OrchidExtensionsKt.from(collectionId, OrchidExtensionsKt::camelCase);
        collectionIdWords = OrchidExtensionsKt.with(collectionIdWords, StringUtils::capitalize);
        String collectionIdTitle = OrchidExtensionsKt.to(collectionIdWords, OrchidExtensionsKt::titleCase).trim();

        if(!EdenUtils.isEmpty(collectionIdTitle) && !collectionTypeTitle.equals(collectionIdTitle)) {
            return collectionTypeTitle + " - " + collectionIdTitle;
        }
        else {
            return collectionTypeTitle;
        }
    }
}
