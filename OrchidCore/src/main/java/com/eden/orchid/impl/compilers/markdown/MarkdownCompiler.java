package com.eden.orchid.impl.compilers.markdown;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.IRender;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class MarkdownCompiler extends OrchidCompiler {

    private Parser parser;
    private IRender renderer;

    @Inject
    public MarkdownCompiler(Set<Extension> extensionSet, Set<MutableDataSet> injectedOptions) {
        this.priority = 900;

        MutableDataSet options = new MutableDataSet();
        options.set(HtmlRenderer.GENERATE_HEADER_ID, true);
        options.set(HtmlRenderer.RENDER_HEADER_ID, true);

        options.set(Parser.EXTENSIONS, extensionSet);

        for(MutableDataSet injectedOption : injectedOptions) {
            options.setAll(injectedOption);
        }

        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
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
