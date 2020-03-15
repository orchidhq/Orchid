package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.assets.AssetManager;
import com.eden.orchid.api.theme.components.ModularPageList;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.relations.ThemeRelation;

import javax.annotation.Nullable;
import javax.inject.Provider;

import com.eden.orchid.utilities.StreamUtilsKt;
import com.google.inject.name.Named;
import kotlin.text.StringsKt;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

@Description(value = "How Orchid manages your site's themes.", name = "Themes")
@Archetype(value = ConfigArchetype.class, key = "services.themes")
public final class ThemeServiceImpl implements ThemeService, OrchidEventListener {

    private OrchidContext context;
    private final AssetManager assetManager;
    private final String defaultTheme;

    private Provider<Set<Theme>> themesProvider;

    @Inject
    public ThemeServiceImpl(
            AssetManager assetManager,
            Provider<Set<Theme>> themesProvider,
            @Named("theme") String defaultTheme) {
        this.assetManager = assetManager;
        this.defaultTheme = defaultTheme;
        this.themesProvider = themesProvider;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public AssetManager getAssetManager() {
        return assetManager;
    }

// Interface Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Theme findTheme(String themeKey, @Nullable Map<String, Object> params) {
        Set<Theme> availableThemes = themesProvider.get();

        Optional<Theme> requestedTheme = availableThemes
                .stream()
                .sorted()
                .filter(theme -> theme.getKey().equals(themeKey))
                .findFirst();

        final Optional<Theme> themeOptional;
        if (requestedTheme.isPresent()) {
            themeOptional = requestedTheme;
        } else {
            themeOptional = availableThemes
                    .stream()
                    .sorted()
                    .filter(theme -> theme.getKey().equals(defaultTheme))
                    .findFirst();
        }

        return themeOptional
                .map(it -> (Theme) context.resolve(it.getClass()))
                .map(it -> {
                    it.extractOptions(context, params);
                    return it;
                })
                .orElseGet(() -> {
                    Clog.e("Could not find theme [{}]", themeKey);
                    return null;
                });
    }

    @On(Orchid.Lifecycle.ClearCache.class)
    public void onClearCache(Orchid.Lifecycle.ClearCache event) {
        assetManager.clearAssets();
    }

    public static Theme lookupTheme(Object source) {
        if(source instanceof Theme) {
            return (Theme) source;
        }
        else if(source instanceof OrchidPage) {
            return ((OrchidPage) source).getTheme();
        }
        else if(source instanceof OrchidComponent) {
            return ((OrchidComponent) source).getPage().getTheme();
        }
        else {
            return null;
        }
    }
}
