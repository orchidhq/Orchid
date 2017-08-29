package com.eden.orchid.impl.server.files;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.server.OrchidFileController;
import com.google.inject.name.Named;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;

public class FileController implements OrchidFileController {

    private OrchidContext context;

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
            OrchidContext context,
            @Named("d") String destination,
            StaticFileResponse staticFileResponse,
            IndexFileResponse indexFileResponse,
            NotFound404Response notFound404Response,
            FaviconResponse faviconResponse,
            AdminAssetResponse adminAssetResponse) {
        this.context = context;
        this.staticFileResponse = staticFileResponse;
        this.indexFileResponse = indexFileResponse;
        this.notFound404Response = notFound404Response;
        this.faviconResponse = faviconResponse;
        this.adminAssetResponse = adminAssetResponse;

        this.destination = destination;
    }

    public NanoHTTPD.Response findFile(String targetPath) {
        if(this.rootFolder == null) {
            this.rootFolder = new File(this.destination);
        }

        if(targetPath.equalsIgnoreCase("favicon.ico")) {
            return faviconResponse.getResponse(targetPath);
        }

        File targetFile = new File(rootFolder, targetPath);

        if (targetFile.exists()) {
            if (targetFile.isDirectory()) {
                for (String indexFile : indexFiles) {
                    String indexPath = StringUtils.strip(targetPath, "/") + "/" + indexFile;
                    File targetIndexFile = new File(rootFolder, indexPath.replace('/', File.separatorChar));
                    if (targetIndexFile.exists()) {
                        return staticFileResponse.getResponse(targetIndexFile, indexPath);
                    }
                }

                return indexFileResponse.getResponse(targetFile, targetPath);
            }
            else {
                return staticFileResponse.getResponse(targetFile, targetPath);
            }
        }
        else {
            NanoHTTPD.Response adminAsset = adminAssetResponse.getResponse(targetFile, targetPath);
            if(adminAsset != null) {
                return adminAsset;
            }
            else {
                return notFound404Response.getResponse(targetPath);
            }
        }
    }
}
