package com.eden.orchid.impl.themes.templateTags;

import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class AlertTag extends TemplateTag {

    @Option @StringDefault("info")
    @Getter @Setter
    private String level;

    @Option @StringDefault("")
    @Getter @Setter
    private String headline;

    @Inject
    public AlertTag() {
        super("alert", true);
    }

    @Override
    public String[] parameters() {
        return new String[] {"level", "headline"};
    }
}
