package com.eden.orchid.impl.generators;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Singleton
@Description("Prints all assets to the output directory. This is all scripts and styles (after compilation) that have " +
        "been registered to an AssetHolder, and all files in the directories specified in config.sourceDirs.")
public final class AssetsGenerator extends OrchidGenerator {

    @Option
    @Description("Set which local resource directories you want to copy static assets from.")
    public String[] sourceDirs;

    @Inject
    public AssetsGenerator(OrchidContext context) {
        super(context, "assets", 2);
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
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        pages.forEach(page -> {
            if(context.isBinaryExtension(page.getReference().getOutputExtension())) {
                context.renderBinary(page);
            }
            else {
                context.renderRaw(page);
            }
        });

        context.getGlobalAssetHolder()
               .getScripts()
               .forEach(context::renderRaw);
        context.getGlobalAssetHolder()
               .getStyles()
               .forEach(context::renderRaw);

        context.getDefaultTheme()
               .getScripts()
               .forEach(context::renderRaw);
        context.getDefaultTheme()
               .getStyles()
               .forEach(context::renderRaw);
    }
}

