package com.eden.orchid.api.resources;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource;
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource;
import com.eden.orchid.api.resources.resourcesource.ThemeResourceSource;
import com.eden.orchid.api.theme.Theme;
import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@ImplementedBy(ResourceServiceImpl.class)
public interface ResourceService extends OrchidService {

    default String[] getIgnoredFilenames() {
        return getService(ResourceService.class).getIgnoredFilenames();
    }

    default Map<String, Object> getDatafile(final String fileName) {
        return getService(ResourceService.class).getDatafile(fileName);
    }

    default Map<String, Object> getDatafiles(final String directory) {
        return getService(ResourceService.class).getDatafiles(directory);
    }

    default OrchidResource getResourceEntry(final String fileName, @Nullable OrchidResourceSource.Scope scopes) {
        return getService(ResourceService.class).getResourceEntry(fileName, scopes);
    }

    default OrchidResource getResourceEntry(Theme theme, final String fileName, @Nullable OrchidResourceSource.Scope scopes) {
        return getService(ResourceService.class).getResourceEntry(theme, fileName, scopes);
    }

    default List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive, @Nullable OrchidResourceSource.Scope scopes) {
        return getService(ResourceService.class).getResourceEntries(path, fileExtensions, recursive, scopes);
    }

    default Map<String, Object> loadAdditionalFile(String url) {
        return getService(ResourceService.class).loadAdditionalFile(url);
    }

    default @Nullable OrchidResource findClosestFile(String filename) {
        return getService(ResourceService.class).findClosestFile(filename);
    }

    default @Nullable OrchidResource findClosestFile(String filename, boolean strict) {
        return getService(ResourceService.class).findClosestFile(filename, strict);
    }

    default @Nullable OrchidResource findClosestFile(String filename, boolean strict, int maxIterations) {
        return getService(ResourceService.class).findClosestFile(filename, strict, maxIterations);
    }

    default @Nullable OrchidResource findClosestFile(String baseDir, String filename, boolean strict, int maxIterations) {
        return getService(ResourceService.class).findClosestFile(baseDir, filename, strict, maxIterations);
    }

    default OrchidResource locateLocalResourceEntry(final String fileName) {
        return getService(ResourceService.class).locateLocalResourceEntry(fileName);
    }

    default OrchidResource locateLocalResourceEntry(final String fileName, String[] fileExtensions) {
        return getService(ResourceService.class).locateLocalResourceEntry(fileName, fileExtensions);
    }

    default OrchidResource locateLocalResourceEntry(final String fileName, List<String> fileExtensions) {
        return getService(ResourceService.class).locateLocalResourceEntry(fileName, fileExtensions);
    }

    default OrchidResource locateTemplate(@Nullable Theme theme, final String fileNames) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames);
    }

    default OrchidResource locateTemplate(@Nullable Theme theme, final String fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames, ignoreMissing);
    }

    default OrchidResource locateTemplate(@Nullable Theme theme, final String[] fileNames) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames);
    }

    default OrchidResource locateTemplate(@Nullable Theme theme, final List<String> fileNames) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames);
    }

    default OrchidResource locateTemplate(@Nullable Theme theme, final String[] fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames, ignoreMissing);
    }

    default OrchidResource locateTemplate(@Nullable Theme theme, final List<String> fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames, ignoreMissing);
    }

}
