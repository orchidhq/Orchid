package com.eden.orchid.api.site;

import com.eden.orchid.api.OrchidService;

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

}
