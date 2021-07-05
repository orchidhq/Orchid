package com.eden.orchid.impl.compilers.pebble;

import clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.AbstractTheme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.attributes.methodaccess.NoOpMethodAccessValidator;
import com.mitchellbosecke.pebble.cache.CacheKey;
import com.mitchellbosecke.pebble.cache.PebbleCache;
import com.mitchellbosecke.pebble.cache.tag.ConcurrentMapTagCache;
import com.mitchellbosecke.pebble.cache.template.ConcurrentMapTemplateCache;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.NodeVisitorFactory;
import com.mitchellbosecke.pebble.lexer.LexerImpl;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RootNode;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.parser.ParserImpl;
import com.mitchellbosecke.pebble.parser.ParserOptions;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import kotlin.NotImplementedError;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public final class PebbleCompiler extends OrchidCompiler implements OrchidEventListener {

    private final ExecutorService executor;
    private final Provider<OrchidContext> contextProvider;

    private final PebbleCache<CacheKey, Object> tagCache;
    private final PebbleCache<Object, PebbleTemplate> templateCache;

    private final Extension[] extensionArray;

    @Inject
    public PebbleCompiler(Set<Extension> extensions, Provider<OrchidContext> contextProvider) {
        super(10000);

        extensionArray = new Extension[extensions.size()];
        extensions.toArray(extensionArray);

        tagCache = new ConcurrentMapTagCache();
        templateCache = new ConcurrentMapTemplateCache();

        this.executor = Executors.newFixedThreadPool(1);
        this.contextProvider = contextProvider;
    }

    @Override
    public void compile(OutputStream os, @Nullable OrchidResource resource, String extension, String input, Map<String, Object> data) {
        try {
            PebbleTemplateLoader loader = new PebbleTemplateLoader(contextProvider.get(), contextProvider.get().getTheme());

            PebbleEngine engine = new PebbleEngine.Builder()
                    .loader(loader)
                    .executorService(executor)
                    .extension(extensionArray)
                    .methodAccessValidator(new NoOpMethodAccessValidator())
                    .newLineTrimming(false)
                    .tagCache(tagCache)
                    .templateCache(templateCache)
                    .build();

            engine.getExtensionRegistry().getAttributeResolver().add(new GetMethodAttributeResolver());

            LexerImpl lexer = new LexerImpl(
                    engine.getSyntax(),
                    engine.getExtensionRegistry().getUnaryOperators().values(),
                    engine.getExtensionRegistry().getBinaryOperators().values());
            TokenStream tokenStream = lexer.tokenize(new StringReader(input), "");

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

            ByteArrayOutputStream os1 = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(os1, StandardCharsets.UTF_8);
            compiledTemplate.evaluate(writer, data);
            writer.close();
            os.write(os1.toByteArray());
        } catch (PebbleException e) {
            OrchidExtensionsKt.logSyntaxError(input, extension, e.getLineNumber(), 0, e.getMessage(), e.getCause());
        } catch (Exception e) {
            Clog.e("Error rendering Pebble template (see template source below)", e);
            Clog.e(input);
            contextProvider.get().diagnosisMessage(() -> ExceptionUtils.getStackTrace(e));
        }
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
        tagCache.invalidateAll();
        templateCache.invalidateAll();
    }

}
