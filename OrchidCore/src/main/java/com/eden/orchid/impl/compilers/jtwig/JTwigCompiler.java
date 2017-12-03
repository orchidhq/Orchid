package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.compilers.OrchidCompiler;
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
import java.util.Map;
import java.util.Set;

@Singleton
public final class JTwigCompiler extends OrchidCompiler {

    private final EnvironmentConfiguration jtwigEnvironment;

    @Inject
    public JTwigCompiler(Set<JtwigFunction> functionSet, Set<TypedResourceLoader> loaderSet) {
        super(1000);
        EnvironmentConfigurationBuilder config = EnvironmentConfigurationBuilder.configuration();

        config.parser().withoutTemplateCache();

        for(JtwigFunction function : functionSet) {
            config.functions().add(function);
        }

        List<TypedResourceLoader> loaders = new ArrayList<>(config.resources().resourceLoaders().build());
        for(TypedResourceLoader loader : loaderSet) {
            loaders.add(0, loader);
        }
        config.resources().resourceLoaders().set(loaders);

        jtwigEnvironment = config.build();
    }

    @Override
    public String compile(String extension, String source, Map<String, Object> data) {
        return new JtwigTemplate(
                new EnvironmentFactory().create(jtwigEnvironment),
                new ResourceReference(ResourceReference.STRING, source)
        ).render(JtwigModel.newModel(data));
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
