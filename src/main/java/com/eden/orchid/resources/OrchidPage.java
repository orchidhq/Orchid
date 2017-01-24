package com.eden.orchid.resources;

import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class OrchidPage {

    private OrchidReference reference;

    private OrchidPage next;
    private OrchidPage previous;

    private String description;
    private String alias;

    private JSONObject data;
    private JSONArray menu;

    private OrchidResource resource;

    public OrchidPage(OrchidResource resource) {
        this.resource = resource;

        this.reference = resource.getReference();

        if(!OrchidUtils.isEmpty(resource.queryEmbeddedData("description"))) {
            this.description = resource.queryEmbeddedData("description").toString();
        }

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

        String outputPath = reference.getFullPath();
        String outputName = reference.getFileName() + "." + reference.getOutputExtension();
        OrchidResources.writeFile(outputPath.toLowerCase(), outputName.toLowerCase(), content);

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

        if(!OrchidUtils.isEmpty(reference.getTitle())) {
            pageData.put("title", reference.getTitle());
        }

        if(!OrchidUtils.isEmpty(description)) {
            pageData.put("description", description);
        }

        if(resource != null && !OrchidUtils.isEmpty(resource.getContent())) {
            String compiledContent = Orchid.getTheme().compile(
                    reference.getExtension(),
                    resource.getContent()
            );

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

            pageData.put("title", page.getReference().getTitle());
            pageData.put("description", page.getDescription());
            pageData.put("url", page.getReference().toString());

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public OrchidResource getResource() {
        return resource;
    }

    public OrchidReference getReference() {
        return reference;
    }

    public void setReference(OrchidReference reference) {
        this.reference = reference;
    }

    public void setResource(OrchidResource resource) {
        this.resource = resource;
    }
}
