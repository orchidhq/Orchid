package com.eden.orchid.server.server.file;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.server.server.RequestHandler;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

public class FileHandler implements RequestHandler {

    private OrchidContext context;

    private RenderIndex renderIndex;
    private RenderFile renderFile;
    private Render404 render404;

    private File rootFolder;

    private String[] indexFiles = new String[]{"index.html", "index.htm"};

    @Inject
    public FileHandler(
            OrchidContext context,
            RenderIndex renderIndex,
            RenderFile renderFile,
            Render404 render404) {
        this.context = context;
        this.renderIndex = renderIndex;
        this.renderFile = renderFile;
        this.render404 = render404;
    }

    /**
     * The FileHandler is the last to run. If it can't find the file, it will at least render a 404.
     * @param t
     * @param targetPath
     * @return
     */
    @Override
    public boolean canHandle(HttpExchange t, String targetPath) {
        return true;
    }

    @Override
    public void render(HttpExchange t, String targetPath) throws IOException {
        if(this.rootFolder == null) {
            String baseDir = context.query("options.d").toString();
            this.rootFolder = new File(baseDir);
        }

        // Check if file exists
        File targetFile = new File(rootFolder, targetPath);

        if (targetFile.exists()) {
            if (targetFile.isDirectory()) {

                boolean rendered = false;
                for (String indexFile : indexFiles) {
                    String indexPath = StringUtils.strip(targetPath, "/") + "/" + indexFile;

                    File targetIndexFile = new File(rootFolder, indexPath.replace('/', File.separatorChar));

                    if (targetIndexFile.exists()) {
                        renderFile.render(t, targetIndexFile, targetPath);
                        rendered = true;
                        break;
                    }
                }

                if (!rendered) {
                    renderIndex.render(t, targetFile, targetPath);
                }
            }
            else {
                renderFile.render(t, targetFile, targetPath);
            }
        }
        else {
            render404.render(t, targetPath);
        }
    }
}
