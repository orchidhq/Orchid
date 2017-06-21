package com.eden.orchid.server.impl.controllers.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.google.inject.name.Named;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;

public class FileController {

    private OrchidContext context;

    private File rootFolder;

    private String[] indexFiles = new String[]{"index.html", "index.htm"};

    private StaticFileResponse staticFileResponse;
    private IndexFileResponse indexFileResponse;
    private NotFound404Response notFound404Response;
    private FaviconResponse faviconResponse;

    private final String destination;

    @Inject
    public FileController(
            OrchidContext context,
            @Named("d") String destination,
            StaticFileResponse staticFileResponse,
            IndexFileResponse indexFileResponse,
            NotFound404Response notFound404Response,
            FaviconResponse faviconResponse) {
        this.context = context;
        this.staticFileResponse = staticFileResponse;
        this.indexFileResponse = indexFileResponse;
        this.notFound404Response = notFound404Response;
        this.faviconResponse = faviconResponse;

        this.destination = destination;
    }

    public NanoHTTPD.Response findFile(String targetPath) {
        if(this.rootFolder == null) {
            this.rootFolder = new File(this.destination);
        }

        Clog.v("Target path: '#{$1}'", new Object[]{targetPath});
        if(targetPath.equalsIgnoreCase("favicon.ico")) {
            Clog.v("Attempting to locate Favicon");
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
            return notFound404Response.getResponse(targetPath);
        }
    }
}
