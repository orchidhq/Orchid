package com.eden.orchid.api.site;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.name.Named;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;

public final class OrchidSiteImpl implements OrchidSite {

    private OrchidContext context;

    @Getter private final String orchidVersion;
    @Getter @Setter private String version;
    @Getter private String baseUrl;
    @Getter @Setter private String environment;

    @Getter @Setter private String defaultTemplateExtension;

    @Inject
    public OrchidSiteImpl(
            String orchidVersion,
            @Named("v") String version,
            @Named("baseUrl") String baseUrl,
            @Named("environment") String environment,
            @Named("defaultTemplateExtension") String defaultTemplateExtension
    ) {
        this.orchidVersion = orchidVersion;
        this.version = version;
        this.baseUrl = OrchidUtils.normalizePath(baseUrl);
        this.environment = environment;
        this.defaultTemplateExtension = defaultTemplateExtension;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = OrchidUtils.normalizePath(baseUrl);
    }

    public boolean isDebug() { return !isProduction(); }
    public boolean isProduction() { return this.getEnvironment().equalsIgnoreCase("prod") || this.getEnvironment().equalsIgnoreCase("production"); }

    public JSONObject toJSON() {
        JSONObject site = new JSONObject();
        site.put("orchidVersion", orchidVersion);
        site.put("version", version);
        site.put("baseUrl", baseUrl);
        site.put("environment", environment);

        return site;
    }

    @Override
    public String toString() {
        return this.toJSON().toString();
    }
}
