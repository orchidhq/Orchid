package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.InputStream;

public abstract class BaseRenderServiceImpl implements RenderService {

    protected OrchidContext context;
    @Getter protected TemplateResolutionStrategy strategy;

    @Option
    @BooleanDefault(false)
    public boolean dry;

    @Inject
    public BaseRenderServiceImpl(OrchidContext context, TemplateResolutionStrategy strategy) {
        this.context = context;
        this.strategy = strategy;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    public final InputStream getRenderedTemplate(OrchidPage page) {
        InputStream is = null;

        page.setCurrent(true);

        for (String template : strategy.getPageLayout(page)) {
            OrchidResource templateResource = context.getResourceEntry(template);
            if (templateResource != null) {
                String content = "" + context.compile(FilenameUtils.getExtension(template), templateResource.getContent(), page);
                is = toStream(content);
            }
        }

        page.setCurrent(false);

        return is;
    }

    public final boolean renderTemplate(OrchidPage page) {
        return render(page, getRenderedTemplate(page));
    }

    public final InputStream getRenderedString(OrchidPage page, String extension, String templateString) {
        page.setCurrent(true);
        String content = "" + context.compile(extension, templateString, page);
        page.setCurrent(false);
        return toStream(content);
    }

    public final boolean renderString(OrchidPage page, String extension, String templateString) {
        return render(page, getRenderedString(page, extension, templateString));
    }

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

    public final boolean renderRaw(OrchidPage page) {
        return render(page, getRenderedRaw(page));
    }

    public final InputStream getRenderedBinary(OrchidPage page) {
        page.setCurrent(true);
        InputStream is = page.getResource().getContentStream();
        page.setCurrent(false);
        return is;
    }

    public final boolean renderBinary(OrchidPage page) {
        return render(page, getRenderedBinary(page));
    }

    protected final InputStream toStream(String content) {
        try {
            return IOUtils.toInputStream(content, "UTF-8");
        }
        catch (Exception e) {
            return null;
        }
    }

    protected final boolean skipPage(OrchidPage page) {
        return dry || page.isDraft() || !page.shouldRender();
    }

    /**
     * {@inheritDoc}
     */
    public abstract boolean render(OrchidPage page, InputStream content);

}
