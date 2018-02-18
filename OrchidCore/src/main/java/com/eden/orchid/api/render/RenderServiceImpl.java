package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @since v1.0.0
 * @orchidApi services
 */
public class RenderServiceImpl implements RenderService {

    protected OrchidContext context;
    protected TemplateResolutionStrategy strategy;
    protected OrchidRenderer renderer;

    @Option @BooleanDefault(false)
    @Description("On a dry run, pages are indexed but not rendered.")
    public boolean dry;

    @Getter @Accessors(fluent = true)
    @Option @BooleanDefault(false)
    @Description("Normally, draft pages are not rendered along with the rest of the site, but this behavior can be " +
            "turned off by setting this value to `true`."
    )
    public boolean includeDrafts;

    @Inject
    public RenderServiceImpl(OrchidContext context, TemplateResolutionStrategy strategy, OrchidRenderer renderer) {
        this.context = context;
        this.strategy = strategy;
        this.renderer = renderer;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public final InputStream getRenderedTemplate(OrchidPage page) {
        InputStream is = null;

        page.setCurrent(true);

        for (String template : strategy.getPageLayout(page)) {
            OrchidResource templateResource = context.getResourceEntry(template);
            if (templateResource != null) {
                String content = "" + context.compile(FilenameUtils.getExtension(template), templateResource.getContent(), page);
                is = toStream(content);
                break;
            }
        }

        page.setCurrent(false);

        return is;
    }

    @Override
    public final boolean renderTemplate(final OrchidPage page) {
        return renderInternal(page, () -> getRenderedTemplate(page));
    }

    @Override
    public final InputStream getRenderedString(OrchidPage page, String extension, String templateString) {
        page.setCurrent(true);
        String content = "" + context.compile(extension, templateString, page);
        page.setCurrent(false);
        return toStream(content);
    }

    @Override
    public final boolean renderString(final OrchidPage page, final String extension, final String templateString) {
        return renderInternal(page, () -> getRenderedString(page, extension, templateString));
    }

    @Override
    public final InputStream getRenderedRaw(OrchidPage page) {
        page.setCurrent(true);
        String content = page.getResource().getContent();
        if (page.getResource().shouldPrecompile()) {
            content = context.precompile(page.getResource().getPrecompilerExtension(), content, page.getData());
        }
        content = "" + context.compile(page.getResource().getReference().getExtension(), content, page);
        page.setCurrent(false);
        return toStream(content);
    }

    @Override
    public final boolean renderRaw(final OrchidPage page) {
        return renderInternal(page, () -> getRenderedRaw(page));
    }

    @Override
    public final InputStream getRenderedBinary(OrchidPage page) {
        page.setCurrent(true);
        InputStream is = page.getResource().getContentStream();
        page.setCurrent(false);
        return is;
    }

    @Override
    public final boolean renderBinary(final OrchidPage page) {
        return renderInternal(page, () -> getRenderedBinary(page));
    }

    @Override
    public boolean render(OrchidPage page, RenderMode renderMode) {
        switch (renderMode) {
            case TEMPLATE: return renderTemplate(page);
            case RAW:      return renderRaw(page);
            case BINARY:   return renderBinary(page);
            default: throw new IllegalArgumentException("Dynamic RenderMode rendering must be one of [TEMPLATE, RAW, BINARY]");
        }
    }

    @Override
    public InputStream getRendered(OrchidPage page, RenderMode renderMode) {
        switch (renderMode) {
            case TEMPLATE: return getRenderedTemplate(page);
            case RAW:      return getRenderedRaw(page);
            case BINARY:   return getRenderedBinary(page);
            default: throw new IllegalArgumentException("Dynamic RenderMode rendering must be one of [TEMPLATE, RAW, BINARY]");
        }
    }

    @Override
    public boolean render(OrchidPage page, String renderMode) {
        switch (renderMode.toUpperCase()) {
            case "TEMPLATE": return renderTemplate(page);
            case "RAW":      return renderRaw(page);
            case "BINARY":   return renderBinary(page);
            default: throw new IllegalArgumentException("Dynamic RenderMode rendering must be one of [TEMPLATE, RAW, BINARY]");
        }
    }

    @Override
    public InputStream getRendered(OrchidPage page, String renderMode) {
        switch (renderMode.toUpperCase()) {
            case "TEMPLATE": return getRenderedTemplate(page);
            case "RAW":      return getRenderedRaw(page);
            case "BINARY":   return getRenderedBinary(page);
            default: throw new IllegalArgumentException("Dynamic RenderMode rendering must be one of [TEMPLATE, RAW, BINARY]");
        }
    }

    InputStream toStream(String content) {
        try {
            return IOUtils.toInputStream(content, "UTF-8");
        }
        catch (Exception e) {
            return null;
        }
    }

    boolean skipPage(OrchidPage page) {
        return dry || (page.isDraft() && !includeDrafts) || !page.shouldRender();
    }

    boolean renderInternal(OrchidPage page, Supplier<InputStream> factory) {
        long startTime = System.currentTimeMillis();
        long stopTime;

        boolean result = false;

        if (!skipPage(page)) {
            result = renderer.render(page, factory.get());
        }

        stopTime = System.currentTimeMillis();
        context.onPageGenerated(page, stopTime - startTime);

        return result;
    }

}
