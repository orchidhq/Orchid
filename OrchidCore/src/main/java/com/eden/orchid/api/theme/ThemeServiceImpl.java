package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.GlobalAssetHolder;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Set;
import java.util.Stack;

public final class ThemeServiceImpl implements ThemeService {

    private OrchidContext context;
    private AssetHolder assetHolder;

    private ThemeHolder<Theme> themes;
    private ThemeHolder<AdminTheme> adminThemes;

    private final String defaultTheme;
    private final String defaultAdminTheme;

    private Provider<Set<Theme>> themesProvider;
    private Provider<Set<AdminTheme>> adminThemesProvider;

    @Inject
    public ThemeServiceImpl(GlobalAssetHolder assetHolder, Provider<Set<Theme>> themesProvider, @Named("theme") String defaultTheme, Provider<Set<AdminTheme>> adminThemesProvider, @Named("adminTheme") String defaultAdminTheme) {
        this.assetHolder = assetHolder;
        this.defaultTheme = defaultTheme;
        this.themesProvider = themesProvider;

        this.defaultAdminTheme = defaultAdminTheme;
        this.adminThemesProvider = adminThemesProvider;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;

        themes = new ThemeHolder<>(context, defaultTheme, "theme", themesProvider.get());
        adminThemes = new ThemeHolder<>(context, defaultAdminTheme, "adminTheme", adminThemesProvider.get());
    }

    @Override
    public AssetHolder getGlobalAssetHolder() {
        return assetHolder;
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

        public ThemeHolder(OrchidContext context, String defaultTheme, String defaultOptionsKey, Set<T> availableThemes) {
            this.context = context;
            this.defaultThemeKey = defaultTheme;
            this.defaultOptionsKey = defaultOptionsKey;
            this.availableThemes = availableThemes;
            this.defaultTheme = findTheme(this.defaultThemeKey);
            this.themeStack = new Stack<>();
        }

        public T getTheme() {
            return (themeStack.size() > 0) ? themeStack.peek() : defaultTheme;
        }

        public T getDefaultTheme() {
            return defaultTheme;
        }

        public T findTheme(String themeKey) {
            T foundTheme = availableThemes
                    .stream()
                    .sorted()
                    .filter(theme -> theme.getKey().equals(themeKey))
                    .findFirst()
                    .orElse(null);

            if (foundTheme != null) {
                return foundTheme;
            }
            else {
                Clog.e("Could not find theme [{}]", themeKey);
                return null;
            }
        }

        public void pushTheme(T theme) {
            JSONObject themeOptions;

            if(OrchidUtils.elementIsObject(context.query(theme.getKey()))) {
                themeOptions = (JSONObject) context.query(theme.getKey()).getElement();
            }
            else if(OrchidUtils.elementIsObject(context.query(defaultOptionsKey))) {
                themeOptions = (JSONObject) context.query(defaultOptionsKey).getElement();
            }
            else {
                themeOptions = context.getOptionsData();
            }

            pushTheme(theme, themeOptions);
        }

        public void pushTheme(T theme, JSONObject themeOptions) {
            theme.clearCache();
            theme.initialize();

            theme.extractOptions(context, themeOptions);

            theme.addAssets();
            themeStack.push(theme);
        }

        public void popTheme() {
            themeStack.pop();
        }

        public void clearThemes() {
            themeStack.clear();
        }
    }
}
