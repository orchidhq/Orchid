package com.eden.orchid.resources;

import org.json.JSONObject;

import java.util.List;

public interface OrchidResources {

    void registerResourceSource(ResourceSource resourceSource);

    OrchidResource getResourceDirEntry(String fileName);

    OrchidResource getResourceEntry(String fileName);

    List<OrchidResource> getResourceDirEntries(String dirName, String[] fileExtensions, boolean recursive);

    List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive);

    OrchidResource getProjectReadme();

    OrchidResource getProjectLicense();

    boolean render(
            String template,
            String extension,
            boolean templateReference,
            JSONObject pageData,
            String alias,
            OrchidReference reference);
}
