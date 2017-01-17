package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.resources.OrchidResources;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class OrchidPage {

    private OrchidPage next;
    private OrchidPage previous;

    private String title;
    private String description;
    private String alias;

    private String fileName;
    private String filePath;

    private JSONObject data;
    private JSONArray menu;

    private boolean usePrettyUrl;

    private OrchidResource resource;

    public OrchidPage(OrchidResource resource) {
        this.resource = resource;

        if(!OrchidUtils.isEmpty(resource.queryEmbeddedData("title"))) {
            this.title = resource.queryEmbeddedData("title").toString();
        }
        else {
            this.title = FilenameUtils.removeExtension(resource.getFileName());
        }

        if(!OrchidUtils.isEmpty(resource.queryEmbeddedData("description"))) {
            this.description = resource.queryEmbeddedData("description").toString();
        }

        this.fileName = resource.getFileName();
        this.filePath = resource.getFilePath();

        if(resource.getEmbeddedData().getElement() instanceof JSONObject) {
            this.data = (JSONObject) resource.getEmbeddedData().getElement();
        }
        else {
            this.data = new JSONObject();
            this.data.put("data", resource.getEmbeddedData().getElement());
        }

        if(resource.queryEmbeddedData("menu") != null) {
            if(resource.queryEmbeddedData("menu").getElement() instanceof JSONArray) {
                this.menu = (JSONArray) resource.queryEmbeddedData("menu").getElement();
            }
            else {
                this.menu = new JSONArray();
                this.menu.put(resource.queryEmbeddedData("menu").getElement());
            }
        }
    }

    /**
     * Render a template from a resource file
     *
     * @param templateName the full relative name of the template to render
     */
    public void renderTemplate(String templateName) {
        render(templateName, FilenameUtils.getExtension(templateName), true);
    }

    /**
     * Render a template from a string using the given file extension to find the appropriate compilers
     *
     * @param template the content of a 'template' to render
     * @param extension the extension representing the type of 'template'
     */
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
        templateVariables.put("page", pageData);
        if(!OrchidUtils.isEmpty(alias)) {
            templateVariables.put(alias, pageData);
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

        if(!OrchidUtils.isEmpty(title)) {
            pageData.put("title", title);
        }

        if(!OrchidUtils.isEmpty(description)) {
            pageData.put("description", description);
        }

        if(resource != null && !OrchidUtils.isEmpty(resource.getContent())) {
            Clog.v("resource content: " + resource.getContent());

            String compiledContent = Orchid.getTheme().compile(
                    FilenameUtils.getExtension(resource.getFileName()),
                    resource.getContent()
            );

            Clog.v("compiled resource content: " + compiledContent);

            pageData.put("content", compiledContent);
        }
        else {
            pageData.put("content", "");
        }

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
        String baseUrl = "";
        if(Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }

        String basePath;

        if(!filePath.startsWith(baseUrl)) {
            basePath = StringUtils.strip(baseUrl, File.separator + "/")
                    + File.separator
                    + StringUtils.strip(filePath, File.separator + "/");
        }
        else {
            basePath = StringUtils.strip(filePath, File.separator + "/");
        }

        if(usePrettyUrl) {
            return basePath
                    + File.separator
                    + StringUtils.strip(FilenameUtils.removeExtension(fileName), File.separator + "/")
                    + File.separator;
        }
        else {
            return basePath
                    + File.separator
                    + StringUtils.strip(fileName, File.separator + "/");
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

    public JSONArray getMenu() {
        return menu;
    }

    public void setMenu(JSONArray menu) {
        this.menu = menu;
    }

    public boolean isUsePrettyUrl() {
        return usePrettyUrl;
    }

    public void setUsePrettyUrl(boolean usePrettyUrl) {
        this.usePrettyUrl = usePrettyUrl;
    }
}
