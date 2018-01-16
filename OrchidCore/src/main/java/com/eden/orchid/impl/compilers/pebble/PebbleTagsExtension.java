package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateTag;
import com.google.inject.Provider;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
public final class PebbleTagsExtension extends AbstractPebbleExtension {

    private Set<TemplateTag> templateTags;

    @Inject
    public PebbleTagsExtension(Provider<OrchidContext> contextProvider, Set<TemplateTag> templateTags) {
        super(contextProvider);
        this.templateTags = templateTags;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        List<TokenParser> tokenParsers = new ArrayList<>();

        for(TemplateTag templateTag : templateTags) {
            tokenParsers.add(new PebbleWrapperTemplateTag(
                    contextProvider,
                    templateTag.getName(),
                    templateTag.parameters(),
                    templateTag.hasContent(),
                    templateTag.getClass()
            ));
        }

        return tokenParsers;
    }
}
