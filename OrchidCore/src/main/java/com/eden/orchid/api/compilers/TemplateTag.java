package com.eden.orchid.api.compilers;

import com.eden.orchid.api.options.OptionsHolder;
import lombok.Getter;

public abstract class TemplateTag implements OptionsHolder {

    @Getter
    private final String name;

    @Getter
    private final String defaultParameter;

    public TemplateTag(String name, String defaultParameter) {
        this.name = name;
        this.defaultParameter = defaultParameter;
    }

}
