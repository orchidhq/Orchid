package com.eden.orchid.impl.compilers;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkdownCompiler extends OrchidCompiler {

    HtmlRenderer renderer;

    @Inject
    public MarkdownCompiler() {
        HtmlRenderer.Builder builder = HtmlRenderer.builder();

        renderer = HtmlRenderer.builder().build();

        this.priority = 900;
    }

    @Override
    public String compile(String extension, String source, Object... data) {
        return renderer.render(Parser.builder().build().parse(source));
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"md", "markdown"};
    }
}
