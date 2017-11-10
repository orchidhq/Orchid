package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.function.Supplier;

public class RenderServiceImpl implements RenderService {

    protected OrchidContext context;
    protected TemplateResolutionStrategy strategy;
    protected OrchidRenderer renderer;

    @Option
    @BooleanDefault(false)
    public boolean dry;

    @Option
    @BooleanDefault(false)
    public boolean renderDrafts;

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
            content = context.precompile(content, page.getData());
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

    InputStream toStream(String content) {
        try {
            return IOUtils.toInputStream(content, "UTF-8");
        }
        catch (Exception e) {
            return null;
        }
    }

    boolean skipPage(OrchidPage page) {
        return dry || (page.isDraft() && !renderDrafts) || !page.shouldRender();
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
