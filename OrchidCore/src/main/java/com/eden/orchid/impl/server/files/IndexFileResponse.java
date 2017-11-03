package com.eden.orchid.impl.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class IndexFileResponse {

    private final OrchidContext context;
    private final Map<String, String> iconMap;
    private final TemplateResolutionStrategy strategy;

    @Inject
    public IndexFileResponse(OrchidContext context, TemplateResolutionStrategy strategy) {
        this.context = context;
        this.strategy = strategy;

        this.iconMap = new HashMap<>();

        iconMap.put("css", "/assets/svg/css.svg");
        iconMap.put("csv", "/assets/svg/csv.svg");
        iconMap.put("html", "/assets/svg/html.svg");
        iconMap.put("jpg", "/assets/svg/jpg.svg");
        iconMap.put("js", "/assets/svg/js.svg");
        iconMap.put("json", "/assets/svg/json.svg");
        iconMap.put("png", "/assets/svg/png.svg");
        iconMap.put("xml", "/assets/svg/xml.svg");

        iconMap.put("file", "/assets/svg/folder.svg");
        iconMap.put("folder", "/assets/svg/folder.svg");
    }

    public NanoHTTPD.Response getResponse(File targetFile, String targetPath) {
        String content = "";

        if (targetFile.isDirectory()) {
            Clog.i("Rendering directory index: {}", targetPath);
            File[] files = targetFile.listFiles();

            if (files != null) {
                JSONArray jsonDirs = new JSONArray();
                JSONArray jsonFiles = new JSONArray();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss z", Locale.getDefault());

                for (File file : files) {
                    JSONObject newFile = new JSONObject();
                    newFile.put("url", OrchidUtils.applyBaseUrl(context, StringUtils.strip(targetPath, "/") + "/" + file.getName()));
                    newFile.put("name", file.getName());
                    newFile.put("size", file.length());
                    newFile.put("date", formatter.format(new Date(file.lastModified())));

                    if (file.isDirectory()) {
                        newFile.put("icon", iconMap.get("folder"));
                        jsonDirs.put(newFile);
                    }
                    else {
                        newFile.put("icon",
                                iconMap.containsKey(FilenameUtils.getExtension(file.getName()))
                                        ? iconMap.get(FilenameUtils.getExtension(file.getName()))
                                        : iconMap.get("file")
                        );
                        jsonFiles.put(newFile);
                    }
                }

                OrchidResource resource = context.getResourceEntry("templates/server/directoryListing.twig");

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
                page.addCss(new OrchidPage(context.getResourceEntry("https://cdn.rawgit.com/milligram/milligram/master/dist/milligram.min.css"), ""));
                page.addCss("assets/css/directoryListing.css");
                for (String template : strategy.getPageLayout(page)) {
                    OrchidResource templateResource = context.getResourceEntry(template);
                    if (templateResource != null) {
                        content = "" + context.compile(FilenameUtils.getExtension(template), templateResource.getContent(), page);
                    }
                }
            }
        }

        return NanoHTTPD.newFixedLengthResponse(content);
    }
}
