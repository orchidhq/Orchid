package com.eden.orchid.wiki.model;

import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class WikiModel {

    @Getter
    private Map<String, WikiSection> sections;

    public void initialize() {
        this.sections = new LinkedHashMap<>();
    }

    public List<OrchidPage> getAllPages() {
        List<OrchidPage> allPages = new ArrayList<>();
        for (String key : sections.keySet()) {
            allPages.add(sections.get(key).getSummaryPage());
            allPages.addAll(sections.get(key).getWikiPages());
        }

        return allPages;
    }

}
