package com.eden.orchid.impl.compilers.markdown;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkdownCompiler extends OrchidCompiler {

    private HtmlRenderer renderer;

    @Inject
    public MarkdownCompiler() {
        MutableDataHolder options = new MutableDataSet();
        options.set(HtmlRenderer.GENERATE_HEADER_ID, true);
        options.set(HtmlRenderer.RENDER_HEADER_ID, true);

        renderer = HtmlRenderer.builder(options).build();

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
