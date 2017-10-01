package com.eden.orchid.impl.generators;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.Description;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public final class AssetsGenerator extends OrchidGenerator {

    @Option
    @Description("Set which local resource directories you want to copy static assets from.")
    public String[] sourceDirs;

    @Inject
    public AssetsGenerator(OrchidContext context) {
        super(context, "assets", 2);
    }

    @Override
    public String getDescription() {
        return "Prints all assets to the output directory. This is all scripts and styles (after compilation) that have been registered to an AssetHolder, and all binary files in the 'assets/images'";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidPage> assets = new ArrayList<>();

        if (!EdenUtils.isEmpty(sourceDirs)) {
            for (String sourceDir : sourceDirs) {
                List<OrchidResource> resourcesList = context.getResourceEntries(sourceDir, null, true);
                for (OrchidResource entry : resourcesList) {
                    OrchidPage page = new OrchidPage(entry, entry.getReference().getFileName());
                    page.getReference().setUsePrettyUrl(false);
                    assets.add(page);
                }
            }
        }

        return assets;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.stream()
             .filter(page -> context.isBinaryExtension(page.getReference().getOutputExtension()))
             .forEach(context::renderBinary);
        pages.stream()
             .filter(OrchidUtils.not(page -> context.isBinaryExtension(page.getReference().getOutputExtension())))
             .forEach(context::renderRaw);

        context.getGlobalAssetHolder()
               .getScripts()
               .stream()
               .forEach(context::renderRaw);
        context.getGlobalAssetHolder()
               .getStyles()
               .stream()
               .forEach(context::renderRaw);

        context.getDefaultTheme()
               .getScripts()
               .stream()
               .forEach(context::renderRaw);
        context.getDefaultTheme()
               .getStyles()
               .stream()
               .forEach(context::renderRaw);
    }
}

