package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.AssetManager;
import com.eden.orchid.impl.relations.ThemeRelation;
import com.google.inject.ImplementedBy;

@ImplementedBy(ThemeServiceImpl.class)
public interface ThemeService extends OrchidService {

    default AssetManager getAssetManager() {
        return getService(ThemeService.class).getAssetManager();
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

    default Theme doWithTheme(ThemeRelation theme, Runnable cb) {
        return getService(ThemeService.class).doWithTheme(theme, cb);
    }

    default void popTheme() {
        getService(ThemeService.class).popTheme();
    }

    default void clearThemes() {
        getService(ThemeService.class).clearThemes();
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

    default void popAdminTheme() {
        getService(ThemeService.class).popAdminTheme();
    }

    default void clearAdminThemes() {
        getService(ThemeService.class).clearAdminThemes();
    }

}
