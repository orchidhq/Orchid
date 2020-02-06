package com.eden.orchid.api.resources;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource;
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource;
import com.eden.orchid.api.resources.resourcesource.ThemeResourceSource;
import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @since v1.0.0
 * @orchidApi services
 */
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

    default List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive, @Nullable OrchidResourceSource.Scope scopes) {
        return getService(ResourceService.class).getResourceEntries(path, fileExtensions, recursive, scopes);
    }

    @Deprecated
    default OrchidResource getLocalResourceEntry(final String fileName) {
        return getService(ResourceService.class).getResourceEntry(fileName, LocalResourceSource.INSTANCE);
    }

    @Deprecated
    default OrchidResource getThemeResourceEntry(final String fileName) {
        return getService(ResourceService.class).getResourceEntry(fileName, ThemeResourceSource.INSTANCE);
    }

    @Deprecated
    default OrchidResource getResourceEntry(final String fileName) {
        return getService(ResourceService.class).getResourceEntry(fileName, null);
    }

    @Deprecated
    default List<OrchidResource> getLocalResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        return getService(ResourceService.class).getResourceEntries(path, fileExtensions, recursive, LocalResourceSource.INSTANCE);
    }

    @Deprecated
    default List<OrchidResource> getThemeResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        return getService(ResourceService.class).getResourceEntries(path, fileExtensions, recursive, ThemeResourceSource.INSTANCE);
    }

    @Deprecated
    default List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        return getService(ResourceService.class).getResourceEntries(path, fileExtensions, recursive, null);
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

    default OrchidResource locateTemplate(final String fileNames) {
        return getService(ResourceService.class).locateTemplate(fileNames);
    }

    default OrchidResource locateTemplate(final String fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(fileNames, ignoreMissing);
    }

    default OrchidResource locateTemplate(final String[] fileNames) {
        return getService(ResourceService.class).locateTemplate(fileNames);
    }

    default OrchidResource locateTemplate(final List<String> fileNames) {
        return getService(ResourceService.class).locateTemplate(fileNames);
    }

    default OrchidResource locateTemplate(final String[] fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(fileNames, ignoreMissing);
    }

    default OrchidResource locateTemplate(final List<String> fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(fileNames, ignoreMissing);
    }

}
