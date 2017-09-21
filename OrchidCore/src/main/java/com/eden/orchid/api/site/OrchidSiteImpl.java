package com.eden.orchid.api.site;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.name.Named;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class OrchidSiteImpl implements OrchidSite {

    private OrchidContext context;

    @Getter private final String orchidVersion;
    @Getter @Setter private String version;
    @Getter @Setter private String baseUrl;
    @Getter @Setter private String environment;

    @Inject
    public OrchidSiteImpl(String orchidVersion, @Named("v") String version, @Named("baseUrl") String baseUrl, @Named("environment") String environment) {
        this.orchidVersion = orchidVersion;
        this.version = version;
        this.baseUrl = OrchidUtils.normalizePath(baseUrl);
        this.environment = environment;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = OrchidUtils.normalizePath(baseUrl);
    }
}
