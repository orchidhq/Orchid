package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.theme.assets.AssetManager;
import com.eden.orchid.impl.relations.ThemeRelation;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import kotlin.text.StringsKt;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@Description(value = "How Orchid manages your site's themes.", name = "Themes")
public final class ThemeServiceImpl implements ThemeService, OrchidEventListener {

    private OrchidContext context;
    private final AssetManager assetManager;

    private ThemeHolder<Theme> themes;
    private ThemeHolder<AdminTheme> adminThemes;

    private final String defaultTheme;
    private final String defaultAdminTheme;

    private Provider<Set<Theme>> themesProvider;
    private Provider<Set<AdminTheme>> adminThemesProvider;

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

        Theme emptyTheme = new Theme(context, "Default", 1) { };
        AdminTheme emptyAdminTheme = new AdminTheme(context, "Default", 1) { };

        themes = new ThemeHolder<>(context, defaultTheme, "theme", themesProvider.get(), emptyTheme);
        adminThemes = new ThemeHolder<>(context, defaultAdminTheme, "adminTheme", adminThemesProvider.get(), emptyAdminTheme);
    }

    @Override
    public void onStart() {
        themes.clearThemes();
        adminThemes.clearThemes();
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
    @Override public void  clearThemes()           { themes.clearThemes(); }

    @Override public AdminTheme getAdminTheme()                  { return adminThemes.getTheme(); }
    @Override public AdminTheme findAdminTheme(String theme)     { return adminThemes.findTheme(theme); }
    @Override public void       pushAdminTheme(AdminTheme theme) { adminThemes.pushTheme(theme); }
    @Override public void       popAdminTheme()                  { adminThemes.popTheme(); }
    @Override public void       clearAdminThemes()               { adminThemes.clearThemes(); }

    @Override public Theme doWithTheme(ThemeRelation themeObject, Runnable cb) {
        Theme theme = themeObject.get();

        if (theme != null) {
            pushTheme(theme);
            cb.run();
            theme.renderAssets();
            popTheme();
            return theme;
        }
        else {
            cb.run();
            return null;
        }
    }

// Delegate interface calls to inner class
//----------------------------------------------------------------------------------------------------------------------

    private static class ThemeHolder<T extends AbstractTheme> {

        private OrchidContext context;

        private String defaultThemeKey;
        private String defaultOptionsKey;
        private T defaultTheme;
        private Stack<T> themeStack;
        private Set<T> availableThemes;

        ThemeHolder(OrchidContext context, String defaultTheme, String defaultOptionsKey, Set<T> availableThemes, T emptyTheme) {
            this.context = context;
            this.defaultThemeKey = defaultTheme;
            this.defaultOptionsKey = defaultOptionsKey;
            this.availableThemes = availableThemes;
            this.defaultTheme = findTheme(this.defaultThemeKey);
            this.themeStack = new Stack<>();
            if(this.defaultTheme == null) {
                this.defaultTheme = emptyTheme;
            }
        }

        T getTheme() {
            return (themeStack.size() > 0) ? themeStack.peek() : defaultTheme;
        }

        T findTheme(String themeKey) {
            if(EdenUtils.isEmpty(themeKey)) {
                Clog.e("{} key cannot be empty [{}-{}]", StringsKt.capitalize(defaultOptionsKey));
                return null;
            }

            if(availableThemes.size() == 0) {
                Clog.e("No {} registered", defaultOptionsKey);
                return null;
            }

            return availableThemes
                    .stream()
                    .sorted()
                    .filter(theme -> theme.getKey().equals(themeKey))
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

        void clearThemes() {
            themeStack.clear();
            defaultTheme.extractOptions(context, new HashMap<>());
            pushTheme(defaultTheme);
        }
    }

    @On(Orchid.Lifecycle.ClearCache.class)
    public void onClearCache(Orchid.Lifecycle.ClearCache event) {
        assetManager.clearAssets();
    }
}
