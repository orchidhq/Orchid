package com.eden.orchid.api.resources;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidParser;
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
public final class ResourceServiceImpl implements ResourceService {

    private OrchidContext context;
    private Set<LocalResourceSource> localResourceSources;
    private Set<DefaultResourceSource> defaultResourceSources;
    private OkHttpClient client;

    private final String resourcesDir;

    @Inject
    public ResourceServiceImpl(
            @Named("resourcesDir") String resourcesDir,
            Set<LocalResourceSource> localResourceSources,
            Set<DefaultResourceSource> defaultResourceSources) {

        this.localResourceSources = new ObservableTreeSet<>(localResourceSources);
        this.defaultResourceSources = new ObservableTreeSet<>(defaultResourceSources);

        this.client = new OkHttpClient();
        this.resourcesDir = resourcesDir;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public JSONObject getLocalDatafile(final String fileName) {
        return context.getParserExtensions().stream()
                .map(ext -> {
                    OrchidResource resource = getLocalResourceEntry(fileName + "." + ext);
                    if (resource != null) {
                        String content = resource.getContent();

                        if (!EdenUtils.isEmpty(content)) {
                            return context.parse(ext, content);
                        }
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public JSONObject getLocalDatafiles(final String directory) {
        String[] parserExtensions = new String[context.getParserExtensions().size()];
        context.getParserExtensions().toArray(parserExtensions);
        List<OrchidResource> files = getLocalResourceEntries(directory, parserExtensions, false);

        JSONObject allDatafiles = new JSONObject();

        for (OrchidResource file : files) {
            file.getReference().setUsePrettyUrl(false);
            JSONObject fileData = context.parse(file.getReference().getExtension(), file.getContent());

            if (fileData != null) {
                if (fileData.has(OrchidParser.arrayAsObjectKey) && fileData.keySet().size() == 1) {
                    allDatafiles.put(file.getReference().getFileName(), fileData.getJSONArray(OrchidParser.arrayAsObjectKey));
                }
                else {
                    allDatafiles.put(file.getReference().getFileName(), fileData);
                }
            }

        }

        return allDatafiles;
    }

    @Override
    public OrchidResource getLocalResourceEntry(final String fileName) {
        return localResourceSources
                .stream()
                .map(source -> source.getResourceEntry(fileName))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public OrchidResource getThemeResourceEntry(final String fileName) {
        return context.getTheme().getResourceEntry(fileName);
    }

    @Override
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

    @Override
    public List<OrchidResource> getLocalResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        addEntries(entries, localResourceSources, path, fileExtensions, recursive);

        return new ArrayList<>(entries.values());
    }

    @Override
    public List<OrchidResource> getThemeResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        List<OrchidResourceSource> themeSources = new ArrayList<>();
        themeSources.add(context.getTheme());
        addEntries(entries, themeSources, path, fileExtensions, recursive);

        return new ArrayList<>(entries.values());
    }

    @Override
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

    @Override
    public JSONObject loadAdditionalFile(String url) {
        if (!EdenUtils.isEmpty(url) && url.trim().startsWith("file://")) {
            return loadLocalFile(url.replaceAll("file://", ""));
        }
        else {
            return loadRemoteFile(url);
        }
    }

    @Override
    public JSONObject loadLocalFile(String url) {
        try {
            File file = new File(url);
            String s = IOUtils.toString(new FileInputStream(file));
            return context.parse("json", s);
        }
        catch (FileNotFoundException e) {
            // ignore files not being found
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public JSONObject loadRemoteFile(String url) {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                return new JSONObject(response.body().string());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public OrchidResource findClosestFile(String filename) {
        return findClosestFile(filename, false);
    }

    @Override
    public OrchidResource findClosestFile(String filename, boolean strict) {
        return findClosestFile(filename, strict, 10);
    }

    @Override
    public OrchidResource findClosestFile(String filename, boolean strict, int maxIterations) {
        File folder = new File(resourcesDir);

        while (true) {
            if (folder.isDirectory()) {
                List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                for (File file : files) {
                    if (!strict) {
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
