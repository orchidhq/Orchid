package com.eden.orchid.api.resources;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.resources.resource.FileResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resourcesource.CachingResourceSource;
import com.eden.orchid.api.resources.resourcesource.DataResourceSource;
import com.eden.orchid.api.resources.resourcesource.DefaultDataResourceSource;
import com.eden.orchid.api.resources.resourcesource.DefaultTemplateResourceSource;
import com.eden.orchid.api.resources.resourcesource.DelegatingResourceSource;
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource;
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource;
import com.eden.orchid.api.resources.resourcesource.TemplateResourceSource;
import com.eden.orchid.api.theme.AbstractTheme;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.CacheKt;
import com.eden.orchid.utilities.LRUCache;
import com.eden.orchid.utilities.OrchidUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
@Description(value = "How Orchid locates resources.", name = "Resources")
@Archetype(value = ConfigArchetype.class, key = "services.resources")
public final class ResourceServiceImpl implements ResourceService, OrchidEventListener {
    private OrchidContext context;
    private final List<OrchidResourceSource> resourceSources;
    private final LRUCache<CachingResourceSource.CacheKey, OrchidResource> resourceCache;

    @Option
    @StringDefault({".DS_store", ".localized"})
    @Description("A list of filenames to globally filter out files from being sourced. Should be used primarily for ignoring pesky hidden or system files that are not intended to be used as site content.")
    private String[] ignoredFilenames;

    @Inject
    public ResourceServiceImpl(Set<OrchidResourceSource> resourceSources) {
        this.resourceSources = resourceSources.stream().sorted().collect(Collectors.toList());
        this.resourceCache = new LRUCache<>();
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

// Get all matching resources
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public OrchidResourceSource getDefaultResourceSource(@Nullable OrchidResourceSource.Scope scopes, @Nullable AbstractTheme theme) {
        List<OrchidResourceSource> allSources = new ArrayList<>(resourceSources);
        if(theme != null) {
            allSources.add(theme.getResourceSource());
        }

        List<OrchidResourceSource.Scope> validScopes = new ArrayList<>();
        if (scopes != null) {
            validScopes.add(scopes);
        }

        return new CachingResourceSource(
                resourceCache,
                theme,
                new DelegatingResourceSource(
                        allSources,
                        validScopes,
                        0,
                        LocalResourceSource.INSTANCE
                ),
                validScopes
        );
    }

    @Override
    public TemplateResourceSource getTemplateResourceSource(@Nullable OrchidResourceSource.Scope scopes, @NonNull AbstractTheme theme) {
        return new DefaultTemplateResourceSource(
                theme,
                getDefaultResourceSource(scopes, theme)
        );
    }

    @Override
    public DataResourceSource getDataResourceSource(@Nullable OrchidResourceSource.Scope scopes) {
        return new DefaultDataResourceSource(
                getDefaultResourceSource(scopes, null)
        );
    }

    // Find closest file
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public @Nullable OrchidResource findClosestFile(String filename) {
        return findClosestFile(filename, false);
    }

    @Override
    public @Nullable OrchidResource findClosestFile(String filename, boolean strict) {
        return findClosestFile(filename, strict, 10);
    }

    @Override
    public @Nullable OrchidResource findClosestFile(String filename, boolean strict, int maxIterations) {
        return findClosestFile(null, filename, strict, maxIterations);
    }

    @Override
    public @Nullable OrchidResource findClosestFile(String baseDir, String filename, boolean strict, int maxIterations) {
        if (EdenUtils.isEmpty(baseDir)) {
            baseDir = context.getSourceDir();
        }
        File folder = new File(baseDir);

        while (true) {
            if (folder.isDirectory()) {
                List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));
                for (File file : files) {
                    if (!strict) {
                        if (FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase(filename)) {
                            return new FileResource(
                                    new OrchidReference(context, FileResource.Companion.pathFromFile(file, context.getSourceDir())),
                                    file
                            );
                        }
                    } else {
                        if (file.getName().equals(filename)) {
                            return new FileResource(
                                    new OrchidReference(context, FileResource.Companion.pathFromFile(file, context.getSourceDir())),
                                    file
                            );
                        }
                    }
                }
            }
            // set the folder to its own parent and search again
            if (folder.getParentFile() != null && maxIterations > 0) {
                folder = folder.getParentFile();
                maxIterations--;
            } else
            // there is no more parent to search, exit the loop
            {
                break;
            }
        }
        return null;
    }

