package com.eden.orchid.impl.resources;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.resources.OrchidReference;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Resources implements OrchidResources {

    private Set<LocalResourceSource> localResourceSources;
    private Set<DefaultResourceSource> defaultResourceSources;

    @Inject
    public Resources() {

    }

    @Override
    public OrchidResource getLocalResourceEntry(String fileName) {
        checkSources();
        for(LocalResourceSource localResourceSource : localResourceSources) {
            OrchidResource resource = localResourceSource.getResourceEntry(fileName);

            if(resource != null) {
                return resource;
            }
        }

        return null;
    }

    @Override
    public OrchidResource getResourceEntry(String fileName) {
        checkSources();
        OrchidResource resource = getLocalResourceEntry(fileName);

        if(resource != null) {
            return resource;
        }
        else {
            for(DefaultResourceSource defaultResourceSource : defaultResourceSources) {
                resource = defaultResourceSource.getResourceEntry(fileName);

                if(resource != null) {
                    return resource;
                }
            }

            return null;
        }
    }

    @Override
    public List<OrchidResource> getLocalResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        checkSources();
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        addEntries(entries, localResourceSources, path, fileExtensions, recursive);

        return new ArrayList<>(entries.values());
    }

    @Override
    public List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        checkSources();
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        addEntries(entries, localResourceSources, path, fileExtensions, recursive);
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

        for(OrchidResourceSource resourceSource : sources) {
            if (resourceSource.getPriority() < 0) {
                continue;
            }

            List<OrchidResource> resources = resourceSource.getResourceEntries(path, fileExtensions, recursive);

            if(resources != null) {
                for(OrchidResource resource : resources) {

                    String relative = OrchidUtils.getRelativeFilename(resource.getReference().getFullPath(), path);

                    String key = relative
                            + File.separator
                            + resource.getReference().getFileName()
                            + "."
                            + resource.getReference().getOutputExtension();

                    if (entries.containsKey(key)) {
                        if (resource.getPriority() > entries.get(relative).getPriority()) {
                            entries.put(key, resource);
                        }
                    }
                    else {
                        entries.put(key, resource);
                    }
                }
            }
        }
    }

    private void checkSources() {
        if (localResourceSources == null) {
            localResourceSources = new ObservableTreeSet<>(OrchidUtils.resolveSet(LocalResourceSource.class));
        }

        if (defaultResourceSources == null) {
            defaultResourceSources = new ObservableTreeSet<>(OrchidUtils.resolveSet(DefaultResourceSource.class));
        }
    }

    public OrchidResource getProjectReadme() {
        if (!EdenUtils.isEmpty(Orchid.getContext().query("options.resourcesDir"))) {
            String resourceDir = Orchid.getContext().query("options.resourcesDir").toString();

            File folder = new File(resourceDir);

            // set hard limit of searching no more than 10 parent directories for the README
            int maxIterations = 10;

            while (true) {
                if (folder.isDirectory()) {
                    List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                    for (File file : files) {
                        if (FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase("readme")) {
                            Clog.i("Found README file: #{$1}", new Object[]{file.getAbsolutePath()});
                            return new FileResource(file);
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

    public OrchidResource getProjectLicense() {
        if (!EdenUtils.isEmpty(Orchid.getContext().query("options.resourcesDir"))) {
            String resourceDir = Orchid.getContext().query("options.resourcesDir").toString();

            File folder = new File(resourceDir);

            // set hard limit of searching no more than 10 parent directories for the README
            int maxIterations = 10;

            while (true) {
                if (folder.isDirectory()) {
                    List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                    for (File file : files) {
                        if (FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase("license")) {
                            Clog.i("Found License file: #{$1}", new Object[]{file.getAbsolutePath()});

                            return new FileResource(file);
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

    public boolean render(
            String template,
            String extension,
            boolean templateReference,
            JSONObject pageData,
            String alias,
            OrchidReference reference) {
        String templateContent = "";
        if (templateReference) {
            OrchidResource templateResource = getResourceEntry(template);

            if (templateResource == null) {
                templateResource = getResourceEntry("templates/pages/index.twig");
            }
            if (templateResource == null) {
                templateResource = getResourceEntry("templates/pages/index.html");
            }
            if (templateResource == null) {
                return false;
            }

            templateContent = templateResource.getContent();
        }
        else {
            templateContent = (EdenUtils.isEmpty(template)) ? "" : template;
        }

        JSONObject templateVariables = new JSONObject(getContext().getRoot().toMap());
        templateVariables.put("page", pageData);
        if (!EdenUtils.isEmpty(alias)) {
            templateVariables.put(alias, pageData);
        }

        String content = getContext().getTheme().compile(extension, templateContent, templateVariables);

        if (content == null) {
            Clog.v("#{$1} compiled to null", new Object[]{reference.toString()});
            content = "";
        }

        String outputPath = reference.getFullPath();
        String outputName = reference.getFileName() + "." + reference.getOutputExtension();

        outputPath = Orchid.getContext().query("options.d").getElement().toString() + File.separator + outputPath.replaceAll("/", File.separator);

        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(outputPath + File.separator + outputName);
            Files.write(classesFile, content.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
