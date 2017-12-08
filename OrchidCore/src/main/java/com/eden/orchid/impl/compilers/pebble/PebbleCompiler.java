package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.NodeVisitorFactory;
import com.mitchellbosecke.pebble.extension.Test;
import com.mitchellbosecke.pebble.lexer.LexerImpl;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RootNode;
import com.mitchellbosecke.pebble.operator.BinaryOperator;
import com.mitchellbosecke.pebble.operator.UnaryOperator;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.parser.ParserImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public final class PebbleCompiler extends OrchidCompiler implements OrchidEventListener {

    private ExecutorService executor;
    private PebbleEngine engine;

    @Inject
    public PebbleCompiler(PebbleTemplateLoader loader, Set<JtwigFunction> filters) {
        super(10000);

        executor = Executors.newFixedThreadPool(10);

        engine = new PebbleEngine.Builder()
                .loader(loader)
                .executorService(executor)
                .extension(new Extension() {
                    @Override public Map<String, Filter> getFilters() {
                        Map<String, Filter> filterMap = new HashMap<>();

                        for (JtwigFunction function : filters) {
                            if (function instanceof Filter) {
                                filterMap.put(function.name(), (Filter) function);
                            }
                        }

                        return filterMap;
                    }

                    @Override public Map<String, Test> getTests() {
                        return null;
                    }

                    @Override public Map<String, Function> getFunctions() {
                        return null;
                    }

                    @Override public List<TokenParser> getTokenParsers() {
                        return null;
                    }

                    @Override public List<BinaryOperator> getBinaryOperators() {
                        return null;
                    }

                    @Override public List<UnaryOperator> getUnaryOperators() {
                        return null;
                    }

                    @Override public Map<String, Object> getGlobalVariables() {
                        return null;
                    }

                    @Override public List<NodeVisitorFactory> getNodeVisitors() {
                        return null;
                    }
                })
                .build();
    }

    @Override
    public String compile(String extension, String source, Map<String, Object> data) {
        try {
            LexerImpl lexer = new LexerImpl(
                    engine.getSyntax(),
                    engine.getExtensionRegistry().getUnaryOperators().values(),
                    engine.getExtensionRegistry().getBinaryOperators().values());
            TokenStream tokenStream = lexer.tokenize(new StringReader(source), "");

            Parser parser = new ParserImpl(
                    engine.getExtensionRegistry().getUnaryOperators(),
                    engine.getExtensionRegistry().getBinaryOperators(),
                    engine.getExtensionRegistry().getTokenParsers());
            RootNode root = parser.parse(tokenStream);

            PebbleTemplateImpl compiledTemplate = new PebbleTemplateImpl(engine, root, "");

            for (NodeVisitorFactory visitorFactory : engine.getExtensionRegistry().getNodeVisitors()) {
                visitorFactory.createVisitor(compiledTemplate).visit(root);
            }

            Writer writer = new StringWriter();
            compiledTemplate.evaluate(writer, data);

            return writer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"html", "twig", "peb", "pebble"};
    }

// Clean up executor on shutdown
//----------------------------------------------------------------------------------------------------------------------

    @On(Orchid.Lifecycle.Shutdown.class)
    public void onEndSession(Orchid.Lifecycle.Shutdown event) {
        executor.shutdown();
    }

}
