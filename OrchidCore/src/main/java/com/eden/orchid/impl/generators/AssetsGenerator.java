package com.eden.orchid.impl.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AssetsGenerator extends OrchidGenerator {

    @Inject
    public AssetsGenerator(OrchidContext context, OrchidRenderer renderer) {
        super(2, "assets", context, renderer);
    }

    @Override
    public String getDescription() {
        return "Prints all assets to the output directory. This is all scripts and styles (after compilation) that have been registered to an AssetHolder, and all binary files in the 'assets/images'";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidResource> resourcesList = context.getResourceEntries("assets/images", null, true);
        List<OrchidPage> assets = new ArrayList<>();
        for (OrchidResource entry : resourcesList) {
            OrchidPage page = new OrchidPage(entry, "asset");
            page.getReference().setUsePrettyUrl(false);
            assets.add(page);
        }
        return assets;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.stream()
             .filter(page -> context.isBinaryExtension(page.getReference().getOutputExtension()))
             .forEach(renderer::renderBinary);
        pages.stream()
             .filter(OrchidUtils.not(page -> context.isBinaryExtension(page.getReference().getOutputExtension())))
             .forEach(renderer::renderRaw);

        context.getGlobalAssetHolder()
               .getScripts()
               .stream()
               .forEach(renderer::renderRaw);
        context.getGlobalAssetHolder()
               .getStyles()
               .stream()
               .forEach(renderer::renderRaw);

        context.getDefaultTheme()
               .getScripts()
               .stream()
               .forEach(renderer::renderRaw);
        context.getDefaultTheme()
               .getStyles()
               .stream()
               .forEach(renderer::renderRaw);
    }
}

