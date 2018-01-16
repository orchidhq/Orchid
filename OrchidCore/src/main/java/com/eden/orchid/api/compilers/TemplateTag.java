package com.eden.orchid.api.compilers;

import com.eden.orchid.api.options.OptionsHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public abstract class TemplateTag implements OptionsHolder {

    @Getter
    private final String name;

    @Getter
    @Accessors(fluent = true)
    private final boolean hasContent;

    @Getter @Setter
    private String content;

    public TemplateTag(String name, boolean hasContent) {
        this.name = name;
        this.hasContent = hasContent;
    }

    public abstract String[] parameters();
}
