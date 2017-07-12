package com.eden.orchid.api.theme.pages;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.google.inject.Injector;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Data
public class OrchidPage {

    protected OrchidContext context;
    protected OrchidResource resource;
    protected OrchidReference reference;

    protected OrchidPage next;
    protected OrchidPage previous;

    protected String description;
    protected String type;

    protected JSONObject data;
    protected JSONArray menu;

    protected Map<String, Class<? extends OrchidComponent>> componentClasses;
    protected Map<String, OrchidComponent> components;

    protected String layout;

    // injected members
    protected OrchidRenderer renderer;

    public OrchidPage(OrchidResource resource) {
        this.context = resource.getContext();
        this.context.getInjector().injectMembers(this);

        this.resource = resource;

        this.reference = new OrchidReference(this.context, resource.getReference());
        this.reference.setExtension(resource.getReference().getOutputExtension());

        this.componentClasses = new HashMap<>();
        this.components = new HashMap<>();

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
        renderer.renderTemplate(this, templateName);
    }

    /**
     * Render a template given by a raw string
     *
     * @param extension the extension describing the format of templateString
     * @param templateString the template to render
     */
    public void renderString(String extension, String templateString) {
        renderer.renderString(this, extension, templateString);
    }

    /**
     * Render the contents of your resource directly
     */
    public void renderRaw() {
        renderer.renderRaw(this);
    }

    public String getContent() {
        if (resource != null && !EdenUtils.isEmpty(resource.getContent())) {
            String compiledContent = resource.getContent();

            if(resource.shouldPrecompile()) {
                compiledContent = context.precompile(compiledContent, data);
            }

            return context.compile(
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

    public String getTitle() {
        return reference.getTitle();
    }

    public String getLink() {
        return reference.toString();
    }

    public String getLayout() {
        if(!EdenUtils.isEmpty(layout)) {
            return Clog.format("[#{$1}.twig, layouts/#{$1}.twig, layouts/index.twig]", new Object[]{layout});
        }
        else {
            return "layouts/index.twig";
        }
    }

// Manage page components
//----------------------------------------------------------------------------------------------------------------------

    public void addComponent(Class<? extends OrchidComponent> component) {
        this.addComponent(component.getName(), component);
    }

    public void addComponent(String alias, Class<? extends OrchidComponent> component) {
        this.componentClasses.put(alias, component);
    }

    public void prepareComponents() {
        Injector injector = context.getInjector();
        for (Map.Entry<String, Class<? extends OrchidComponent>> componentClass : componentClasses.entrySet()) {

            OrchidComponent component = injector.getInstance(componentClass.getValue());
            if(component != null) {
                component.setPage(this);
                component.prepare();
                if(!componentClass.getKey().equals(componentClass.getValue().getName())) {
                    this.components.put(componentClass.getKey(), component);
                }
                else if(!EdenUtils.isEmpty(component.getAlias())) {
                    this.components.put(component.getAlias(), component);
                }
                else if(!EdenUtils.isEmpty(component.getDefaultAlias())) {
                    this.components.put(component.getDefaultAlias(), component);
                }
            }
        }
    }

    public OrchidComponent getComponent(String alias) {
        return this.components.getOrDefault(alias, null);
    }

// Convert a page to/from JSON
//----------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        JSONObject pageJson = new JSONObject();
        pageJson.put("reference", this.reference.toJSON());
        if(this.previous != null) {
            pageJson.put("previous", this.previous.getReference().toJSON());
        }
        if(this.next != null) {
            pageJson.put("next", this.next.getReference().toJSON());
        }

        pageJson.put("description", this.description);
        pageJson.put("type", this.type);

        JSONObject pageData = serializeData();
        if(pageData != null) {
            pageJson.put("data", pageData);
        }

        return pageJson;
    }

    protected JSONObject serializeData() {
        return this.data;
    }

    public static OrchidPage fromJSON(OrchidContext context, JSONObject source) {
        OrchidReference pageReference = OrchidReference.fromJSON(context, source.getJSONObject("reference"));
        OrchidExternalPage externalPage = new OrchidExternalPage(pageReference);

        if(source.has("previous")) {
            externalPage.setPrevious(new OrchidExternalPage(OrchidReference.fromJSON(context, source.getJSONObject("previous"))));
        }
        if(source.has("next")) {
            externalPage.setNext(new OrchidExternalPage(OrchidReference.fromJSON(context, source.getJSONObject("next"))));
        }

        externalPage.description = source.optString("description");
        externalPage.type = source.optString("type");

        if(source.has("data")) {
            externalPage.data = source.getJSONObject("className");
        }

        return externalPage;
    }

    @Override
    public String toString() {
        return this.toJSON().toString(2);
    }
}
