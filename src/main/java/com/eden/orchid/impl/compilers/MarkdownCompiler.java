package com.eden.orchid.impl.compilers;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.registration.AutoRegister;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

@AutoRegister
public class MarkdownCompiler implements OrchidCompiler {
    @Override
    public String compile(String extension, String source, Object... data) {
        return HtmlRenderer.builder().build().render(Parser.builder().build().parse(source));
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"md", "markdown"};
    }

    @Override
    public int priority() {
        return 900;
    }
}
