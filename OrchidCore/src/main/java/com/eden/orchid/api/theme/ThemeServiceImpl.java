package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.assets.GlobalAssetHolder;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Set;
import java.util.Stack;

/**
 * @since v1.0.0
 * @orchidApi services
 */
public final class ThemeServiceImpl implements ThemeService {

    private OrchidContext context;
    private final GlobalAssetHolder globalAssetHolder;

    private ThemeHolder<Theme> themes;
    private ThemeHolder<AdminTheme> adminThemes;

    private final String defaultTheme;
    private final String defaultAdminTheme;

    private Provider<Set<Theme>> themesProvider;
    private Provider<Set<AdminTheme>> adminThemesProvider;

    @Inject
    public ThemeServiceImpl(
            GlobalAssetHolder globalAssetHolder,
            Provider<Set<Theme>> themesProvider,
            @Named("theme") String defaultTheme,
            Provider<Set<AdminTheme>> adminThemesProvider,
            @Named("adminTheme") String defaultAdminTheme) {
        this.globalAssetHolder = globalAssetHolder;
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
        themes.pushTheme(themes.getDefaultTheme());
        adminThemes.pushTheme(adminThemes.getDefaultTheme());
    }

    @Override
    public GlobalAssetHolder getGlobalAssetHolder() {
        return globalAssetHolder;
    }

// Interface Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override public Theme getTheme()                                      { return themes.getTheme(); }
    @Override public Theme getDefaultTheme()                               { return themes.getDefaultTheme(); }
    @Override public Theme findTheme(String theme)                         { return themes.findTheme(theme); }
    @Override public void  pushTheme(Theme theme)                          { themes.pushTheme(theme); }
    @Override public void  pushTheme(Theme theme, JSONObject themeOptions) { themes.pushTheme(theme, themeOptions); }
    @Override public void  popTheme()                                      { themes.popTheme(); }
    @Override public void  clearThemes()                                   { themes.clearThemes(); }
    @Override public Theme doWithTheme(Object theme, Runnable cb)          { return themes.doWithTheme(theme, cb); }

    @Override public AdminTheme getAdminTheme()                                           { return adminThemes.getTheme(); }
    @Override public AdminTheme getDefaultAdminTheme()                                    { return adminThemes.getDefaultTheme(); }
    @Override public AdminTheme findAdminTheme(String theme)                              { return adminThemes.findTheme(theme); }
    @Override public void       pushAdminTheme(AdminTheme theme)                          { adminThemes.pushTheme(theme); }
    @Override public void       pushAdminTheme(AdminTheme theme, JSONObject themeOptions) { adminThemes.pushTheme(theme, themeOptions); }
    @Override public void       popAdminTheme()                                           { adminThemes.popTheme(); }
    @Override public void       clearAdminThemes()                                        { adminThemes.clearThemes(); }

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

        T getDefaultTheme() {
            return defaultTheme;
        }

        T findTheme(String themeKey) {
            if(availableThemes.size() > 0) {
                T foundTheme = availableThemes
                        .stream()
                        .sorted()
                        .filter(theme -> theme.getKey().equals(themeKey))
                        .findFirst()
                        .orElse(null);

                if (foundTheme != null) {
                    return (T) context.getInjector().getInstance(foundTheme.getClass());
                }
                else {
                    Clog.e("Could not find theme [{}-{}]", defaultOptionsKey, themeKey);
                    return null;
                }
            }
            else {
                return null;
            }
        }

        void pushTheme(T theme) {
            JSONObject themeOptions = new JSONObject();

            if(EdenUtils.elementIsObject(context.query(defaultOptionsKey))) {
                themeOptions = EdenUtils.merge(themeOptions,  (JSONObject) context.query(defaultOptionsKey).getElement());
            }
            if(EdenUtils.elementIsObject(context.query(theme.getKey()))) {
                themeOptions = EdenUtils.merge(themeOptions,  (JSONObject) context.query(theme.getKey()).getElement());
            }

            pushTheme(theme, themeOptions);
        }

        void pushTheme(T theme, JSONObject themeOptions) {
            theme.clearCache();
            theme.initialize();

            theme.extractOptions(context, themeOptions);

            themeStack.push(theme);
        }

        void popTheme() {
            themeStack.pop();
        }

        void clearThemes() {
            themeStack.clear();
            pushTheme(this.defaultTheme);
        }

        T doWithTheme(Object themeObject, Runnable cb) {
            T theme = null;
            JSONObject themeOptions = null;

            if(themeObject != null) {
                if(themeObject instanceof String) {
                    themeOptions = null;
                    theme = findTheme((String) themeObject);
                }
                else if(themeObject instanceof JSONObject) {
                    themeOptions = (JSONObject) themeObject;
                    theme = findTheme(((JSONObject) themeObject).getString("key"));
                }
            }

            if (theme != null) {
                if(themeOptions != null) {
                    pushTheme(theme, themeOptions);
                }
                else {
                    pushTheme(theme);
                }
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
    }
}
