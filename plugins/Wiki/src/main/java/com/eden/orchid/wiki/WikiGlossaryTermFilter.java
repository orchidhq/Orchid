package com.eden.orchid.wiki;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.render.ContentFilter;
import org.json.JSONObject;

public class WikiGlossaryTermFilter extends ContentFilter {
    @Override
    public String apply(String content) {
        String outputContent = content;

        for(JSONObject term : WikiGenerator.terms) {
            if(outputContent.contains(term.getString("name"))) {
                outputContent = outputContent.replaceAll(term.getString("name"), Clog.format("<a href=\"#{$1}\">#{$2}</a>", new Object[]{ term.getString("url"), term.getString("name") }));
            }
        }

        return outputContent;
    }
}
