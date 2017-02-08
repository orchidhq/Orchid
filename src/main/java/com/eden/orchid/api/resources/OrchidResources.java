package com.eden.orchid.api.resources;

import com.eden.orchid.api.registration.Contextual;
import org.json.JSONObject;

import java.util.List;

public interface OrchidResources extends Contextual {

    /**
     * Gets a single OrchidResource from the directory declared by the 'resourcesDir' option. Themes and other
     * registered resource sources are not considered.
     *
     * @param fileName the file path and name to find
     * @return an OrchidResource if it can be found, null otherwise
     */
    OrchidResource getResourceDirEntry(String fileName);

    /**
     * Gets a single OrchidResource. The 'resourcesDir' directory is first searched, and then all registered
     * ResourceSources (which include themes) in order of priority.
     *
     * @param fileName the file path and name to find
     * @return an OrchidResource if it can be found, null otherwise
     */
    OrchidResource getResourceEntry(String fileName);

    /**
     * Finds all OrchidResources in a given directory in the 'resources directory' that contain one of the declared file
     * extensions. Themes and other registered resource sources are not considered. If no extensions are specified, all
     * files in the given directory are returned. If recursive is true, the declared directory and all subdirectories
     * are searched instead of just the declared directory.
     *
     * @param dirName the path to search in
     * @param fileExtensions a list of extensions to match files on (optional)
     * @param recursive whether to also search subdirectories
     * @return a list of all OrchidResources found
     */
    List<OrchidResource> getResourceDirEntries(String dirName, String[] fileExtensions, boolean recursive);

    /**
     * Finds all OrchidResources in a given directory in all registered ResourceSources. The 'resourcesDir' directory is
     * first searched, and then all registered ResourceSources (which include themes) in order of priority. If no
     * extensions are specified, all files in the given directory are returned. If recursive is true, the declared
     * directory and all subdirectories are searched instead of just the declared directory.
     *
     * @param dirName the path to search in
     * @param fileExtensions a list of extensions to match files on (optional)
     * @param recursive whether to also search subdirectories
     * @return a list of all OrchidResources found
     */
    List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive);

    /**
     * Find the projects README file
     *
     * @return an OrchidResource containing the README content if it was found, null otherwise
     */
    OrchidResource getProjectReadme();

    /**
     * Find the projects License file
     *
     * @return an OrchidResource containing the License content if it was found, null otherwise
     */
    OrchidResource getProjectLicense();

    boolean render(
            String template,
            String extension,
            boolean templateReference,
            JSONObject pageData,
            String alias,
            OrchidReference reference);
}
