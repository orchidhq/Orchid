package com.eden.orchid.api.theme.models;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.ApplyBaseUrl;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class About implements OptionsHolder {

    @ApplyBaseUrl
    @Option private String avatar;

    @Option private String siteName;
    @Option private String siteDescription;

    @Option private String tagline;
    @Option private String blurb;

}