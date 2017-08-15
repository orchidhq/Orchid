package com.eden.orchid.api.theme;

public interface ThemeService {

    ThemeService getThemeService();

    default void clearThemes() {
        getThemeService().clearThemes();
    }

    default void setDefaultTheme(Theme theme) {
        getThemeService().setDefaultTheme(theme);
    }

    default Theme getDefaultTheme() {
        return getThemeService().getDefaultTheme();
    }

    default Theme getTheme() {
        return getThemeService().getTheme();
    }

    default void pushTheme(Theme theme) {
        getThemeService().pushTheme(theme);
    }

    default void popTheme() {
        getThemeService().popTheme();
    }
}
