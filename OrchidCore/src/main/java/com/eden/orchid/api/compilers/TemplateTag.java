package com.eden.orchid.api.compilers;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.render.Renderable;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Template Tags are a convenient way to create complex structures from simple markup that may be used anywhere. Tags
 * should be wrapped by the main template language, and are expected to provide a template in `templates/tags/` with a
 * filename that matches the `name` of the TemplateTag. This template is what is rendered when the tag is parsed by the
 * main template language, and any Options may be set with the Tag class and are available within the template. All
 * options can be set as named arguments, but if you want to allow certain parameters to be specified as sequential
 * parameters, you may pass the name of those parameters in the `parameters()` method.
 *
 * @orchidApi extensible
 * @since v1.0.0
 */
@Extensible
@Description(value = "A shortcode-like tag for named blocks of template content or DSLs.", name = "Template Tags")
public abstract class TemplateTag implements OptionsHolder, Renderable {

    public enum Type {
        Simple, Content, Tabbed
    }

    public interface Tab extends OptionsHolder {
        String getKey();

        String getContent();

        String[] parameters();
    }

    public static final class SimpleTab implements Tab {
        private final String key;
        private final String content;

        public SimpleTab(String key, String content) {
            this.key = key;
            this.content = content;
        }

        @Override
        public String[] parameters() {
            return new String[0];
        }

        public String getKey() {
            return this.key;
        }

        public String getContent() {
            return this.content;
        }
    }

    protected final String templateBase = "tags";
    private final String name;
    private final boolean rendersContent;
    private final LinkedHashMap<String, Tab> content;
    private final Type type;
    @Option
    @Description("Specify a template or a list of templates to use when rendering this component. The first template that exists will be chosen for this component.")
    protected String template;

    protected OrchidContext context;
    protected OrchidPage page;

    /**
     * Initialize the Tag with the name which it should be called with in the template. The actual implementation of a
     * Tag should have a single constructor annotated with {@link javax.inject.Inject }.
     *
     * @param name           the name which to call this Tag
     * @param type           the type of tag this is. A Simple tag is just a simple self-closing tag, a Content tag
     *                       wraps a single content body, a Tabbed tag wraps multiple content bodies and references them
     *                       by name
     * @param rendersContent true if this tag renders content, false if it just produces side-effects with no output
     */
    public TemplateTag(String name, Type type, boolean rendersContent) {
        this.name = name;
        this.type = type;
        this.rendersContent = rendersContent;
        this.content = new LinkedHashMap<>();
    }

    /**
     * The sequential parameters of this function
     *
     * @return the sequential parameters
     */
    public abstract String[] parameters();

    public void onRender(OrchidContext context, OrchidPage page) {
        this.context = context;
        this.page = page;
    }

    public String getContent() {
        return getContent(null);
    }

    public String getContent(String name) {
        switch (type) {
        case Simple: 
            return "";

        case Content: 
            return content.get(null).getContent();

        case Tabbed: 
            return (content.get(name) != null) ? content.get(name).getContent() : "";

        default: 
            return "";
        }
    }

    public void setContent(Tab content) {
        setContent(null, content);
    }

    public void setContent(String name, Tab content) {
        switch (type) {
        case Simple: 
            break;

        case Content: 
            this.content.put(null, content);
            break;

        case Tabbed: 
            this.content.put(name, content);
            break;

        default: 
            break;
        }
    }

    public Tab getNewTab(final String key, final String content) {
        return new SimpleTab(key, content);
    }

    public List<Tab> getTabs() {
        switch (type) {
        case Simple: 
            return new ArrayList<>();

        case Content: 
            return new ArrayList<>();

        case Tabbed: 
            return new ArrayList<>(this.content.values());

        default: 
            return new ArrayList<>();
        }
    }

    public List<String> getTemplates() {
        return null;
    }

    public final List<String> getPossibleTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add(this.template);
        templates.add(getName());
        return templates;
    }

    public String getTemplateBase() {
        return this.templateBase;
    }

    public String getName() {
        return this.name;
    }

    public boolean rendersContent() {
        return this.rendersContent;
    }

    public Type getType() {
        return this.type;
    }

    public OrchidPage getPage() {
        return this.page;
    }

    public String getTemplate() {
        return this.template;
    }

    public OrchidContext getContext() {
        return context;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }
}
