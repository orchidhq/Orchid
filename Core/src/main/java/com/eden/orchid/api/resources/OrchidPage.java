package com.eden.orchid.api.resources;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.registration.Contextual;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

@Data
public class OrchidPage implements Contextual {

    private OrchidReference reference;

    private OrchidPage next;
    private OrchidPage previous;

    private String description;
    private String alias;

    private JSONObject data;
    private JSONArray menu;

    private OrchidResource resource;

    // injected members
    private OrchidRenderer renderer;

    public OrchidPage(OrchidResource resource) {
        getInjector().injectMembers(this);

        this.resource = resource;

        this.reference = new OrchidReference(resource.getReference());
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
    }

    /**
     * Render a template from a resource file
     *
     * @param templateName the full relative name of the template to render
     */
    public void renderTemplate(String... templateName) {
        this.data = buildPageData();

        renderer.renderTemplate(this, templateName);
    }

    public void renderString(String extension, String content) {
        this.data = buildPageData();
        renderer.renderString(this, extension, content);
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

        if (resource != null && !EdenUtils.isEmpty(resource.getContent())) {
            String compiledContent = getTheme().compile(
                    resource.getReference().getExtension(),
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
        if (page != null) {
            JSONObject pageData = new JSONObject();

            pageData.put("title", page.getReference().getTitle());
            pageData.put("description", page.getDescription());
            pageData.put("url", page.getReference().toString());

            return pageData;
        }

        return null;
    }

    @Inject
    public void setRenderer(OrchidRenderer renderer) {
        this.renderer = renderer;
    }
}
