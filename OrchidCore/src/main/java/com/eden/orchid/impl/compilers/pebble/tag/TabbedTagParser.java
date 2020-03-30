package com.eden.orchid.impl.compilers.pebble.tag;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.compilers.pebble.PebbleWrapperTemplateTag;
import javax.inject.Provider;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.utils.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TabbedTagParser extends BaseTagParser {

    private final String[] tagParameters;
    private final Class<? extends TemplateTag> tagClass;
    private final String[] tabParameters;
    private final Class<? extends TemplateTag.Tab> tabClass;

    private BaseTagParser childParser;

    public TabbedTagParser(
            Provider<OrchidContext> contextProvider,
            String name,
            String[] tagParameters,
            Class<? extends TemplateTag> tagClass,
            String[] tabParameters,
            Class<? extends TemplateTag.Tab> tabClass
    ) {
        super(contextProvider, name);
        this.tagParameters = tagParameters;
        this.tagClass = tagClass;
        this.tabParameters = tabParameters;
        this.tabClass = tabClass;
    }

    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        TokenStream stream = parser.getStream();

        // skip over the tag name token
        stream.next();

        if(stream.current().test(Token.Type.NAME, "dynamic")) {
            stream.next();
            childParser = new DynamicTabbedTagParser(contextProvider, name, tagParameters, tagClass);
            return childParser.parse(token, parser);
        }
        else {
            childParser = new NonDynamicTabbedTagParser(contextProvider, name, tagParameters, tagClass, tabParameters, tabClass);
            return childParser.parse(token, parser);
        }
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContextImpl context) throws IOException {
        childParser.render(self, writer, context);
    }
}
