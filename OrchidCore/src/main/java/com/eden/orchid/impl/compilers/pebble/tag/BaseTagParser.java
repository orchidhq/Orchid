package com.eden.orchid.impl.compilers.pebble.tag;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.options.OptionsHolder;
import javax.inject.Provider;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.BodyNode;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.node.expression.FilterExpression;
import com.mitchellbosecke.pebble.node.expression.RenderableNodeExpression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class BaseTagParser {

    protected final Provider<OrchidContext> contextProvider;
    protected final String name;

    public BaseTagParser(Provider<OrchidContext> contextProvider, String name) {
        this.contextProvider = contextProvider;
        this.name = name;
    }

    public abstract RenderableNode parse(Token token, Parser parser) throws ParserException;

    public abstract void render(PebbleTemplateImpl self, Writer writer, EvaluationContextImpl context) throws IOException;

    protected Map<String, Expression<?>> parseParams(String[] parameters, Class<? extends OptionsHolder> paramsClass, TokenStream stream, Parser parser) throws ParserException {

        // Get list of available parameter names
        OptionsExtractor extractor = contextProvider.get().resolve(OptionsExtractor.class);
        List<String> remainingParameters = new ArrayList<>(extractor.getOptionNames(paramsClass));

        // parameter expressions will be added here
        Map<String, Expression<?>> paramExpressionMap = new HashMap<>();
        if(!isParameterEnd(stream)) {
            if (stream.current().test(Token.Type.NAME)) {
                // parse parameters as map of key=value pairs
                while (!isParameterEnd(stream)) {
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
                        throw new ParserException(null, Clog.format("Could not parse parameter {}.", paramNameToken.getValue()), stream.current().getLineNumber(), "");
                    }
                }
            }
            else {
                // parse the parameters sequentially
                int i = 0;
                while (i < parameters.length && !isParameterEnd(stream)) {
                    Expression<?> parsedExpression = parser.getExpressionParser().parseExpression();
                    paramExpressionMap.put(parameters[i], parsedExpression);
                    i++;
                }
            }
        }

        return paramExpressionMap;
    }

    protected Map<String, Object> evaluateParams(Map<String, Expression<?>> paramExpressionMap, PebbleTemplateImpl self, EvaluationContextImpl context) {
        Map<String, Object> evaluatedParamExpressionMap = new HashMap<>();

        for (Map.Entry<String, Expression<?>> entry : paramExpressionMap.entrySet()) {
            evaluatedParamExpressionMap.put(entry.getKey(), entry.getValue().evaluate(self, context));
        }

        return evaluatedParamExpressionMap;
    }

    protected boolean isParameterEnd(TokenStream stream) {
        return stream.current().test(Token.Type.EXECUTE_END) || stream.current().test(Token.Type.PUNCTUATION, ":");
    }

    protected List<Expression<?>> parseBodyFilters(TokenStream stream, Parser parser) throws ParserException {
        List<Expression<?>> filterInvocationExpressions = new ArrayList<>();

        if (stream.current().test(Token.Type.PUNCTUATION, ":")) {
            stream.next();
            stream.expect(Token.Type.PUNCTUATION, ":");

            filterInvocationExpressions.add(parser.getExpressionParser().parseFilterInvocationExpression());

            while (stream.current().test(Token.Type.OPERATOR, "|")) {
                // skip the '|' token
                stream.next();
                filterInvocationExpressions.add(parser.getExpressionParser().parseFilterInvocationExpression());
            }
        }

        return filterInvocationExpressions;
    }

    protected Expression<?> parseBody(List<Expression<?>> filterInvocationExpressions, String bodyTagName, TokenStream stream, Parser parser) throws ParserException {
        BodyNode body = parser.subparse(token -> token.test(Token.Type.NAME, "end" + bodyTagName));

        stream.next();
        stream.expect(Token.Type.EXECUTE_END);

        Expression<?> lastExpression = new RenderableNodeExpression(body, stream.current().getLineNumber());

        for (Expression<?> filterInvocationExpression : filterInvocationExpressions) {

            FilterExpression filterExpression = new FilterExpression();
            filterExpression.setRight(filterInvocationExpression);
            filterExpression.setLeft(lastExpression);

            lastExpression = filterExpression;
        }

        return lastExpression;
    }

    protected interface CheckedFunction<I> {
        void apply(I i) throws Exception;
    }

    protected static <I> Consumer<I> unchecked(CheckedFunction<I> f) {
        return i -> {
            try {
                f.apply(i);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

}
