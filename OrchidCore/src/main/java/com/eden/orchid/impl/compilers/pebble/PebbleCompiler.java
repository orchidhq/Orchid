package com.eden.orchid.impl.compilers.pebble;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.NodeVisitorFactory;
import com.mitchellbosecke.pebble.lexer.LexerImpl;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RootNode;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.parser.ParserImpl;
import com.mitchellbosecke.pebble.parser.ParserOptions;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public final class PebbleCompiler extends OrchidCompiler implements OrchidEventListener {

    private ExecutorService executor;
    private PebbleEngine engine;

    @Inject
    public PebbleCompiler(PebbleTemplateLoader loader, Set<Extension> extensions) {
        super(10000);

        Extension[] extensionArray = new Extension[extensions.size()];
        extensions.toArray(extensionArray);

        this.executor = Executors.newFixedThreadPool(10);
        this.engine = new PebbleEngine.Builder()
                .loader(loader)
                .executorService(executor)
                .extension(extensionArray)
                .allowGetClass(true)
                .newLineTrimming(false)
                .build();

        this.engine.getExtensionRegistry().getAttributeResolver().add(new GetMethodAttributeResolver());
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
                    engine.getExtensionRegistry().getTokenParsers(),
                    new ParserOptions()
            );
            RootNode root = parser.parse(tokenStream);

            PebbleTemplateImpl compiledTemplate = new PebbleTemplateImpl(engine, root, "");

            for (NodeVisitorFactory visitorFactory : engine.getExtensionRegistry().getNodeVisitors()) {
                visitorFactory.createVisitor(compiledTemplate).visit(root);
            }

            Writer writer = new StringWriter();
            compiledTemplate.evaluate(writer, data);

            return writer.toString();
        }
        catch (PebbleException e) {
            OrchidExtensionsKt.logSyntaxError(source, extension, e.getLineNumber(), e.getMessage());
        }
        catch (Exception e) {
            Clog.e("Error rendering Pebble template (see template source below)", e);
            Clog.e(source);
        }
        return source;
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"peb", "pebble"};
    }

// Clean up executor on shutdown
//----------------------------------------------------------------------------------------------------------------------

    @On(Orchid.Lifecycle.Shutdown.class)
    public void onEndSession(Orchid.Lifecycle.Shutdown event) {
        executor.shutdown();
    }

    @On(Orchid.Lifecycle.ClearCache.class)
    public void onClearCache(Orchid.Lifecycle.ClearCache event) {
        engine.getTagCache().invalidateAll();
        engine.getTemplateCache().invalidateAll();
    }

}
