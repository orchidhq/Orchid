package com.eden.orchid.api.resources;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.FileResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

@Singleton
public final class OrchidResources {

    private OrchidContext context;
    private Set<LocalResourceSource> localResourceSources;
    private Set<DefaultResourceSource> defaultResourceSources;

    @Inject
    public OrchidResources(OrchidContext context, Set<LocalResourceSource> localResourceSources, Set<DefaultResourceSource> defaultResourceSources) {
        this.context = context;
        this.localResourceSources = new ObservableTreeSet<>(localResourceSources);
        this.defaultResourceSources = new ObservableTreeSet<>(defaultResourceSources);
    }

    /**
     * Gets a single OrchidResource from the directory declared by the 'resourcesDir' option. Themes and other
     * registered resource sources are not considered.
     *
     * @param fileName the file path and name to find
     * @return an OrchidResource if it can be found, null otherwise
     */
    public OrchidResource getLocalResourceEntry(final String fileName) {
        return localResourceSources
                .stream()
                .filter(source -> source.getPriority() >= 0)
                .map(source -> source.getResourceEntry(fileName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> null);
    }

    /**
     * Gets a single OrchidResource. The 'resourcesDir' directory is first searched, and then all registered
     * ResourceSources (which include themes) in order of priority.
     *
     * @param fileName the file path and name to find
     * @return an OrchidResource if it can be found, null otherwise
     */
    public OrchidResource getResourceEntry(final String fileName) {
        OrchidResource resource = getLocalResourceEntry(fileName);

        if (resource == null) {
            resource = defaultResourceSources
                    .stream()
                    .filter(source -> source.getPriority() >= 0)
                    .map(source -> source.getResourceEntry(fileName))
                    .filter(Objects::nonNull)
                    .findFirst().orElseGet(() -> null);
        }

        return resource;
    }

    /**
     * Finds all OrchidResources in a given directory in the 'resources directory' that contain one of the declared file
     * extensions. Themes and other registered resource sources are not considered. If no extensions are specified, all
     * files in the given directory are returned. If recursive is true, the declared directory and all subdirectories
     * are searched instead of just the declared directory.
     *
     * @param path           the path to search in
     * @param fileExtensions a list of extensions to match files on (optional)
     * @param recursive      whether to also search subdirectories
     * @return a list of all OrchidResources found
     */
    public List<OrchidResource> getLocalResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        addEntries(entries, localResourceSources, path, fileExtensions, recursive);

        return new ArrayList<>(entries.values());
    }

    /**
     * Finds all OrchidResources in a given directory in all registered ResourceSources. The 'resourcesDir' directory is
     * first searched, and then all registered ResourceSources (which include themes) in order of priority. If no
     * extensions are specified, all files in the given directory are returned. If recursive is true, the declared
     * directory and all subdirectories are searched instead of just the declared directory.
     *
     * @param path           the path to search in
     * @param fileExtensions a list of extensions to match files on (optional)
     * @param recursive      whether to also search subdirectories
     * @return a list of all OrchidResources found
     */
    public List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        addEntries(entries, localResourceSources, path, fileExtensions, recursive);
        addEntries(entries, defaultResourceSources, path, fileExtensions, recursive);

        return new ArrayList<>(entries.values());
    }

    /**
     * Find the projects README file
     *
     * @return an OrchidResource containing the README content if it was found, null otherwise
     */
    public OrchidResource getProjectReadme() {
        if (!EdenUtils.isEmpty(context.query("options.resourcesDir"))) {
            String resourceDir = context.query("options.resourcesDir").toString();

            File folder = new File(resourceDir);

            // set hard limit of searching no more than 10 parent directories for the README
            int maxIterations = 10;

            while (true) {
                if (folder.isDirectory()) {
                    List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                    for (File file : files) {
                        if (FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase("readme")) {
                            return new FileResource(context, file);
                        }
                    }
                }

                // set the folder to its own parent and search again
                if (folder.getParentFile() != null && maxIterations > 0) {
                    folder = folder.getParentFile();
                    maxIterations--;
                }

                // there is no more parent to search, exit the loop
                else {
                    break;
                }
            }
        }

        return null;
    }

    /**
     * Find the projects License file
     *
     * @return an OrchidResource containing the License content if it was found, null otherwise
     */
    public OrchidResource getProjectLicense() {
        if (!EdenUtils.isEmpty(context.query("options.resourcesDir"))) {
            String resourceDir = context.query("options.resourcesDir").toString();

            File folder = new File(resourceDir);

            // set hard limit of searching no more than 10 parent directories for the README
            int maxIterations = 10;

            while (true) {
                if (folder.isDirectory()) {
                    List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                    for (File file : files) {
                        if (FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase("license")) {
                            return new FileResource(context, file);
                        }
                    }
                }

                // set the folder to its own parent and search again
                if (folder.getParentFile() != null && maxIterations > 0) {
                    folder = folder.getParentFile();
                    maxIterations--;
                }

                // there is no more parent to search, exit the loop
                else {
                    break;
                }
            }
        }

        return null;
    }

    private void addEntries(
            TreeMap<String, OrchidResource> entries,
            Collection<? extends OrchidResourceSource> sources,
            String path,
            String[] fileExtensions,
            boolean recursive
    ) {

        sources
                .stream()
                .filter(source -> source.getPriority() >= 0)
                .map(source -> source.getResourceEntries(path, fileExtensions, recursive))
                .filter(OrchidUtils.not(EdenUtils::isEmpty))
                .flatMap(Collection::stream)
                .forEach(resource -> {
                    String relative = OrchidUtils.getRelativeFilename(resource.getReference().getFullPath(), path);

                    String key = relative
                            + File.separator
                            + resource.getReference().getFileName()
                            + "."
                            + resource.getReference().getOutputExtension();

                    if (entries.containsKey(key)) {
                        if (resource.getPriority() > entries.get(key).getPriority()) {
                            entries.put(key, resource);
                        }
                    }
                    else {
                        entries.put(key, resource);
                    }
                });
    }
}