// Find first matching resource
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public OrchidResource locateLocalResourceEntry(final String fileName) {
        return locateLocalResourceEntry(fileName, new ArrayList<>(context.getCompilerExtensions()));
    }

    @Override
    public OrchidResource locateLocalResourceEntry(final String fileName, String[] fileExtensions) {
        List<String> extensions = new ArrayList<>();
        Collections.addAll(extensions, fileExtensions);
        return locateLocalResourceEntry(fileName, extensions);
    }

    @Override
    public OrchidResource locateLocalResourceEntry(final String fileName, List<String> fileExtensions) {
        String fullFileName = OrchidUtils.normalizePath(fileName);
        if (!fullFileName.contains(".")) {
            for (String extension : fileExtensions) {
                String testFileName = fullFileName + "." + extension;
                OrchidResource resource = context.getDefaultResourceSource(LocalResourceSource.INSTANCE, null).getResourceEntry(context, testFileName);
                if (resource != null) {
                    return resource;
                }
            }
        }
        return context.getDefaultResourceSource(LocalResourceSource.INSTANCE, null).getResourceEntry(context, fullFileName);
    }

// Delombok
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String[] getIgnoredFilenames() {
        return ignoredFilenames;
    }

    public void setIgnoredFilenames(String[] ignoredFilenames) {
        this.ignoredFilenames = ignoredFilenames;
    }

// Cache Implementation
//----------------------------------------------------------------------------------------------------------------------

    public static class ResourceCacheKey {
        private final String resourceName;
        @Nullable
        private final OrchidResourceSource.Scope scope;
        private final String themeKey;
        private final int themeHashcode;

        public ResourceCacheKey(final String resourceName, @Nullable final OrchidResourceSource.Scope scope, @Nullable final Theme theme) {
            this.resourceName = resourceName;
            this.scope = scope;
            if(theme != null) {
                this.themeKey = theme.getKey();
                this.themeHashcode = theme.hashCode();
            }
            else {
                this.themeKey = "";
                this.themeHashcode = 0;
            }
        }

        public String getResourceName() {
            return this.resourceName;
        }

        @Nullable
        public OrchidResourceSource.Scope getScope() {
            return this.scope;
        }

        public String getThemeKey() {
            return this.themeKey;
        }

        public int getThemeHashcode() {
            return this.themeHashcode;
        }

        @Override
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof ResourceServiceImpl.ResourceCacheKey)) return false;
            final ResourceServiceImpl.ResourceCacheKey other = (ResourceServiceImpl.ResourceCacheKey) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$resourceName = this.getResourceName();
            final java.lang.Object other$resourceName = other.getResourceName();
            if (this$resourceName == null ? other$resourceName != null : !this$resourceName.equals(other$resourceName)) return false;
            final java.lang.Object this$scope = this.getScope();
            final java.lang.Object other$scope = other.getScope();
            if (this$scope == null ? other$scope != null : !this$scope.equals(other$scope)) return false;
            final java.lang.Object this$themeKey = this.getThemeKey();
            final java.lang.Object other$themeKey = other.getThemeKey();
            if (this$themeKey == null ? other$themeKey != null : !this$themeKey.equals(other$themeKey)) return false;
            if (this.getThemeHashcode() != other.getThemeHashcode()) return false;
            return true;
        }

        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof ResourceServiceImpl.ResourceCacheKey;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $resourceName = this.getResourceName();
            result = result * PRIME + ($resourceName == null ? 43 : $resourceName.hashCode());
            final java.lang.Object $scope = this.getScope();
            result = result * PRIME + ($scope == null ? 43 : $scope.hashCode());
            final java.lang.Object $themeKey = this.getThemeKey();
            result = result * PRIME + ($themeKey == null ? 43 : $themeKey.hashCode());
            result = result * PRIME + this.getThemeHashcode();
            return result;
        }

        @Override
        public java.lang.String toString() {
            return "ResourceServiceImpl.ResourceCacheKey(resourceName=" + this.getResourceName() + ", scope=" + this.getScope() + ", themeKey=" + this.getThemeKey() + ", themeHashcode=" + this.getThemeHashcode() + ")";
        }
    }

    @On(Orchid.Lifecycle.ClearCache.class)
    public void onClearCache(Orchid.Lifecycle.ClearCache event) {
        resourceCache.clear();
    }
}
