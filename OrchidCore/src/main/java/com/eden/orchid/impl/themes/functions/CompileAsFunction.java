package com.eden.orchid.impl.themes.functions;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class CompileAsFunction extends TemplateFunction {

    private final OrchidContext context;

    @Getter @Setter
    @Option
    @Description("The content to compile.")
    private String input;

    @Getter @Setter
    @Option @StringDefault("txt")
    @Description("The extension to compile the inner content against.")
    private String ext;

    @Inject
    public CompileAsFunction(OrchidContext context) {
        super("compileAs", true);
        this.context = context;
    }

    @Override
    public String[] parameters() {
        return new String[] {"ext"};
    }

    @Override
    public Object apply(Object input) {
        return context.compile(ext, input.toString());
    }

}