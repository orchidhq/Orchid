package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.theme.assets.AssetManager;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.relations.ThemeRelation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import com.google.inject.name.Named;
import kotlin.text.StringsKt;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

@Description(value = "How Orchid manages your site's themes.", name = "Themes")
@Archetype(value = ConfigArchetype.class, key = "services.themes")
@Archetype(value = ConfigArchetype.class, key = "site")
public final class ThemeServiceImpl implements ThemeService, OrchidEventListener {

    private OrchidContext context;
    private final AssetManager assetManager;

    private ThemeHolder<Theme> themes;
    private ThemeHolder<AdminTheme> adminThemes;

    private final String defaultTheme;
    private final String defaultAdminTheme;

    private Provider<Set<Theme>> themesProvider;
    private Provider<Set<AdminTheme>> adminThemesProvider;

    @Option
    private String theme;

    @Option
    private String adminTheme;

    @Inject
    public ThemeServiceImpl(
            AssetManager assetManager,
            Provider<Set<Theme>> themesProvider,
            @Named("theme") String defaultTheme,
            Provider<Set<AdminTheme>> adminThemesProvider,
            @Named("adminTheme") String defaultAdminTheme) {
        this.assetManager = assetManager;
        this.defaultTheme = defaultTheme;
        this.themesProvider = themesProvider;

        this.defaultAdminTheme = defaultAdminTheme;
        this.adminThemesProvider = adminThemesProvider;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
        themes = new ThemeHolder<>(context, "theme", themesProvider.get());
        adminThemes = new ThemeHolder<>(context, "adminTheme", adminThemesProvider.get());
    }

    @Override
    public AssetManager getAssetManager() {
        return assetManager;
    }

// Interface Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override public Theme getTheme()              { return themes.getTheme(); }
    @Override public Theme findTheme(String theme) { return themes.findTheme(theme); }
    @Override public void  pushTheme(Theme theme)  { themes.pushTheme(theme); }
    @Override public void  popTheme()              { themes.popTheme(); }
    @Override public void  clearThemes()           {
        Theme emptyTheme = new Theme(context, "Default", 1) { };
        if(!EdenUtils.isEmpty(theme)) {
            themes.initializeThemes(theme, emptyTheme);
        }
        else {
            themes.initializeThemes(defaultTheme, emptyTheme);
        }
    }

    @Override public AdminTheme getAdminTheme()                  { return adminThemes.getTheme(); }
    @Override public AdminTheme findAdminTheme(String theme)     { return adminThemes.findTheme(theme); }
    @Override public void       pushAdminTheme(AdminTheme theme) { adminThemes.pushTheme(theme); }
    @Override public void       popAdminTheme()                  { adminThemes.popTheme(); }
    @Override public void       clearAdminThemes()               {
        AdminTheme emptyAdminTheme = new AdminTheme(context, "Default", 1) { };
        if(!EdenUtils.isEmpty(theme)) {
            adminThemes.initializeThemes(adminTheme, emptyAdminTheme);
        }
        else {
            adminThemes.initializeThemes(defaultAdminTheme, emptyAdminTheme);
        }
    }

    @Override public void renderPageWithTheme(@Nonnull OrchidPage page, ThemeRelation themeObjectFromGenerator, Consumer<OrchidPage> cb) {
        renderPageWithTheme(page, themeObjectFromGenerator, null, cb);
    }

    @Override public void renderPageWithTheme(@Nonnull OrchidPage page, @Nullable ThemeRelation themeObjectFromGenerator, @Nullable ThemeRelation themeObjectFromPage, Consumer<OrchidPage> cb) {
        if(page == null) throw new NullPointerException("Page cannot be null");

        if(themeObjectFromPage != null) {
            Theme pageTheme = themeObjectFromPage.get();

            if (pageTheme != null) {
                pushTheme(pageTheme);
                pageTheme.doWithCurrentPage(page, t -> {
                    cb.accept(page);
                });
                popTheme();
                return;
            }
        }
        if(themeObjectFromGenerator != null) {
            Theme generatorTheme = themeObjectFromGenerator.get();

            if (generatorTheme != null) {
                pushTheme(generatorTheme);
                generatorTheme.doWithCurrentPage(page, t -> {
                    cb.accept(page);
                });
                popTheme();
                return;
            }
        }

        cb.accept(page);
    }

// Delegate interface calls to inner class
//----------------------------------------------------------------------------------------------------------------------

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setAdminTheme(String adminTheme) {
        this.adminTheme = adminTheme;
    }

    private static class ThemeHolder<T extends AbstractTheme> {

        private OrchidContext context;

        private String defaultThemeKey;
        private String defaultOptionsKey;
        private T defaultTheme;
        private Stack<T> themeStack;
        private Set<T> availableThemes;

        ThemeHolder(OrchidContext context, String defaultOptionsKey, Set<T> availableThemes) {
            this.context = context;
            this.defaultOptionsKey = defaultOptionsKey;
            this.availableThemes = availableThemes;
            this.themeStack = new Stack<>();
        }

        void initializeThemes(String defaultTheme, T emptyTheme) {
            themeStack.clear();
            this.defaultThemeKey = defaultTheme;
            this.defaultTheme = findTheme(this.defaultThemeKey);
            if(this.defaultTheme == null) {
                this.defaultTheme = emptyTheme;
            }
            this.defaultTheme.extractOptions(context, new HashMap<>());
        }

        T getTheme() {
            return (themeStack.size() > 0)
                    ? themeStack.peek()
                    : defaultTheme;
        }

        T findTheme(String themeKey) {
            if(EdenUtils.isEmpty(themeKey)) {
                Clog.e("{} key cannot be empty [{}-{}]", StringsKt.capitalize(defaultOptionsKey));
                return null;
            }

            if(availableThemes.size() == 0) {
                return null;
            }

            return availableThemes
                    .stream()
                    .sorted()
                    .filter(theme ->
                            theme.getKey()
                                    .equals(themeKey)
                    )
                    .findFirst()
                    .map(it -> (T) context.resolve(it.getClass()))
                    .orElseGet(() -> {
                        Clog.e("Could not find {} [{}]", defaultOptionsKey, themeKey);
                        return null;
                    });
        }

        void pushTheme(T theme) {
            theme.clearCache();
            theme.initialize();

            themeStack.push(theme);
        }

        void popTheme() {
            themeStack.pop();
        }
    }

    @On(Orchid.Lifecycle.ClearCache.class)
    public void onClearCache(Orchid.Lifecycle.ClearCache event) {
        assetManager.clearAssets();
    }
}
