package com.eden.orchid.compilers.impl;

import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.utilities.AutoRegister;
import org.json.JSONObject;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.environment.EnvironmentFactory;
import org.jtwig.resource.reference.ResourceReference;

@AutoRegister
public class JTwigCompiler implements Compiler {

    public static EnvironmentConfigurationBuilder config = EnvironmentConfigurationBuilder.configuration();

    static {
//        config.parser().withoutTemplateCache();
//        config.render().withStrictMode(false);
    }

    @Override
    public String compile(String extension, String source, Object... data) {
        String s = "";
        if(data != null && data.length > 0 && data[0] != null) {
            s = data[0].toString();
        }

        return new JtwigTemplate(
                new EnvironmentFactory().create(config.build()),
                new ResourceReference(ResourceReference.STRING, source)
        )
                .render(JtwigModel.newModel(new JSONObject(s).toMap()));
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"html", "twig"};
    }

    @Override
    public int priority() {
        return 90;
    }
}
