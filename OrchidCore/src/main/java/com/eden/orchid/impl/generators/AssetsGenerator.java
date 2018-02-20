package com.eden.orchid.impl.generators;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.tasks.TaskService;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Description("Prints all assets to the output directory. This is all scripts and styles (after compilation) that have " +
        "been registered to an AssetHolder, and all files in the directories specified in config.sourceDirs.")
public final class AssetsGenerator extends OrchidGenerator {

    @Option @StringDefault({"assets"})
    @Description("Set which local resource directories you want to copy static assets from.")
    public String[] sourceDirs;

    private List<? extends OrchidPage> assetPages;

    @Inject
    public AssetsGenerator(OrchidContext context) {
        super(context, "assets", OrchidGenerator.PRIORITY_LATE);
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        if (EdenUtils.isEmpty(sourceDirs)) {
            sourceDirs = new String[]{"assets"};
        }

        assetPages = Arrays
                .stream(sourceDirs)
                .flatMap(sourceDir -> context.getLocalResourceEntries(sourceDir, null, true).stream())
                .map(orchidResource -> {
                    OrchidPage page = new OrchidPage(orchidResource, orchidResource.getReference().getFileName());
                    page.getReference().setUsePrettyUrl(false);
                    return page;
                }).collect(Collectors.toList());

        return null;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        assetPages.stream().forEach(page -> {
            if (context.isBinaryExtension(page.getReference().getOutputExtension())) {
                context.renderBinary(page);
            }
            else {
                context.renderRaw(page);
            }
        });

        List<AssetHolder> assetHoldersToRender = new ArrayList<>();
        assetHoldersToRender.add(context.getGlobalAssetHolder());
        assetHoldersToRender.add(context.getDefaultTheme());
        if (context.getTaskType() == TaskService.TaskType.SERVE) {
            assetHoldersToRender.add(context.getDefaultAdminTheme());
        }

        for(AssetHolder holder : assetHoldersToRender) {
            holder.getScripts().forEach(context::renderRaw);
            holder.getStyles().forEach(context::renderRaw);
        }
    }

    @Override
    public List<OrchidCollection> getCollections() {
        return null;
    }

}

