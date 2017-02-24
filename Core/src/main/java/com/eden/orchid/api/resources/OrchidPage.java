package com.eden.orchid.api.resources;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

@Data
public class OrchidPage {

    protected OrchidContext context;

    private OrchidReference reference;

    private OrchidPage next;
    private OrchidPage previous;

    private String description;
    private String type;

    private JSONObject data;
    private JSONArray menu;

    private OrchidResource resource;

    // injected members
    private OrchidRenderer renderer;

    public OrchidPage(OrchidResource resource) {
        this.context = resource.getContext();
        this.context.getInjector().injectMembers(this);

        this.resource = resource;

        this.reference = new OrchidReference(this.context, resource.getReference());
        this.reference.setExtension(resource.getReference().getOutputExtension());

        if (resource.getEmbeddedData() != null) {

            if (!EdenUtils.isEmpty(resource.queryEmbeddedData("description"))) {
                this.description = resource.queryEmbeddedData("description").toString();
            }

            if (resource.getEmbeddedData().getElement() instanceof JSONObject) {
                this.data = (JSONObject) resource.getEmbeddedData().getElement();
            }
            else {
                this.data = new JSONObject();
                this.data.put("data", resource.getEmbeddedData().getElement());
            }

            if (resource.queryEmbeddedData("menu") != null) {
                if (resource.queryEmbeddedData("menu").getElement() instanceof JSONArray) {
                    this.menu = (JSONArray) resource.queryEmbeddedData("menu").getElement();
                }
                else {
                    this.menu = new JSONArray();
                    this.menu.put(resource.queryEmbeddedData("menu").getElement());
                }
            }
        }
        else {
            this.data = new JSONObject();
        }
    }

    /**
     * Render a template from a resource file
     *
     * @param templateName the full relative name of the template to render
     */
    public void renderTemplate(String... templateName) {
        this.data = buildPageData();

        String pageContent = getContent();

        this.data.put("content", pageContent);

        this.data.getJSONObject("page").put("content", pageContent);
        if (!EdenUtils.isEmpty(type)) {
            this.data.getJSONObject(type).put("content", pageContent);
        }

        renderer.renderTemplate(this, templateName);
    }

    public void renderString(String extension, String templateString) {
        this.data = buildPageData();

        String pageContent = getContent();

        this.data.put("content", pageContent);

        this.data.getJSONObject("page").put("content", pageContent);
        if (!EdenUtils.isEmpty(type)) {
            this.data.getJSONObject(type).put("content", pageContent);
        }

        renderer.renderString(this, extension, templateString);
    }

    /**
     * Render the contents of your resource directly
     */
    public void renderRaw() {
        this.data = buildPageData();

        renderer.renderRaw(this);
    }

    private JSONObject buildPageData() {
        JSONObject pageData;
        if (data != null) {
            pageData = new JSONObject(data.toMap());
        }
        else {
            pageData = new JSONObject();
        }

        if (menu != null) {
            pageData.put("menu", menu);
        }

        pageData.put("type", (!EdenUtils.isEmpty(type)) ? type : "page");

        JSONObject previousData = buildLink(previous);
        if (previousData != null) {
            pageData.put("previous", previousData);
        }
        JSONObject nextData = buildLink(next);
        if (nextData != null) {
            pageData.put("next", nextData);
        }

        if (!EdenUtils.isEmpty(reference.getTitle())) {
            pageData.put("title", reference.getTitle());
        }

        if (!EdenUtils.isEmpty(description)) {
            pageData.put("description", description);
        }

        JSONObject pageObjectData = new JSONObject(pageData.toMap());
        pageData.put("page", pageObjectData);
        if (!EdenUtils.isEmpty(type)) {
            pageData.put(type, pageObjectData);
        }

        return pageData;
    }

    private JSONObject buildLink(OrchidPage page) {
        if (page != null) {
            JSONObject pageData = new JSONObject();

            pageData.put("title", page.getReference().getTitle());
            pageData.put("description", page.getDescription());
            pageData.put("url", page.getReference().toString());

            return pageData;
        }

        return null;
    }

    public String getContent() {
        if (resource != null && !EdenUtils.isEmpty(resource.getContent())) {
            String compiledContent = resource.getContent();

            if(resource.shouldPrecompile()) {
                compiledContent = context.getTheme().precompile(compiledContent, data);
            }

            return context.getTheme().compile(
                    resource.getReference().getExtension(),
                    compiledContent
            );
        }
        else {
            return "";
        }
    }

    @Inject
    public void setRenderer(OrchidRenderer renderer) {
        this.renderer = renderer;
    }
}
