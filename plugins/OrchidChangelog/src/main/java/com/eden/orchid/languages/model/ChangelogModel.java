package com.eden.orchid.languages.model;

import lombok.Getter;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ChangelogModel {

    @Getter
    private String sourceUrl;

    @Getter
    private List<ChangelogVersion> versions;

    public void initialize(List<ChangelogVersion> versions, String sourceUrl) {
        this.sourceUrl = sourceUrl;
        this.versions = versions;
    }

}
