package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidService;

public interface ThemeService extends OrchidService {

    default void clearThemes() {
        getService(ThemeService.class).clearThemes();
    }

    default void setDefaultTheme(Theme theme) {
        getService(ThemeService.class).setDefaultTheme(theme);
    }

    default Theme getDefaultTheme() {
        return getService(ThemeService.class).getDefaultTheme();
    }

    default Theme getTheme() {
        return getService(ThemeService.class).getTheme();
    }

    default void pushTheme(Theme theme) {
        getService(ThemeService.class).pushTheme(theme);
    }

    default void popTheme() {
        getService(ThemeService.class).popTheme();
    }
}
