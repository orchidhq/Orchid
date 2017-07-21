package com.eden.orchid.impl.compilers.markdown;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.IRender;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class MarkdownCompiler extends OrchidCompiler {

    private Parser parser;
    private IRender renderer;

    @Inject
    public MarkdownCompiler() {
        this.priority = 900;
        List<Extension> extensionList = Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create()
        );

        MutableDataSet formatOptions = new MutableDataSet();
        formatOptions.set(HtmlRenderer.GENERATE_HEADER_ID, true);
        formatOptions.set(HtmlRenderer.RENDER_HEADER_ID, true);
        formatOptions.set(Parser.EXTENSIONS, extensionList);

        parser = Parser.builder(formatOptions).build();
        renderer = HtmlRenderer.builder(formatOptions).build();
    }

    @Override
    public String compile(String extension, String source, Object... data) {
        return renderer.render(parser.parse(source));
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
