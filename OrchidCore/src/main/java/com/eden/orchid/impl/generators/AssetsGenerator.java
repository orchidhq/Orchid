package com.eden.orchid.impl.generators;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Singleton
@Description("Prints all assets to the output directory. This is all scripts and styles (after compilation) that have " +
        "been registered to an AssetHolder, and all files in the directories specified in config.sourceDirs.")
public final class AssetsGenerator extends OrchidGenerator {

    public static final String GENERATOR_KEY = "assets";

    @Getter @Setter
    @Option
    @Description("Set which local resource directories you want to copy static assets from.")
    private List<AssetDirectory> sourceDirs;

    @Inject
    public AssetsGenerator(OrchidContext context) {
        super(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_INIT);
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        if(EdenUtils.isEmpty(sourceDirs)) {
            AssetDirectory dir = new AssetDirectory();
            dir.setSourceDir("assets");
            dir.setAssetFileExtensions(null);
            dir.setRecursive(true);
            sourceDirs = Collections.singletonList(dir);
        }

        sourceDirs.stream()
                .flatMap( dir      -> context.getLocalResourceEntries(dir.sourceDir, (!EdenUtils.isEmpty(dir.assetFileExtensions)) ? dir.assetFileExtensions : null, dir.recursive).stream())
                .map(     resource -> new AssetPage(null, null, resource, resource.getReference().getFileName()))
                .peek(    asset    -> asset.getReference().setUsePrettyUrl(false))
                .forEach( asset    -> context.getGlobalAssetHolder().addAsset(asset));

        return null;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {

    }

    @Override
    public List<OrchidCollection> getCollections() {
        return null;
    }

// Helpers
//----------------------------------------------------------------------------------------------------------------------

    @Getter @Setter
    public static class AssetDirectory implements OptionsHolder {

        @Option
        @StringDefault("assets")
        @Description("Set which local resource directories you want to copy static assets from.")
        private String sourceDir;

        @Option
        @Description("Restrict the file extensions used for the assets in this directory.")
        private String[] assetFileExtensions;

        @Option @BooleanDefault(true)
        @Description("Whether to include subdirectories of this directory")
        private boolean recursive;

    }

}

