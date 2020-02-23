package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.ImplementedBy;

import java.io.InputStream;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@ImplementedBy(RenderServiceImpl.class)
public interface RenderService extends OrchidService {

    enum RenderMode {
        TEMPLATE, RAW, BINARY
    }

    /**
     * Whether to exclude drafts, which is the default behavior, or whether to include them. 
     *
     * @since v1.0.0
     */
    default boolean includeDrafts() {
        return getService(RenderService.class).includeDrafts();
    }

    /**
     * Dynamically use a RenderMode enum to determine which rendering operation to perform.
     *
     * @param page       the page to render
     * @return if the page was not skipped, the result of {@link OrchidRenderer} indicating whether the page was successfully rendered, false otherwise
     * @since v1.0.0
     */
    default boolean render(final OrchidPage page) {
        return getService(RenderService.class).render(page);
    }

    /**
     * Dynamically use a RenderMode enum to determine which rendering operation to perform.
     *
     * @param page       the page to render
     * @return InputStream the stream used to create side-effects by the render operation
     * @since v1.0.0
     */
    default InputStream getRendered(OrchidPage page) {
        return getService(RenderService.class).getRendered(page);
    }

    /**
     * Renders an AssetPage. If the page represents a binary asset, it will be rendered with {@link #renderBinary(OrchidPage)},
     * otherwise it will be rendered with {@link #renderRaw(OrchidPage)}
     *
     * @param asset the asset to render
     * @return if the page was not skipped, the result of {@link OrchidRenderer} indicating whether the page was successfully rendered, false otherwise
     * @since v1.0.0
     */
    default boolean renderAsset(final AssetPage asset) {
        return getService(RenderService.class).renderAsset(asset);
    }

}
