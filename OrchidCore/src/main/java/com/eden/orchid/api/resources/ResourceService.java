package com.eden.orchid.api.resources;

import com.eden.orchid.api.resources.resource.OrchidResource;
import org.json.JSONObject;

import java.util.List;

public interface ResourceService {

    ResourceService getResourceService();

    default JSONObject getLocalDatafile(final String fileName) {
        return getResourceService().getLocalDatafile(fileName);
    }

    default JSONObject getLocalDatafiles(final String directory) {
        return getResourceService().getLocalDatafiles(directory);
    }

    default OrchidResource getLocalResourceEntry(final String fileName) {
        return getResourceService().getLocalResourceEntry(fileName);
    }

    default OrchidResource getThemeResourceEntry(final String fileName) {
        return getResourceService().getThemeResourceEntry(fileName);
    }

    default OrchidResource getResourceEntry(final String fileName) {
        return getResourceService().getResourceEntry(fileName);
    }

    default List<OrchidResource> getLocalResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        return getResourceService().getLocalResourceEntries(path, fileExtensions, recursive);
    }

    default List<OrchidResource> getThemeResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        return getResourceService().getThemeResourceEntries(path, fileExtensions, recursive);
    }

    default List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        return getResourceService().getResourceEntries(path, fileExtensions, recursive);
    }

    default JSONObject loadAdditionalFile(String url) {
        return getResourceService().loadAdditionalFile(url);
    }

    default JSONObject loadLocalFile(String url) {
        return getResourceService().loadLocalFile(url);
    }

    default JSONObject loadRemoteFile(String url) {
        return getResourceService().loadRemoteFile(url);
    }

    default OrchidResource findClosestFile(String filename) {
        return getResourceService().findClosestFile(filename);
    }

    default OrchidResource findClosestFile(String filename, boolean strict) {
        return getResourceService().findClosestFile(filename, strict);
    }

    default OrchidResource findClosestFile(String filename, boolean strict, int maxIterations) {
        return getResourceService().findClosestFile(filename, strict, maxIterations);
    }

}
