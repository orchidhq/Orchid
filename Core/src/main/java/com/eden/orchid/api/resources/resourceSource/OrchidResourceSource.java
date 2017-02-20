package com.eden.orchid.api.resources.resourceSource;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.resources.resource.OrchidResource;
import org.apache.commons.io.FilenameUtils;

import java.util.List;

/**
 * OrchidResourcesImpl in Orchid are hierarchical, and trying to find a Resource from the OrchidResouces will return the first
 * one it finds in the following order:
 *
 * 1) A file in the directory specified by -resourcesDir
 * 2) An entry from a OrchidResourceSource, with higher priority given preference.
 *
 * This is accomplished by finding the Jarfile in which a particular OrchidResourceSource is contained within, and then
 * looking for an entry in that jar matching a given path. Having a higher priority means the jar associated with this
 * OrchidResourceSource is searched before other Jars.
 */
public abstract class OrchidResourceSource extends Prioritized {

    public abstract OrchidResource getResourceEntry(String fileName);

    public abstract List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive);

    protected boolean shouldAddEntry(String entryName) {
        OrchidCompiler compiler = getTheme().compilerFor(FilenameUtils.getExtension(entryName));

        if (compiler != null && !EdenUtils.isEmpty(compiler.getIgnoredPatterns())) {
            String[] pieces = entryName.split("/");
            String entryFileName = pieces[pieces.length - 1];
            for (String pattern : compiler.getIgnoredPatterns()) {
                if (entryFileName.matches(pattern)) {
                    return false;
                }
            }
        }

        return true;
    }
}
