package com.eden.orchid.impl.compilers.pebble;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.impl.compilers.pebble.tag.BaseTagParser;
import com.eden.orchid.impl.compilers.pebble.tag.ContentTagParser;
import com.eden.orchid.impl.compilers.pebble.tag.SimpleTagParser;
import com.eden.orchid.impl.compilers.pebble.tag.TabbedTagParser;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;
import com.mitchellbosecke.pebble.utils.StringUtils;

import javax.inject.Provider;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public final class PebbleWrapperTemplateTagChildTab implements TokenParser {

    protected final Provider<OrchidContext> contextProvider;
    protected final String name;

    public PebbleWrapperTemplateTagChildTab(Provider<OrchidContext> contextProvider, String name) {
        this.contextProvider = contextProvider;
        this.name = name;
    }

    @Override
    public String getTag() {
        return name;
    }

    @Override
    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        return new Noodle(contextProvider, name).parse(token, parser);
    }

    private static class Noodle extends BaseTagParser {

        private Expression<?> tabNameExpression;
        private Map<String, Expression<?>> paramExpressionMap;
        private Expression<?> tagBodyExpression;

        public Noodle(Provider<OrchidContext> contextProvider, String name) {
            super(contextProvider, name);
        }

        @Override
        public RenderableNode parse(Token token, Parser parser) throws ParserException {
            TokenStream stream = parser.getStream();
            int lineNumber = token.getLineNumber();

            // skip over the tag name token
            stream.next();

            // parameter expressions will be added here
            try {
                tabNameExpression = parser.getExpressionParser().parseExpression();
                paramExpressionMap = parseParams(null, TemplateTag.SimpleTab.class, stream, parser);
                List<Expression<?>> bodyFilters = parseBodyFilters(stream, parser);
                stream.expect(Token.Type.EXECUTE_END);
                tagBodyExpression = parseBody(bodyFilters, name, stream, parser);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return new PebbleWrapperTemplateTag.TemplateTagNode(lineNumber, this);
        }

        @Override
        public void render(PebbleTemplateImpl self, Writer writer, EvaluationContextImpl context) throws IOException {
            Object maybeParentTag = context.getScopeChain().get("__parentTabbedTag");

            if(maybeParentTag == null || !(maybeParentTag instanceof TemplateTag)) {
                throw new PebbleException(null, Clog.format("Child tab '{}' can only be used as a child of a TabbedTag", name));
            }

            TemplateTag parentTag = (TemplateTag) maybeParentTag;

            String tabName = (String) tabNameExpression.evaluate(self, context);
            Map<String, Object> paramsMap = evaluateParams(paramExpressionMap, self, context);

            String bodyContent = StringUtils.toString(tagBodyExpression.evaluate(self, context)).trim();

            TemplateTag.Tab childTabInstance = parentTag.getNewTab(tabName, bodyContent);

            if(!childTabInstance.getType().equals(this.name)) {
                throw new PebbleException(null, Clog.format("Parent tab can only have children tabs of type '{}', got {}", childTabInstance.getName(), name));
            }

            childTabInstance.extractOptions(contextProvider.get(), paramsMap);

            // add the tab to the Tag
            parentTag.setContent(tabName, childTabInstance);
        }
    }
}
