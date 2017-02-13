package com.eden.orchid.impl.compilers.jtwig;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.compilers.OrchidCompiler;
import org.json.JSONObject;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.environment.EnvironmentFactory;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.resource.loader.TypedResourceLoader;
import org.jtwig.resource.reference.ResourceReference;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
public class JTwigCompiler extends OrchidCompiler {
    private EnvironmentConfiguration jtwigEnvironment;

    @Inject
    public JTwigCompiler(Set<JtwigFunction> functionSet, Set<TypedResourceLoader> loaderSet) {
        EnvironmentConfigurationBuilder config = EnvironmentConfigurationBuilder.configuration();

        for(JtwigFunction function : functionSet) {
            config.functions().add(function);
        }

        List<TypedResourceLoader> loaders = new ArrayList<>(config.resources().resourceLoaders().build());
        for(TypedResourceLoader loader : loaderSet) {
            loaders.add(0, loader);
        }
        config.resources().resourceLoaders().set(loaders);

        jtwigEnvironment = config.build();

        this.priority = 1000;
    }


    @Override
    public String compile(String extension, String source, Object... data) {

        String s = "";
        if(data != null && data.length > 0 && data[0] != null) {
            s = data[0].toString();
        }

        JtwigModel model = null;

        if(!EdenUtils.isEmpty(s)) {
            model = JtwigModel.newModel(new JSONObject(s).toMap());
        }
        else {
            model = JtwigModel.newModel();
        }

        return new JtwigTemplate(
                new EnvironmentFactory().create(jtwigEnvironment),
                new ResourceReference(ResourceReference.STRING, source)
        ).render(model);
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"html", "twig"};
    }

}
