package com.eden.orchid.api.site;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.ApplyBaseUrl;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class SiteInfo implements OptionsHolder {

    @ApplyBaseUrl
    @Option private String avatar;

    @Option private String siteName;
    @Option private String siteDescription;

    @Option private String tagline;
    @Option private String blurb;

}