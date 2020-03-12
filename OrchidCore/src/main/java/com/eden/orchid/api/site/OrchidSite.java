package com.eden.orchid.api.site;

import com.eden.orchid.api.OrchidService;
import com.google.inject.ImplementedBy;
import org.json.JSONObject;

@ImplementedBy(OrchidSiteImpl.class)
public interface OrchidSite extends OrchidService {

    default String getOrchidVersion() {
        return getService(OrchidSite.class).getOrchidVersion();
    }

    default void setVersion(String version) { getService(OrchidSite.class).setVersion(version); }
    default String getVersion() { return getService(OrchidSite.class).getVersion(); }

    default void setBaseUrl(String baseUrl) { getService(OrchidSite.class).setBaseUrl(baseUrl); }
    default String getBaseUrl() { return getService(OrchidSite.class).getBaseUrl(); }

    default void setEnvironment(String environment) { getService(OrchidSite.class).setEnvironment(environment); }
    default String getEnvironment() { return getService(OrchidSite.class).getEnvironment(); }

    default boolean isDebug() { return getService(OrchidSite.class).isDebug(); }
    default boolean isProduction() { return getService(OrchidSite.class).isProduction(); }

    default void setDefaultTemplateExtension(String environment) { getService(OrchidSite.class).setDefaultTemplateExtension(environment); }
    default String getDefaultTemplateExtension() { return getService(OrchidSite.class).getDefaultTemplateExtension(); }

    default String getCurrentWorkingDirectory() { return getService(OrchidSite.class).getCurrentWorkingDirectory(); }

    default SiteInfo getSiteInfo() { return getService(OrchidSite.class).getSiteInfo(); }

    default JSONObject toJSON() { return getService(OrchidSite.class).toJSON(); }

    default String getSourceDir() { return getService(OrchidSite.class).getSourceDir(); }
    default String getDestinationDir() { return getService(OrchidSite.class).getDestinationDir(); }
}
