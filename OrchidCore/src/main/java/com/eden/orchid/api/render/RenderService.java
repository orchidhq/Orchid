package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.pages.OrchidPage;

import java.io.InputStream;

public interface OrchidRenderService extends OrchidService {

    /**
     * Render the given page with the given template resource. A list of templates can be given, and the first resource
     * that could be located will be the template used.
     *
     * @param page the page to render
     * @return true if the page was successfully rendered, false otherwise
     */
    default boolean renderTemplate(OrchidPage page) {
        return getService(OrchidRenderService.class).renderTemplate(page);
    }

    /**
     * Render the given page using a literal String as a template.
     *
     * @param page the page to render
     * @param extension the extension that the content represents and should be compiled against
     * @param templateString the template string to render
     * @return true if the page was successfully rendered, false otherwise
     */
    default boolean renderString(OrchidPage page, String extension, String templateString) {
        return getService(OrchidRenderService.class).renderString(page, extension, templateString);
    }

    /**
     * Render the content of a page directly, without any template.
     *
     * @param page the page to render
     * @return true if the page was successfully rendered, false otherwise
     */
    default boolean renderRaw(OrchidPage page) {
        return getService(OrchidRenderService.class).renderRaw(page);
    }

    /**
     * Render the content of a page directly, as a binary stream. No further processing is performed on the file
     * contents, so as to preserve the binary format.
     *
     * @param page the page to render
     * @return true if the page was successfully rendered, false otherwise
     */
    default boolean renderBinary(OrchidPage page) {
        return getService(OrchidRenderService.class).renderBinary(page);
    }

    /**
     * Internal representation of a 'render' operation.
     *
     * @param page the page to render
     * @param extension the extension that the content represents and should be compiled against
     * @param content the template string to render
     * @return true if the page was successfully rendered, false otherwise
     */
    default boolean render(OrchidPage page, String extension, String content) {
        return getService(OrchidRenderService.class).render(page, extension, content);
    }

    /**
     * Internal representation of a 'render' operation on a binary stream.
     *
     * @param page the page to render
     * @param extension the extension that the content represents and should be compiled against
     * @param content the template string to render
     * @return true if the page was successfully rendered, false otherwise
     */
    default boolean render(OrchidPage page, String extension, InputStream content) {
        return getService(OrchidRenderService.class).render(page, extension, content);
    }

}
