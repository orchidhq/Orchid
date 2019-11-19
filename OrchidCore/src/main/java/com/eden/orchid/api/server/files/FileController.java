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

    private final String destination;

    @Inject
    public FileController(@Named("dest") String destination) {
        this.destination = destination;
    }

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
}
