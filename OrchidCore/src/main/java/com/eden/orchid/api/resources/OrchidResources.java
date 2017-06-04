package com.eden.orchid.api.resources;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.FileResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.name.Named;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private OkHttpClient client;

    private final String resourcesDir;

    @Inject
    public OrchidResources(
            OrchidContext context,
            @Named("resourcesDir") String resourcesDir,
            Set<LocalResourceSource> localResourceSources,
            Set<DefaultResourceSource> defaultResourceSources) {
        this.context = context;
        this.localResourceSources = new ObservableTreeSet<>(localResourceSources);
        this.defaultResourceSources = new ObservableTreeSet<>(defaultResourceSources);

        this.client = new OkHttpClient();
        this.resourcesDir = resourcesDir;
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
                .map(source -> source.getResourceEntry(fileName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a single OrchidResource from the directory declared by the 'resourcesDir' option. Themes and other
     * registered resource sources are not considered.
     *
     * @param fileName the file path and name to find
     * @return an OrchidResource if it can be found, null otherwise
     */
    public OrchidResource getThemeResourceEntry(final String fileName) {
        return context.getTheme().getResourceEntry(fileName);
    }

    /**
     * Gets a single OrchidResource. The 'resourcesDir' directory is first searched, and then all registered
     * ResourceSources (which include themes) in order of priority.
     *
     * @param fileName the file path and name to find
     * @return an OrchidResource if it can be found, null otherwise
     */
    public OrchidResource getResourceEntry(final String fileName) {
        // first check for a resource in any specified local resource sources
        OrchidResource resource = getLocalResourceEntry(fileName);

        // If nothing found in local resources, check the theme
        if (resource == null) {
            resource = getThemeResourceEntry(fileName);
        }

        // If nothing found in the theme, check the default resource sources
        if (resource == null) {
            resource = defaultResourceSources
                    .stream()
                    .map(source -> source.getResourceEntry(fileName))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }

        // return the resource if found, otherwise null
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
    public List<OrchidResource> getThemeResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        List<OrchidResourceSource> themeSources = new ArrayList<>();
        themeSources.add(context.getTheme());
        addEntries(entries, themeSources, path, fileExtensions, recursive);

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

        // add entries from local sources
        addEntries(entries, localResourceSources, path, fileExtensions, recursive);

        // add entries from theme
        List<OrchidResourceSource> themeSources = new ArrayList<>();
        themeSources.add(context.getTheme());
        addEntries(entries, themeSources, path, fileExtensions, recursive);

        // add entries from other sources
        addEntries(entries, defaultResourceSources, path, fileExtensions, recursive);

        return new ArrayList<>(entries.values());
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
                    String relative = OrchidUtils.getRelativeFilename(resource.getReference().getPath(), path);

                    String key = relative
                            + "/"
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

    public JSONObject loadAdditionalFile(String url) {
        if(!EdenUtils.isEmpty(url) && url.trim().startsWith("file://")) {
            return loadLocalFile(url.replaceAll("file://", ""));
        }
        else {
            return loadRemoteFile(url);
        }
    }

    public JSONObject loadLocalFile(String url) {
        try {
            File file = new File(url);
            String s = IOUtils.toString(new FileInputStream(file));

            JSONElement el = context.getTheme().parse("json", s);
            if(OrchidUtils.elementIsObject(el)) {
                return (JSONObject) el.getElement();
            }
        }
        catch (FileNotFoundException e) {
            // ignore files not being found
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject loadRemoteFile(String url) {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()) {
                return new JSONObject(response.body().string());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public OrchidResource findClosestFile(String filename) {
        return findClosestFile(filename, false);
    }

    public OrchidResource findClosestFile(String filename, boolean strict) {
        return findClosestFile(filename, strict, 10);
    }

    public OrchidResource findClosestFile(String filename, boolean strict, int maxIterations) {
        File folder = new File(resourcesDir);

        while (true) {
            if (folder.isDirectory()) {
                List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                for (File file : files) {
                    if(!strict) {
                        if (FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase(filename)) {
                            return new FileResource(context, file);
                        }
                    }
                    else {
                        if (file.getName().equals(filename)) {
                            return new FileResource(context, file);
                        }
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


        return null;
    }
}
