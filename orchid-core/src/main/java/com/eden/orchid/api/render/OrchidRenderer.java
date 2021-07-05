package com.eden.orchid.api.render;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.ImplementedBy;

import java.io.InputStream;

@ImplementedBy(FileRenderer.class)
public interface OrchidRenderer {

    /**
     * Internal representation of a 'render' operation on a binary stream, producing a side-effect as the intended final
     * output.
     *
     * @param page the page to render
     * @param content the template string to render
     * @return true if the page was successfully rendered, false otherwise
     *
     * @since v1.0.0
     */
    boolean render(OrchidPage page, InputStream content);

}
