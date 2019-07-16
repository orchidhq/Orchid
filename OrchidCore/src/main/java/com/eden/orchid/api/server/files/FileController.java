package com.eden.orchid.api.server.files;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.server.OrchidFileController;
import com.eden.orchid.api.server.OrchidResponse;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;

public final class FileController implements OrchidFileController {

    private File rootFolder;

    private String[] indexFiles = new String[]{"index.html", "index.htm"};

    private StaticFileResponse staticFileResponse;
    private IndexFileResponse indexFileResponse;
    private NotFound404Response notFound404Response;
    private FaviconResponse faviconResponse;
    private AdminAssetResponse adminAssetResponse;

    private final String destination;

    @Inject
    public FileController(
            @Named("dest") String destination,
            StaticFileResponse staticFileResponse,
            IndexFileResponse indexFileResponse,
            NotFound404Response notFound404Response,
            FaviconResponse faviconResponse,
            AdminAssetResponse adminAssetResponse) {
        this.staticFileResponse = staticFileResponse;
        this.indexFileResponse = indexFileResponse;
        this.notFound404Response = notFound404Response;
        this.faviconResponse = faviconResponse;
        this.adminAssetResponse = adminAssetResponse;

        this.destination = destination;
    }

    public OrchidResponse findFile(OrchidContext context, String targetPath) {
        if(this.rootFolder == null) {
            this.rootFolder = new File(this.destination);
        }

        if(targetPath.equalsIgnoreCase("favicon.ico")) {
            return faviconResponse.getResponse(context, targetPath);
        }

        File targetFile = new File(rootFolder, targetPath);

        if (targetFile.exists()) {
            if (targetFile.isDirectory()) {
                for (String indexFile : indexFiles) {
                    String indexPath = StringUtils.strip(targetPath, "/") + "/" + indexFile;
                    File targetIndexFile = new File(rootFolder, indexPath.replace('/', File.separatorChar));
                    if (targetIndexFile.exists()) {
                        return staticFileResponse.getResponse(context, targetIndexFile, indexPath);
                    }
                }

                return indexFileResponse.getResponse(context, targetFile, targetPath);
            }
            else {
                return staticFileResponse.getResponse(context, targetFile, targetPath);
            }
        }
        else {
            OrchidResponse adminAsset = adminAssetResponse.getResponse(context, targetFile, targetPath);
            if(adminAsset != null) {
                return adminAsset;
            }
            else {
                return notFound404Response.getResponse(context, targetPath);
            }
        }
    }
}
