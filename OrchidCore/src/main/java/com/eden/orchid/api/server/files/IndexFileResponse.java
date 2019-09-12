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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class IndexFileResponse {

    // TODO: convert this to a component
    public OrchidResponse getResponse(OrchidContext context, File targetFile, String targetPath) {
        AssetHolder assetHolder = new AssetHolderDelegate(context, null, null);
        String content = "";

        if (targetFile.isDirectory()) {
            Clog.i("Rendering directory index: {}", targetPath);
            File[] files = targetFile.listFiles();

            if (files != null) {
                List<FileRow> jsonDirs = new ArrayList<>();
                List<FileRow> jsonFiles = new ArrayList<>();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss z", Locale.getDefault());

                jsonDirs.add(new FileRow(
                        OrchidUtils.applyBaseUrl(context, StringUtils.strip(targetPath, "/")) + "/..",
                        StringUtils.strip(targetPath, "/") + "/..",
                        ".. (Go up)",
                        "",
                        "",
                        assetHolder.addAsset("assets/svg/folder.svg")
                ));

                for (File file : files) {
                    FileRow newFile = new FileRow();
                    newFile.url = OrchidUtils.applyBaseUrl(context, StringUtils.strip(targetPath, "/") + "/" + file.getName());
                    newFile.path = StringUtils.strip(targetPath, "/") + "/" + file.getName();
                    newFile.name = file.getName();
                    newFile.date = formatter.format(new Date(file.lastModified()));
                    String icon;

                    if (file.isDirectory()) {
                        icon = "folder";
                        newFile.size = file.listFiles().length + " entries";
                        jsonDirs.add(newFile);
                    }
                    else {
                        icon = FilenameUtils.getExtension(file.getName());
                        newFile.size = humanReadableByteCount(file.length(), true);
                        jsonFiles.add(newFile);
                    }

                    String s = "assets/svg/" + icon + ".svg";

                    AssetPage iconPage = assetHolder.addAsset(s);

                    if(iconPage == null) {
                        iconPage = assetHolder.addAsset("assets/svg/file.svg");
                    }
                    newFile.icon = iconPage;
                }

                jsonDirs.sort(Comparator.comparing(fileRow -> fileRow.name));
                jsonFiles.sort(Comparator.comparing(fileRow -> fileRow.name));

                OrchidResource resource = context.getResourceEntry("templates/server/directoryListing.peb");

                Map<String, Object> indexPageVars = new HashMap<>();
                indexPageVars.put("title", "List of files/dirs under " + targetPath);
                indexPageVars.put("path", targetPath);
                indexPageVars.put("dirs", jsonDirs);
                indexPageVars.put("files", jsonFiles);

                Map<String, Object> object = new HashMap<>(context.getConfig());
                object.put("page", indexPageVars);
                object.put("theme", context.getTheme());

                String directoryListingContent;
                if (resource != null) {
                    directoryListingContent = context.compile(resource.getReference().getExtension(), resource.getContent(), object);
                }
                else {
                    directoryListingContent = context.serialize("json", object);
                }

                OrchidPage page = new OrchidPage(
                        new StringResource(context, "directoryListing.txt", directoryListingContent),
                        "directoryListing",
                        "Index"
                );

                InputStream is = context.getRenderedTemplate(page);
                try {
                    content = IOUtils.toString(is, Charset.forName("UTF-8"));
                }
                catch (IOException e) {
                    content = directoryListingContent;
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


    private static class FileRow {
        public String url;
        public String path;
        public String name;
        public String size;
        public String date;
        public AssetPage icon;

        public FileRow() {
        }

        public FileRow(String url, String path, String name, String size, String date, AssetPage icon) {
            this.url = url;
            this.path = path;
            this.name = name;
            this.size = size;
            this.date = date;
            this.icon = icon;
        }
    }
}
