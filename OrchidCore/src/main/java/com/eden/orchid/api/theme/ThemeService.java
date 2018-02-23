package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.google.inject.ImplementedBy;
import org.json.JSONObject;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@ImplementedBy(ThemeServiceImpl.class)
public interface ThemeService extends OrchidService {

    default AssetHolder getGlobalAssetHolder() {
        return getService(ThemeService.class).getGlobalAssetHolder();
    }

    default Theme getDefaultTheme() {
        return getService(ThemeService.class).getDefaultTheme();
    }

    default Theme getTheme() {
        return getService(ThemeService.class).getTheme();
    }

    default Theme findTheme(String theme) {
        return getService(ThemeService.class).findTheme(theme);
    }

    default void pushTheme(Theme theme) {
        getService(ThemeService.class).pushTheme(theme);
    }

    default void pushTheme(Theme theme, JSONObject themeOptions) {
        getService(ThemeService.class).pushTheme(theme, themeOptions);
    }

    default Theme doWithTheme(Object theme, Runnable cb) {
        return getService(ThemeService.class).doWithTheme(theme, cb);
    }

    default void popTheme() {
        getService(ThemeService.class).popTheme();
    }

    default void clearThemes() {
        getService(ThemeService.class).clearThemes();
    }

    default AdminTheme getDefaultAdminTheme() {
        return getService(ThemeService.class).getDefaultAdminTheme();
    }

    default AdminTheme getAdminTheme() {
        return getService(ThemeService.class).getAdminTheme();
    }

    default AdminTheme findAdminTheme(String theme) {
        return getService(ThemeService.class).findAdminTheme(theme);
    }

    default void pushAdminTheme(AdminTheme theme) {
        getService(ThemeService.class).pushAdminTheme(theme);
    }

    default void pushAdminTheme(AdminTheme theme, JSONObject themeOptions) {
        getService(ThemeService.class).pushAdminTheme(theme, themeOptions);
    }

    default void popAdminTheme() {
        getService(ThemeService.class).popAdminTheme();
    }

    default void clearAdminThemes() {
        getService(ThemeService.class).clearAdminThemes();
    }
}
