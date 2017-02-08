package com.eden.orchid.impl.compilers.jtwig;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.registration.AutoRegister;
import org.json.JSONObject;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.environment.EnvironmentFactory;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.resource.loader.TypedResourceLoader;
import org.jtwig.resource.reference.ResourceReference;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class JTwigCompiler implements OrchidCompiler {

    private EnvironmentConfigurationBuilder config = EnvironmentConfigurationBuilder.configuration();
    private boolean hasRegisteredComponents = false;

    @Override
    public String compile(String extension, String source, Object... data) {
        if(!hasRegisteredComponents) {
            for(JtwigFunction function : getRegistrar().resolveSet(JtwigFunction.class)) {
                config.functions().add(function);
            }

            List<TypedResourceLoader> loaders = new ArrayList<>(config.resources().resourceLoaders().build());
            for(TypedResourceLoader loader : getRegistrar().resolveSet(TypedResourceLoader.class)) {
                loaders.add(0, loader);
            }
            config.resources().resourceLoaders().set(loaders);

            hasRegisteredComponents = true;
        }

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
                new EnvironmentFactory().create(config.build()),
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

    @Override
    public int priority() {
        return 1000;
    }

}
