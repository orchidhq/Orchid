package com.eden.orchid.resources;

import com.eden.orchid.Orchid;
import org.json.JSONObject;

import java.util.List;

public class OrchidResources {

    public static void registerResourceSource(ResourceSource resourceSource) {
        Orchid.getResources().registerResourceSource(resourceSource);
    }

    public static OrchidResource getResourceDirEntry(String fileName) {
        return Orchid.getResources().getResourceDirEntry(fileName);
    }

    public static OrchidResource getResourceEntry(String fileName) {
        return Orchid.getResources().getResourceEntry(fileName);
    }

    public static List<OrchidResource> getResourceDirEntries(String dirName, String[] fileExtensions, boolean recursive) {
        return Orchid.getResources().getResourceDirEntries(dirName, fileExtensions, recursive);
    }

    public static List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive) {
        return Orchid.getResources().getResourceEntries(dirName, fileExtensions, recursive);
    }

    public static OrchidResource getProjectReadme() {
        return Orchid.getResources().getProjectReadme();
    }

    public static OrchidResource getProjectLicense() {
        return Orchid.getResources().getProjectLicense();
    }

    public static boolean render(
            String template,
            String extension,
            boolean templateReference,
            JSONObject pageData,
            String alias,
            OrchidReference reference) {
        return Orchid.getResources().render(template, extension, templateReference, pageData, alias, reference);
    }
}
