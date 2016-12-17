package com.eden.orchid.compilers.impl;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.compilers.ContentCompiler;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.json.JSONArray;
import org.json.JSONObject;

@AutoRegister
public class MarkdownCompiler implements ContentCompiler {
    @Override
    public String compile(String input, JSONObject json) {
        return HtmlRenderer.builder().build()
                .render(Parser.builder().build().parse(input));
    }

    @Override
    public String compile(String input, JSONArray json) {
        return HtmlRenderer.builder().build()
               .render(Parser.builder().build().parse(input));
    }

    @Override
    public int priority() {
        return 100;
    }
}
