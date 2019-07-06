package com.eden.orchid.api.theme;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.AssetManager;
import com.eden.orchid.testhelpers.BaseOrchidTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ThemeServiceTest extends BaseOrchidTest {

//    private Injector injector;
    private OrchidContext context;
    private ThemeService underTest;
    private ThemeServiceImpl service;

    private AssetManager assetManager;

    private JSONObject theme2ContextOptions;
    private JSONObject adminTheme2ContextOptions;
    private JSONObject themeContextOptions;
    private JSONObject adminThemeContextOptions;

    private Theme theme1;
    private Set<Theme> themes;

    private AdminTheme adminTheme1;
    private Set<AdminTheme> adminThemes;

    @BeforeEach
    public void setUp() {
        super.setUp();
        themes = new HashSet<>();
        theme1 = mock(Theme.class);
        when(theme1.getKey()).thenReturn("theme1");
        themes.add(theme1);

        adminTheme1 = mock(AdminTheme.class);
        when(adminTheme1.getKey()).thenReturn("adminTheme1");
        adminThemes = new HashSet<>();
        adminThemes.add(adminTheme1);

        assetManager = mock(AssetManager.class);

        themeContextOptions = new JSONObject();
        theme2ContextOptions = new JSONObject();
        adminThemeContextOptions = new JSONObject();
        adminTheme2ContextOptions = new JSONObject();

        // Mock Injector

        // Mock Context
        context = mock(OrchidContext.class);
        when(context.query("theme")).thenReturn(new JSONElement(themeContextOptions));
        when(context.query("theme2")).thenReturn(new JSONElement(theme2ContextOptions));
        when(context.query("adminTheme")).thenReturn(new JSONElement(adminThemeContextOptions));
        when(context.query("adminTheme2")).thenReturn(new JSONElement(adminTheme2ContextOptions));

        when(context.resolve((Class<Theme>) theme1.getClass())).thenReturn(theme1);
        when(context.resolve((Class<AdminTheme>) adminTheme1.getClass())).thenReturn(adminTheme1);

        // Create instance of Service Implementation
        service = new ThemeServiceImpl(assetManager, () -> themes,  "theme1", () -> adminThemes, "adminTheme1");
        service.initialize(context);

        // Create wrapper around the Implementation to verify it works in composition
        underTest = new ThemeService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void getGlobalAssetHolder() throws Throwable {
        assertThat(underTest.getAssetManager(), is(assetManager));
    }

    @Test
    public void getTheme() throws Throwable {
        assertThat(underTest.getTheme(), is(theme1));
    }

    @Test
    public void findTheme() throws Throwable {
        assertThat(underTest.findTheme("theme1"), is(theme1));
        assertThat(underTest.findTheme("theme2"), is(nullValue()));
    }

    @Test
    public void pushAndPopTheme() throws Throwable {
        Theme theme2 = mock(Theme.class);
        when(theme2 .getKey()).thenReturn("theme2");
        when(context.resolve((Class<Theme>) theme2.getClass())).thenReturn(theme2);

        underTest.pushTheme(theme2);
        assertThat(underTest.getTheme(), is(theme2));
        underTest.popTheme();
        assertThat(underTest.getTheme(), is(theme1));

        underTest.pushTheme(theme2);
        assertThat(underTest.getTheme(), is(theme2));
        underTest.clearThemes();
        assertThat(underTest.getTheme(), is(theme1));
    }

    @Test
    public void getAdminTheme() throws Throwable {
        assertThat(underTest.getAdminTheme(), is(adminTheme1));
    }

    @Test
    public void findAdminTheme() throws Throwable {
        assertThat(underTest.findAdminTheme("adminTheme1"), is(adminTheme1));
        assertThat(underTest.findAdminTheme("adminTheme2"), is(nullValue()));
    }

    @Test
    public void pushAndPopAdminTheme() throws Throwable {
        AdminTheme adminTheme2 = mock(AdminTheme.class);
        when(adminTheme2.getKey()).thenReturn("adminTheme2");
        when(context.resolve((Class<AdminTheme>) adminTheme2.getClass())).thenReturn(adminTheme2);

        underTest.pushAdminTheme(adminTheme2);
        assertThat(underTest.getAdminTheme(), is(adminTheme2));
        underTest.popAdminTheme();
        assertThat(underTest.getAdminTheme(), is(adminTheme1));

        underTest.pushAdminTheme(adminTheme2);
        assertThat(underTest.getAdminTheme(), is(adminTheme2));
        underTest.clearAdminThemes();
        assertThat(underTest.getAdminTheme(), is(adminTheme1));
    }

}