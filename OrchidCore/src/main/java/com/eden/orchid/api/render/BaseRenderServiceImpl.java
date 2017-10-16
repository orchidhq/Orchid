package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.io.InputStream;

public abstract class BaseOrchidRendererServiceImpl implements OrchidRenderService {

    protected OrchidContext context;
    @Getter protected TemplateResolutionStrategy strategy;

    @Inject
    public BaseOrchidRendererServiceImpl(OrchidContext context, TemplateResolutionStrategy strategy) {
        this.context = context;
        this.strategy = strategy;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    public final boolean renderTemplate(OrchidPage page) {
        for (String template : strategy.getPageTemplate(page)) {
            OrchidResource templateResource = context.getResourceEntry(template);

            if (templateResource != null) {
                return render(page, FilenameUtils.getExtension(template), templateResource.getContent());
            }
        }

        return false;
    }

    public final boolean renderString(OrchidPage page, String extension, String templateString) {
        return render(page, extension, templateString);
    }

    public final boolean renderRaw(OrchidPage page) {
        String content = page.getResource().getContent();

        if(page.getResource().shouldPrecompile()) {
            content = context.precompile(content, page.getData());
        }

        return render(page, page.getResource().getReference().getExtension(), content);
    }

    public final boolean renderBinary(OrchidPage page) {
        return render(page, page.getResource().getReference().getExtension(), page.getResource().getContentStream());
    }

    /**
     * Internal representation of a 'render' operation.
     *
     * @param page the page to render
     * @param extension the extension that the content represents and should be compiled against
     * @param content the template string to render
     * @return true if the page was successfully rendered, false otherwise
     */
    public abstract boolean render(OrchidPage page, String extension, String content);

    /**
     * Internal representation of a 'render' operation on a binary stream.
     *
     * @param page the page to render
     * @param extension the extension that the content represents and should be compiled against
     * @param content the template string to render
     * @return true if the page was successfully rendered, false otherwise
     */
    public abstract boolean render(OrchidPage page, String extension, InputStream content);

}
