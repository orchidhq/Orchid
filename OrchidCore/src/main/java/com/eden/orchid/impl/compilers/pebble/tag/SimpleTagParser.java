package com.eden.orchid.impl.compilers.pebble.tag;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.impl.compilers.pebble.PebbleWrapperTemplateTag;
import com.google.inject.Provider;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class SimpleTagParser extends BaseTagParser {

    private final Class<? extends TemplateTag> tagClass;
    private final String[] parameters;

    private Map<String, Expression<?>> paramExpressionMap;

    public SimpleTagParser(
            Provider<OrchidContext> contextProvider,
            String name,
            String[] parameters,
            Class<? extends TemplateTag> tagClass) {
        super(contextProvider, name);
        this.parameters = parameters;
        this.tagClass = tagClass;
    }

    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // skip over the tag name token
        stream.next();

        // parameter expressions will be added here
        paramExpressionMap = parseParams(parameters, tagClass, stream, parser);

        // end parsing now
        stream.expect(Token.Type.EXECUTE_END);

        return new PebbleWrapperTemplateTag.TemplateTagNode(lineNumber, this);
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContextImpl context) throws IOException {
        TemplateTag freshTag = contextProvider.get().getInjector().getInstance(tagClass);

        Map<String, Object> evaluatedParamExpressionMap = evaluateParams(paramExpressionMap, self, context);
        freshTag.extractOptions(contextProvider.get(), evaluatedParamExpressionMap);

        freshTag.onRender();
        if (freshTag.rendersContent()) {
            Map<String, Object> templateArgs = new HashMap<>();
            templateArgs.put("tag", freshTag);
            renderTagContent(self, writer, context, templateArgs, Exception::printStackTrace);
        }
    }

}
