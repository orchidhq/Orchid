package com.eden.orchid.wiki.model;

import com.eden.orchid.wiki.pages.WikiPage;
import com.eden.orchid.wiki.pages.WikiSummaryPage;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class WikiSection {
    private String section;
    private WikiSummaryPage summaryPage;
    private List<WikiPage> wikiPages;
}
