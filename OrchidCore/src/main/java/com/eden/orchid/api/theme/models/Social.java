package com.eden.orchid.api.theme.models;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Social implements OptionsHolder {

    @Option private String twitter;
    @Option private String facebook;
    @Option private String instagram;
    @Option private String googlePlus;
    @Option private String rss;
    @Option private String email;
    @Option private String github;

}