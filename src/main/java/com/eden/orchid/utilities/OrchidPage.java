package com.eden.orchid.utilities;

import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.resources.OrchidResource;
import com.eden.orchid.utilities.resources.OrchidResources;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.File;

public class OrchidPage {

    private OrchidPage next;
    private OrchidPage previous;

    private String title;
    private String description;
    private String url;
    private String alias;

    private String fileName;
    private String filePath;

    private JSONObject data;
    private JSONObject menu;

    public void renderTemplate(String templateName) {
        render(templateName, FilenameUtils.getExtension(templateName), true);
    }

    public void renderString(String template, String extension) {
        render(template, extension, false);
    }

    private boolean render(String template, String extension, boolean templateReference) {
        String templateContent = "";
        if(templateReference) {
            OrchidResource templateResource = OrchidResources.getResourceEntry(template);

            if(templateResource == null) {
                templateResource = OrchidResources.getResourceEntry("templates/pages/index.twig");
            }
            if(templateResource == null) {
                templateResource = OrchidResources.getResourceEntry("templates/pages/index.html");
            }
            if(templateResource == null) {
                return false;
            }
            
            templateContent = templateResource.getContent();
        }
        else {
            templateContent = (OrchidUtils.isEmpty(template)) ? "" : template;
        }

        JSONObject pageData = buildPageData();

        JSONObject templateVariables = new JSONObject(Orchid.getRoot().toMap());
        pageData.put("page", pageData);
        if(!OrchidUtils.isEmpty(alias)) {
            pageData.put(alias, pageData);
        }

        String content = Orchid.getTheme().compile(extension, templateContent, templateVariables);
        OrchidResources.writeFile(filePath, fileName, content);

        return true;
    }

    private JSONObject buildPageData() {
        JSONObject pageData = new JSONObject();
        if(data != null) {
            for(String key : data.keySet()) {
                pageData.put(key, data.get(key));
            }
        }
        if(menu != null) {
            pageData.put("menu", menu);
        }

        JSONObject previousData = buildLink(previous);
        if(previousData != null) {
            pageData.put("previous", previousData);
        }
        JSONObject nextData = buildLink(next);
        if(nextData != null) {
            pageData.put("next", nextData);
        }

        pageData.put("title", (OrchidUtils.isEmpty(title) ? "" : title));
        pageData.put("description", (OrchidUtils.isEmpty(description) ? "" : description));

        return pageData;
    }

    private JSONObject buildLink(OrchidPage page) {
        if(page != null) {
            JSONObject pageData = new JSONObject();

            pageData.put("title", page.getTitle());
            pageData.put("description", page.getDescription());
            pageData.put("url", page.getUrl());

            return pageData;
        }

        return null;
    }

// Getters and Setters
//----------------------------------------------------------------------------------------------------------------------
    public OrchidPage getNext() {
        return next;
    }

    public void setNext(OrchidPage next) {
        this.next = next;
    }

    public OrchidPage getPrevious() {
        return previous;
    }

    public void setPrevious(OrchidPage previous) {
        this.previous = previous;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        String baseUrl = "";
        if(Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }
        if(!url.startsWith(baseUrl)) {
            this.url = baseUrl + File.separator + baseUrl;
        }
        else {
            this.url = url;
        }
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getMenu() {
        return menu;
    }

    public void setMenu(JSONObject menu) {
        this.menu = menu;
    }
}
