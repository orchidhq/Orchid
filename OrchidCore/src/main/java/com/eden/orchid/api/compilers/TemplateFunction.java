package com.eden.orchid.api.compilers;

import com.eden.orchid.api.options.OptionsHolder;
import lombok.Getter;

public abstract class TemplateFunction implements OptionsHolder {

    @Getter
    private final String name;

    public TemplateFunction(String name) {
        this.name = name;
    }

    public boolean isSafe() {
        return false;
    }

    public abstract String[] parameters();

    public abstract Object apply(Object input);

}
