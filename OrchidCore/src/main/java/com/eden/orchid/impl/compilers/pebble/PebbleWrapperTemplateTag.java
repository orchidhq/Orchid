package com.eden.orchid.impl.compilers.pebble;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.google.inject.Provider;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;
import lombok.Getter;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class PebbleWrapperTemplateTag implements TokenParser {

    private final Provider<OrchidContext> contextProvider;
    private final String name;
    private final String defaultParameter;
    private final Class<? extends TemplateTag> tagClass;

    public PebbleWrapperTemplateTag(Provider<OrchidContext> contextProvider, String name, String defaultParameter, Class<? extends TemplateTag> tagClass) {
        this.contextProvider = contextProvider;
        this.name = name;
        this.defaultParameter = defaultParameter;
        this.tagClass = tagClass;
    }

    @Override
    public String getTag() {
        return name;
    }

    @Override
    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // skip over the tag name token
        stream.next();

        // Get list of available parameter names
        OptionsExtractor extractor = contextProvider.get().getInjector().getInstance(OptionsExtractor.class);
        List<String> remainingParameters = new ArrayList<>(extractor.getOptionNames(tagClass));

        // parameter expressions will be added here
        Map<String, Expression<?>> paramExpressionMap = new HashMap<>();

        if(stream.current().getType().equals(Token.Type.NAME)) {
            // parse map of key=value pairs
            while (!stream.current().getType().equals(Token.Type.EXECUTE_END)) {
                Token paramNameToken = stream.expect(Token.Type.NAME);

                Optional<String> foundKey = remainingParameters.stream().filter(key -> key.equalsIgnoreCase(paramNameToken.getValue())).findAny();

                if (foundKey.isPresent()) {
                    String paramKey = foundKey.get();
                    remainingParameters.remove(paramKey);
                    stream.expect(Token.Type.PUNCTUATION, "=");
                    Expression<?> parsedExpression = parser.getExpressionParser().parseExpression();
                    paramExpressionMap.put(paramKey, parsedExpression);
                }
                else {
                    throw new ParserException(null, Clog.format("Could not parse parameter {}.", paramNameToken.getValue()), lineNumber, "");
                }
            }

            stream.expect(Token.Type.EXECUTE_END);
        }
        else if(stream.current().getType().equals(Token.Type.EXECUTE_END)) {
            // at end, just complete now
            stream.expect(Token.Type.EXECUTE_END);
        }
        else  {
            // parse the default parameter
            Expression<?> parsedExpression = parser.getExpressionParser().parseExpression();
            paramExpressionMap.put(defaultParameter, parsedExpression);
            stream.expect(Token.Type.EXECUTE_END);
        }

        return new TemplateTagNode(lineNumber, paramExpressionMap);
    }

    private class TemplateTagNode extends AbstractRenderableNode {

        private final Map<String, Expression<?>> paramExpressionMap;

        public TemplateTagNode(int lineNumber, Map<String, Expression<?>> paramExpressionMap) {
            super(lineNumber);
            this.paramExpressionMap = paramExpressionMap;
        }

        @Override
        public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException, IOException {
            Map<String, Object> evaluatedParamExpressionMap = new HashMap<>();

            for(Map.Entry<String, Expression<?>> entry : paramExpressionMap.entrySet()) {
                evaluatedParamExpressionMap.put(entry.getKey(), entry.getValue().evaluate(self, context));
            }

            String template = Clog.format("tags/{}.{}", name, contextProvider.get().getDefaultTemplateExtension());

            OrchidResource resource = contextProvider.get().getResourceEntry(template);

            if (resource == null) {
                throw new PebbleException(null, Clog.format("Could not find template for TemplateTag [{}] ({}).", name, template), getLineNumber(), self.getName());
            }

            TemplateTag freshTag = contextProvider.get().getInjector().getInstance(tagClass);
            JSONObject object = new JSONObject(evaluatedParamExpressionMap);
            freshTag.extractOptions(contextProvider.get(), object);

            Map<String, Object> templateArgs = new HashMap<>();
            templateArgs.put("tag", freshTag);

            String output = resource.compileContent(templateArgs);
            writer.append(output);
        }

        @Override
        public void accept(NodeVisitor visitor) {
            visitor.visit(this);
        }
    }

}
