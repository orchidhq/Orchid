package com.eden.orchid.resources;

import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.utilities.RegistrationProvider;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class OrchidResources implements RegistrationProvider {
    private Map<Integer, ResourceSource> resourceSources = new TreeMap<>(Collections.reverseOrder());

    /**
     * Gets a single OrchidResource from the directory declared by the 'resourcesDir' option. Themes and other
     * registered resource sources are not considered.
     *
     * @param fileName the file path and name to find
     * @return an OrchidResource if it can be found, null otherwise
     */
    public abstract OrchidResource getResourceDirEntry(String fileName);

    /**
     * Gets a single OrchidResource. The 'resourcesDir' directory is first searched, and then all registered
     * ResourceSources (which include themes) in order of priority.
     *
     * @param fileName the file path and name to find
     * @return an OrchidResource if it can be found, null otherwise
     */
    public abstract OrchidResource getResourceEntry(String fileName);

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
    public abstract List<OrchidResource> getResourceDirEntries(String dirName, String[] fileExtensions, boolean recursive);

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
    public abstract List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive);

    /**
     * Find the projects README file
     *
     * @return an OrchidResource containing the README content if it was found, null otherwise
     */
    public abstract OrchidResource getProjectReadme();

    /**
     * Find the projects License file
     *
     * @return an OrchidResource containing the License content if it was found, null otherwise
     */
    public abstract OrchidResource getProjectLicense();

    public abstract boolean render(
            String template,
            String extension,
            boolean templateReference,
            JSONObject pageData,
            String alias,
            OrchidReference reference);

    public Map<Integer, ResourceSource> getResourceSources() {
        return resourceSources;
    }

    public void setResourceSources(Map<Integer, ResourceSource> resourceSources) {
        this.resourceSources = resourceSources;
    }

    public void register(Object object) {
        if(object instanceof ResourceSource) {
            ResourceSource resourceSource = (ResourceSource) object;

            int priority = resourceSource.getResourcePriority();
            while(resourceSources.containsKey(priority)) {
                priority--;
            }

            resourceSources.put(priority, resourceSource);
        }
    }

    public void reorderResourceSources() {
        Theme theme = Orchid.getTheme();

        reorderThemes();

        List<ResourceSource> sources = new ArrayList<>();

        for(Map.Entry<Integer, ResourceSource> source : resourceSources.entrySet()) {
            if(source.getValue() instanceof Theme) {
                if(!source.getValue().getClass().isAssignableFrom(theme.getClass())) {
                    source.getValue().setResourcePriority(-1);
                }
            }

            sources.add(source.getValue());
        }

        resourceSources = new TreeMap<>(Collections.reverseOrder());

        for(ResourceSource resourceSource : sources) {
            register(resourceSource);
        }
    }

    private void reorderThemes() {
        Class<?> superclass = Orchid.getTheme().getClass();
        int i = 0;

        // find the highest priority of any Theme
        int highestThemePriority = 0;
        for(Map.Entry<Integer, ResourceSource> resourceSourceEntry : resourceSources.entrySet()) {
            if (resourceSourceEntry.getValue() instanceof Theme) {
                highestThemePriority = Math.max(highestThemePriority, resourceSourceEntry.getValue().getResourcePriority());
            }
        }

        // Go through all Themes and set each parent theme as the next-highest Theme priority
        while(!superclass.equals(Theme.class)) {
            for(Map.Entry<Integer, ResourceSource> resourceSourceEntry : resourceSources.entrySet()) {
                if(resourceSourceEntry.getValue() instanceof Theme) {
                    Theme theme = (Theme) resourceSourceEntry.getValue();
                    if (theme.getClass().equals(superclass)) {
                        theme.setResourcePriority((highestThemePriority) - i);
                        break;
                    }
                }
            }

            i++;
            superclass = superclass.getSuperclass();
        }
    }
}
