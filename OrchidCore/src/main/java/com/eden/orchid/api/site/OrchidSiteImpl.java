package com.eden.orchid.api.site;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.ImpliedKey;
import com.eden.orchid.api.options.annotations.ModularListConfig;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.name.Named;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.collections.MapsKt;
import org.json.JSONObject;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Description(value = "The global configurations for your Orchid site.", name = "Site")
@Archetype(value = ConfigArchetype.class, key = "site")
public final class OrchidSiteImpl implements OrchidSite {
    private OrchidContext context;
    private final String orchidVersion;
    private final String currentWorkingDirectory;
    private final String sourceDir;
    private final String destinationDir;
    private String version;
    private String environment;
    private String defaultTemplateExtension;
    @Option
    @Description("Basic, common information about your site, mostly for display and SEO purposes.")
    private SiteInfo about;

    @Option
    @ImpliedKey("value")
    private OrchidBaseUrls baseUrl;

    private String resolvedBaseUrl;

    @Inject
    public OrchidSiteImpl(
            String orchidVersion,
            @Named("version") String version,
            @Named("baseUrl") String baseUrl,
            @Named("environment") String environment,
            @Named("defaultTemplateExtension") String defaultTemplateExtension,
            @Named("src") String sourceDir,
            @Named("dest") String destinationDir
    ) {
        this.orchidVersion = orchidVersion;
        this.currentWorkingDirectory = OrchidUtils.normalizePath(Paths.get(".").toAbsolutePath().normalize().toString());
        this.version = version;
        this.environment = environment;
        this.defaultTemplateExtension = defaultTemplateExtension;
        this.sourceDir = sourceDir;
        this.destinationDir = destinationDir;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public boolean isDebug() {
        return !isProduction();
    }

    @Override
    public boolean isProduction() {
        return this.getEnvironment().equalsIgnoreCase("prod") || this.getEnvironment().equalsIgnoreCase("production");
    }

    @Override
    public SiteInfo getSiteInfo() {
        return about;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject site = new JSONObject();
        site.put("orchidVersion", orchidVersion);
        site.put("version", version);
        site.put("baseUrl", resolvedBaseUrl);
        site.put("environment", environment);
        return site;
    }

    @Override
    public String toString() {
        return this.toJSON().toString();
    }

    public String getOrchidVersion() {
        return this.orchidVersion;
    }

    public String getCurrentWorkingDirectory() {
        return this.currentWorkingDirectory;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getBaseUrl() {
        if (resolvedBaseUrl == null) {
            // always use the dev server URL if running a serve task
            baseUrl.add(MapsKt.mapOf(new Pair("type", "devServer"), new Pair("order", Integer.MIN_VALUE)));
            // always default to the defined base URL if nothing else is given
            baseUrl.add(MapsKt.mapOf(new Pair("type", "default"), new Pair("order", Integer.MAX_VALUE)));

            List<BaseUrlFactory> factories = baseUrl.get(context);
            for(BaseUrlFactory factory : factories) {
                if(factory.isEnabled(context)) {
                    resolvedBaseUrl = factory.getBaseUrl(context);
                    break;
                }
            }

            if (resolvedBaseUrl.equals("/")) {
                this.resolvedBaseUrl = resolvedBaseUrl;
            } else {
                this.resolvedBaseUrl = (resolvedBaseUrl.startsWith("/"))
                        ? "/" + OrchidUtils.normalizePath(resolvedBaseUrl)
                        : OrchidUtils.normalizePath(resolvedBaseUrl);
            }
            System.out.println();
        }
        return this.resolvedBaseUrl;
    }

    public String getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(final String environment) {
        this.environment = environment;
    }

    public String getDefaultTemplateExtension() {
        return this.defaultTemplateExtension;
    }

    public void setDefaultTemplateExtension(final String defaultTemplateExtension) {
        this.defaultTemplateExtension = defaultTemplateExtension;
    }

    public SiteInfo getAbout() {
        return this.about;
    }

    public void setAbout(final SiteInfo about) {
        this.about = about;
    }

    @Override
    public String getSourceDir() {
        return sourceDir;
    }

    @Override
    public String getDestinationDir() {
        return destinationDir;
    }

    public void setBaseUrl(OrchidBaseUrls baseUrl) {
        this.baseUrl = baseUrl;
    }
}
