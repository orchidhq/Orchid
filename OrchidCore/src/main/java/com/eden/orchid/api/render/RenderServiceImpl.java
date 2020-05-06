package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.resources.resource.InlineResource;
import com.eden.orchid.api.theme.AbstractTheme;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.relations.ThemeRelation;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Supplier;

@Description(value = "How Orchid renders pages and writes them to disk.", name = "Render")
@Archetype(value = ConfigArchetype.class, key = "services.renderer")
public class RenderServiceImpl implements RenderService {

// Class fields
//----------------------------------------------------------------------------------------------------------------------

    protected OrchidContext context;
    protected final OrchidRenderer renderer;

    @Option
    @BooleanDefault(false)
    @Description("On a dry run, pages are indexed but not rendered.")
    public boolean dry;

    @Option
    @BooleanDefault(false)
    @Description("Normally, draft pages are not rendered along with the rest of the site, but this behavior can be turned off by setting this value to `true`.")
    public boolean includeDrafts;

// Constructors and initialization
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public RenderServiceImpl(OrchidContext context, OrchidRenderer renderer) {
        this.context = context;
        this.renderer = renderer;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

// Public API
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public synchronized boolean render(OrchidPage page) {
        return renderInternal(page, () -> getRendered(page));
    }

    @Override
    public synchronized InputStream getRendered(OrchidPage page) {
        page.addPossibleTheme(0, context.getDefaultThemeRelation());
        OrchidGenerator generator = page.getGenerator();
        if(generator != null) {
            page.addPossibleTheme(10, generator.getTheme());
        }
        ThemeRelation pageThemeRelation = page.getThemeRelation();
        if(pageThemeRelation != null) {
            page.addPossibleTheme(20, pageThemeRelation);
        }

        return renderPageWithTheme(page, page.getTheme(), () -> {
            RenderMode renderMode = page.getPageRenderMode();
            switch (renderMode) {
                case TEMPLATE:
                    return getRenderedTemplate(page);

                case RAW:
                    return getRenderedRaw(page);

                case BINARY:
                    return getRenderedBinary(page);

                default:
                    throw new IllegalArgumentException("Dynamic RenderMode rendering must be one of [TEMPLATE, RAW, BINARY]");
            }
        });
    }

    @Override
    public synchronized boolean renderAsset(AssetPage asset) {
        boolean success = false;
        if (!(asset.getResource() instanceof InlineResource)) {
            success = render(asset);
        }
        asset.setRendered(true);
        return success;
    }

    @Override
    public boolean includeDrafts() {
        return this.includeDrafts;
    }

// Rendering modes
//----------------------------------------------------------------------------------------------------------------------

    private synchronized InputStream getRenderedTemplate(OrchidPage page) {
        page.setCurrent(true);
        String content = "" + page.renderInLayout();
        page.setCurrent(false);
        return toStream(content);
    }

    private synchronized InputStream getRenderedRaw(OrchidPage page) {
        page.setCurrent(true);
        String content = page.getResource().getContent();
        if (page.getResource().shouldPrecompile()) {
            content = context.compile(page.getResource(), page.getResource().getPrecompilerExtension(), content, page);
        }
        content = "" + context.compile(page.getResource(), page.getResource().getReference().getExtension(), content, page);
        page.setCurrent(false);
        return toStream(content);
    }

    private synchronized InputStream getRenderedBinary(OrchidPage page) {
        page.setCurrent(true);
        InputStream is = page.getResource().getContentStream();
        page.setCurrent(false);
        return is;
    }

// Rendering helpers/wrappers
//----------------------------------------------------------------------------------------------------------------------

    private synchronized <T> T renderPageWithTheme(@Nonnull OrchidPage page, @Nonnull AbstractTheme pageTheme, Supplier<T> cb) {
        if(page == null) throw new NullPointerException("Page cannot be null");
        if(pageTheme == null) throw new NullPointerException("Page theme cannot be null");
        if(context.getState().isPreBuildState()) throw new IllegalStateException("Cannot render pages until after indexing has completed");

        if(pageTheme instanceof Theme) {
            context.pushTheme((Theme) pageTheme);
        }
        else if(pageTheme instanceof AdminTheme) {
            context.pushAdminTheme((AdminTheme) pageTheme);
        }
        T result = pageTheme.doWithCurrentPage(page, cb);
        if(pageTheme instanceof Theme) {
            context.popTheme();
        }
        else if(pageTheme instanceof AdminTheme) {
            context.popAdminTheme();
        }
        return result;
    }

    private synchronized InputStream toStream(String content) {
        try {
            return IOUtils.toInputStream(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean skipPage(OrchidPage page) {
        return dry || (page.isDraft() && !includeDrafts) || !page.shouldRender();
    }

    private boolean renderInternal(OrchidPage page, Supplier<InputStream> factory) {
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
