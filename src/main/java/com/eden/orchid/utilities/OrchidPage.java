package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.resources.OrchidResource;
import com.eden.orchid.utilities.resources.OrchidResources;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
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
    private String content;

    private JSONObject data;
    private JSONArray menu;

    private OrchidResource resource;

    public OrchidPage(String filePath, String fileName, String content) {
        this.title = FilenameUtils.removeExtension(fileName);
        this.url = filePath
                + File.separator
                + FilenameUtils.removeExtension(fileName)
                + "."
                + Orchid.getTheme().getOutputExtension(FilenameUtils.getExtension(fileName));

        this.filePath = filePath;
        this.fileName = fileName;

        this.content = content;
    }

    public OrchidPage(OrchidResource resource) {
        this.resource = resource;

        if(!OrchidUtils.isEmpty(resource.queryData("title"))) {
            this.title = resource.queryData("title").toString();
        }
        else {
            this.title = FilenameUtils.removeExtension(resource.getFileName());
        }

        if(!OrchidUtils.isEmpty(resource.queryData("description"))) {
            this.description = resource.queryData("description").toString();
        }

        if(!OrchidUtils.isEmpty(resource.queryData("url"))) {
            this.url = resource.queryData("url").toString();
        }
        else {
            this.url = resource.getPath()
                    + File.separator
                    + FilenameUtils.removeExtension(resource.getFileName())
                    + "."
                    + Orchid.getTheme().getOutputExtension(FilenameUtils.getExtension(resource.getFileName()));
        }

        this.fileName = resource.getFileName();
        this.filePath = resource.getPath();

        if(resource.getData().getElement() instanceof JSONObject) {
            this.data = (JSONObject) resource.getData().getElement();
        }
        else {
            this.data = new JSONObject();
            this.data.put("data", resource.getData().getElement());
        }

        if(resource.queryData("menu") != null) {
            if(resource.queryData("menu").getElement() instanceof JSONArray) {
                this.menu = (JSONArray) resource.queryData("menu").getElement();
            }
            else {
                this.menu = new JSONArray();
                this.menu.put(resource.queryData("menu").getElement());
            }
        }

        this.content = resource.getContent();
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
        else if(!OrchidUtils.isEmpty(content)) {
            Clog.v("content: " + content);
            String compiledContent = Orchid.getTheme().compile(
                    FilenameUtils.getExtension(fileName),
                    content
            );

            Clog.v("compiled content: " + compiledContent);

            pageData.put("content", compiledContent);

            this.fileName = FilenameUtils.removeExtension(this.fileName) + "." +
                    Orchid.getTheme().getOutputExtension(FilenameUtils.getExtension(fileName));
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
        return url;
    }

    public void setUrl(String url) {
        String baseUrl = "";
        if(Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }
        if(!url.startsWith(baseUrl)) {
            this.url = baseUrl + File.separator + filePath;
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

    public JSONArray getMenu() {
        return menu;
    }

    public void setMenu(JSONArray menu) {
        this.menu = menu;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void usePrettyUrl() {
        filePath = filePath + File.separator + FilenameUtils.removeExtension(fileName);
        fileName = "index." + Orchid.getTheme().getOutputExtension(FilenameUtils.getExtension(fileName));
        setUrl(filePath + File.separator);
    }
}
