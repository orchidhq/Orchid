package com.eden.orchid.api.compilers;

import com.eden.orchid.api.options.OptionsHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Template Tags are a convenient way to create complex structures from simple markup that may be used anywhere. Tags
 * should be wrapped by the main template language, and are expected to provide a template in `templates/tags/` with a
 * filename that matches the `name` of the TemplateTag. This template is what is rendered when the tag is parsed by the
 * main template language, and any Options may be set with the Tag class and are available within the template. All
 * options can be set as named arguments, but if you want to allow certain parameters to be specified as sequential
 * parameters, you may pass the name of those parameters in the `parameters()` method.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class TemplateTag implements OptionsHolder {

    @Getter
    private final String name;

    @Getter @Accessors(fluent = true)
    private final boolean hasContent;

    @Getter @Setter
    private String content;

    /**
     * Initialize the Tag with the name which it should be called with in the template. The actual implementation of a
     * Tag should have a single constructor annotated with {@link javax.inject.Inject }.
     *
     * @param name the name which to call this Tag
     * @param hasContent true if this tag wraps arbitrary content, false otherwise
     */
    public TemplateTag(String name, boolean hasContent) {
        this.name = name;
        this.hasContent = hasContent;
    }

    /**
     * The sequential parameters of this function
     *
     * @return the sequential parameters
     */
    public abstract String[] parameters();

}
