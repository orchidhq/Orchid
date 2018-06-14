package com.eden.orchid.api.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class IndexFileResponse {

    private final OrchidContext context;
    private final AssetHolder assetHolder;

    @Inject
    public IndexFileResponse(OrchidContext context) {
        this.context = context;
        assetHolder = new AssetHolderDelegate(context, null, null);
    }

    public OrchidResponse getResponse(File targetFile, String targetPath) {
        String content = "";

        if (targetFile.isDirectory()) {
            Clog.i("Rendering directory index: {}", targetPath);
            File[] files = targetFile.listFiles();

            if (files != null) {
                JSONArray jsonDirs = new JSONArray();
                JSONArray jsonFiles = new JSONArray();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss z", Locale.getDefault());

                JSONObject parentFolder = new JSONObject();
                parentFolder.put("url", OrchidUtils.applyBaseUrl(context, StringUtils.strip(targetPath, "/")) + "/..");
                parentFolder.put("path", StringUtils.strip(targetPath, "/") + "/..");
                parentFolder.put("name", "..");
                parentFolder.put("size", "");
                parentFolder.put("date", "");
                parentFolder.put("icon", assetHolder.addAsset("assets/svg/folder.svg"));
                jsonDirs.put(parentFolder);

                for (File file : files) {
                    JSONObject newFile = new JSONObject();
                    newFile.put("url", OrchidUtils.applyBaseUrl(context, StringUtils.strip(targetPath, "/") + "/" + file.getName()));
                    newFile.put("path", StringUtils.strip(targetPath, "/") + "/" + file.getName());
                    newFile.put("name", file.getName());
                    newFile.put("date", formatter.format(new Date(file.lastModified())));
                    
                    String icon;

                    if (file.isDirectory()) {
                        icon = "folder";
                        newFile.put("size", file.listFiles().length + " entries");
                        jsonDirs.put(newFile);
                    }
                    else {
                        icon = FilenameUtils.getExtension(file.getName());
                        newFile.put("size", humanReadableByteCount(file.length(), true));
                        jsonFiles.put(newFile);
                    }

                    String s = "assets/svg/" + icon + ".svg";

                    AssetPage iconPage = assetHolder.addAsset(s);

                    if(iconPage == null) {
                        iconPage = assetHolder.addAsset("assets/svg/file.svg");
                    }
                    newFile.put("icon", iconPage);
                }

                OrchidResource resource = context.getResourceEntry("templates/server/directoryListing.peb");

                JSONObject indexPageVars = new JSONObject();
                indexPageVars.put("title", "List of files/dirs under " + targetPath);
                indexPageVars.put("path", targetPath);
                indexPageVars.put("dirs", jsonDirs);
                indexPageVars.put("files", jsonFiles);

                JSONObject object = new JSONObject(context.getOptionsData().toMap());
                object.put("page", indexPageVars);
                object.put("theme", context.getTheme());

                String directoryListingContent;
                if (resource != null) {
                    directoryListingContent = context.compile(resource.getReference().getExtension(), resource.getContent(), object.toMap());
                }
                else {
                    directoryListingContent = object.toString(2);
                }

                OrchidPage page = new OrchidPage(new StringResource(context, "directoryListing.txt", directoryListingContent), "directoryListing");
                page.addJs("assets/js/shadowComponents.js");
                assetHolder.addCss("assets/css/directoryListing.css");

                InputStream is = context.getRenderedTemplate(page);
                try {
                    content = IOUtils.toString(is, Charset.forName("UTF-8"));
                }
                catch (Exception e) {
                    content = "";
                }
            }
        }

        return new OrchidResponse(context).content(content);
    }

    private String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
