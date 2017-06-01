package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.OrchidContext;
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
public class JTwigCompiler extends OrchidCompiler {

    private OrchidContext context;
    private EnvironmentConfiguration jtwigEnvironment;

    @Inject
    public JTwigCompiler(OrchidContext context, Set<JtwigFunction> functionSet, Set<TypedResourceLoader> loaderSet) {
        this.context = context;
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

        this.priority = 1000;
    }

    @Override
    public String compile(String extension, String source, Object... data) {
        Map<String, Object> siteData = context.getSiteData(data);
        return new JtwigTemplate(
                new EnvironmentFactory().create(jtwigEnvironment),
                new ResourceReference(ResourceReference.STRING, source)
        ).render(JtwigModel.newModel(siteData));
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
