package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.compilers.TemplateTag;
import javax.inject.Provider;
import com.mitchellbosecke.pebble.attributes.AttributeResolver;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public final class CorePebbleExtension extends AbstractExtension {

    private Provider<OrchidContext> contextProvider;

    private Set<TemplateTag> templateTags;
    private Set<TemplateFunction> templateFunctions;
    private Set<AttributeResolver> attributeResolvers;

    @Inject
    public CorePebbleExtension(
            Provider<OrchidContext> contextProvider,
            Set<TemplateTag> templateTags,
            Set<TemplateFunction> templateFunctions,
            Set<AttributeResolver> attributeResolvers) {
        this.contextProvider = contextProvider;
        this.templateTags = templateTags;
        this.templateFunctions = templateFunctions;
        this.attributeResolvers = attributeResolvers;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        List<TokenParser> tokenParsers = new ArrayList<>();

        for(TemplateTag templateTag : templateTags) {
            final String[] tabParams;
            final Class<? extends TemplateTag.Tab> tabClass;
            if(templateTag.getType() == TemplateTag.Type.Tabbed) {
                TemplateTag.Tab tab = templateTag.getNewTab("", "");
                tabParams = tab.parameters();
                tabClass = tab.getClass();
            }
            else {
                tabParams = null;
                tabClass = null;
            }

            tokenParsers.add(new PebbleWrapperTemplateTag(
                    contextProvider,
                    templateTag.getName(),
                    templateTag.getType(),
                    templateTag.parameters(),
                    templateTag.getClass(),
                    tabParams,
                    tabClass
            ));
        }

        return tokenParsers;
    }

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filterMap = new HashMap<>();

        for (TemplateFunction function : templateFunctions) {
            filterMap.put(function.getName(), new PebbleWrapperTemplateFilter(
                    contextProvider,
                    function.getName(),
                    Arrays.asList(function.parameters()),
                    function.getClass()));
        }

        return filterMap;
    }

    @Override
    public Map<String, Function> getFunctions() {
        Map<String, Function> functionMap = new HashMap<>();

        for (TemplateFunction function : templateFunctions) {
            functionMap.put(function.getName(), new PebbleWrapperTemplateFunction(
                    contextProvider,
                    function.getName(),
                    Arrays.asList(function.parameters()),
                    function.getClass()));
        }

        return functionMap;
    }

    @Override
    public List<AttributeResolver> getAttributeResolver() {
        return new ArrayList<>(attributeResolvers);
    }
}
