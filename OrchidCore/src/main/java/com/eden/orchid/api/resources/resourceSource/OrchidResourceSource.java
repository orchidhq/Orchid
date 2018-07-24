package com.eden.orchid.api.resources.resourceSource;

import com.eden.orchid.api.resources.resource.OrchidResource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * OrchidResourceSource define the resource lookup order. Resources are looked up in the following order:
 *
 * 1) Local sources
 * 2) The currently active theme
 * 3) Plugin sources
 *
 * This makes it so that any resource defined in a plugin or theme can always be overridden by your local resoure
 * sources. Likewise, any resource defined in a plugin can be overridden by the theme.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public interface OrchidResourceSource extends Comparable<OrchidResourceSource> {

    int getPriority();

    OrchidResource getResourceEntry(String fileName);

    List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive);

    default boolean shouldAddEntry(String entryName) {
        return true;
    }

    default int compareTo(@NotNull OrchidResourceSource o) {
        return getPriority() - getPriority();
    }

}
