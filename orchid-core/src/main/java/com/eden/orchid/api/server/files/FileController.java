package com.eden.orchid.api.server.files;

import clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidRootIndex;
import com.eden.orchid.api.server.OrchidFileController;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import com.eden.orchid.utilities.StreamUtilsKt;
import com.google.inject.name.Named;
import kotlin.collections.CollectionsKt;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public final class FileController implements OrchidFileController {

    private File rootFolder;

    private String[] indexFiles = new String[]{"index.html", "index.htm"};

    private final String destination;

    @Inject
    public FileController(@Named("dest") String destination) {
        this.destination = destination;
    }

    @Override
    public OrchidResponse findFile(OrchidContext context, String targetPath) {
        if(this.rootFolder == null) {
            this.rootFolder = new File(this.destination);
        }

        File targetFile = new File(rootFolder, targetPath);

        if (targetFile.exists()) {
            if (targetFile.isDirectory()) {
                for (String indexFile : indexFiles) {
                    String indexPath = StringUtils.strip(targetPath, "/") + "/" + indexFile;
                    File targetIndexFile = new File(rootFolder, indexPath.replace('/', File.separatorChar));
                    if (targetIndexFile.exists()) {
                        return StaticFileResponse.getResponse(context, targetIndexFile, indexPath);
                    }
                }

                return IndexFileResponse.getResponse(context, targetFile, targetPath);
            }
            else {
                return StaticFileResponse.getResponse(context, targetFile, targetPath);
            }
        }
        else {
            OrchidResponse adminAsset = AdminAssetResponse.getResponse(context, targetFile, targetPath);
            if(adminAsset != null) {
                return adminAsset;
            }
            else {
                return NotFound404Response.getResponse(context, targetPath);
            }
        }
    }

    @Override
    public OrchidResponse findPage(OrchidContext context, String targetPath) {
        List<String> possibleMatches = CollectionsKt.listOf(
                targetPath,
                targetPath + "/" + indexFiles[0],
                targetPath + "/" + indexFiles[1]
        );

        OrchidPage matchedPage = null;
        for(String possibleMatch : possibleMatches) {
            Stream<OrchidPage> indexedPagesStream = context
                    .getIndex()
                    .getAllIndexedPages()
                    .values()
                    .stream()
                    .flatMap(it -> it.getFirst().getAllPages().stream());

            Stream<AssetPage> assetPagesStream = context
                    .getAssetManager()
                    .getAllAssetPages();

            Stream<OrchidPage> allOrchidPagesStream = Stream.concat(indexedPagesStream, assetPagesStream);

            OrchidPage page = OrchidExtensionsKt.findPageByServerPath(allOrchidPagesStream, possibleMatch);

            if(page != null) {
                matchedPage = page;
                break;
            }
        }

        if(matchedPage != null) {
            return new OrchidResponse(context).page(matchedPage).status(200);
        }
        else {
            return findFile(context, targetPath);
        }
    }
}
