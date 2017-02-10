package com.eden.orchid.impl.compilers;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import javax.inject.Singleton;

@Singleton
public class MarkdownCompiler implements OrchidCompiler {

    HtmlRenderer renderer;

    public MarkdownCompiler() {
        HtmlRenderer.Builder builder = HtmlRenderer.builder();

        renderer = HtmlRenderer.builder().build();
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

    @Override
    public int priority() {
        return 900;
    }
}
