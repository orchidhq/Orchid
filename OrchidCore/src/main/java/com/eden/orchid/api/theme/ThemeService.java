package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.AssetManager;
import com.eden.orchid.impl.relations.ThemeRelation;
import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.Map;

@ImplementedBy(ThemeServiceImpl.class)
public interface ThemeService extends OrchidService {

    default AssetManager getAssetManager() {
        return getService(ThemeService.class).getAssetManager();
    }

    default Theme findTheme(String theme, @Nullable Map<String, Object> params) {
        return getService(ThemeService.class).findTheme(theme, params);
    }
}
