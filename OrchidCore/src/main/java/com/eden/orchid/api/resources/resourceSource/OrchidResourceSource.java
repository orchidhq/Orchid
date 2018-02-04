package com.eden.orchid.api.resources.resourceSource;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.resources.resource.OrchidResource;

import javax.inject.Inject;
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
public abstract class OrchidResourceSource extends Prioritized {

    protected final OrchidContext context;

    @Inject
    public OrchidResourceSource(OrchidContext context, int priority) {
        super(priority);
        this.context = context;
    }

    public abstract OrchidResource getResourceEntry(String fileName);

    public abstract List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive);

    protected boolean shouldAddEntry(String entryName) {
        return true;
    }
}
