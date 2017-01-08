package com.eden.orchid.compilers.impl.markdown;

import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.utilities.AutoRegister;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

@AutoRegister
public class MarkdownCompiler implements Compiler {
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
        return 60;
    }
}
