package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.AssetManager;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.relations.ThemeRelation;
import com.google.inject.ImplementedBy;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@ImplementedBy(ThemeServiceImpl.class)
public interface ThemeService extends OrchidService {

    default AssetManager getAssetManager() {
        return getService(ThemeService.class).getAssetManager();
    }

    default Theme getTheme() {
        return getService(ThemeService.class).getTheme();
    }

    default ThemeRelation getDefaultThemeRelation() {
        return getService(ThemeService.class).getDefaultThemeRelation();
    }

    default Theme findTheme(String theme) {
        return getService(ThemeService.class).findTheme(theme);
    }

    default void pushTheme(Theme theme) {
        getService(ThemeService.class).pushTheme(theme);
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
