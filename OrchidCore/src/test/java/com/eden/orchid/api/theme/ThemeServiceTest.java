package com.eden.orchid.api.theme;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.AssetManager;
import com.eden.orchid.testhelpers.OrchidUnitTest;
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

public final class ThemeServiceTest implements OrchidUnitTest {

    private OrchidContext context;
    private ThemeService underTest;

    private AssetManager assetManager;

    private JSONObject theme2ContextOptions;
    private JSONObject adminTheme2ContextOptions;
    private JSONObject themeContextOptions;
    private JSONObject adminThemeContextOptions;

    private Theme theme1;
    private Theme theme2;
    private Set<Theme> themes;

    private AdminTheme adminTheme1;
    private AdminTheme adminTheme2;
    private Set<AdminTheme> adminThemes;

    public class Theme1 extends Theme {
        public Theme1(OrchidContext context) {
            super(context, "theme1");
        }
    }

    public class Theme2 extends Theme {
        public Theme2(OrchidContext context) {
            super(context, "theme2");
        }
    }

    public class AdminTheme1 extends AdminTheme {
        public AdminTheme1(OrchidContext context) {
            super(context, "AdminTheme1");
        }
    }

    public class AdminTheme2 extends AdminTheme {
        public AdminTheme2(OrchidContext context) {
            super(context, "AdminTheme2");
        }
    }

    @BeforeEach
    public void setUp() {
        themes = new HashSet<>();
        theme1 = mock(Theme1.class);
        theme2 = mock(Theme2.class);
        when(theme1.getKey()).thenReturn("theme1");
        when(theme2.getKey()).thenReturn("theme2");
        when(theme1.toString()).thenReturn("theme1.toString()");
        when(theme2.toString()).thenReturn("theme2.toString()");
        themes.add(theme1);

        adminThemes = new HashSet<>();
        adminTheme1 = mock(AdminTheme1.class);
        adminTheme2 = mock(AdminTheme2.class);
        when(adminTheme1.getKey()).thenReturn("adminTheme1");
        when(adminTheme2.getKey()).thenReturn("adminTheme2");
        when(adminTheme1.toString()).thenReturn("adminTheme1.toString()");
        when(adminTheme2.toString()).thenReturn("adminTheme2.toString()");
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
        when(context.resolve((Class<Theme>) theme2.getClass())).thenReturn(theme2);
        when(context.resolve((Class<AdminTheme>) adminTheme1.getClass())).thenReturn(adminTheme1);
        when(context.resolve((Class<AdminTheme>) adminTheme2.getClass())).thenReturn(adminTheme2);

        // Create instance of Service Implementation
        ThemeServiceImpl service = new ThemeServiceImpl(assetManager, () -> themes, "theme1", () -> adminThemes, "adminTheme1");
        service.initialize(context);

        // Create wrapper around the Implementation to verify it works in composition
        underTest = new ThemeService() {
            public void initialize(OrchidContext context) {
            }

            public <T extends OrchidService> T getService(Class<T> serviceClass) {
                return (T) service;
            }
        };
    }

    @Test
    public void getGlobalAssetHolder() throws Throwable {
        assertThat(underTest.getAssetManager(), is(assetManager));
    }

    @Test
    public void getTheme() throws Throwable {
        underTest.clearThemes();
        assertThat(underTest.getTheme(), is(theme1));
    }

    @Test
    public void findTheme() throws Throwable {
        underTest.clearThemes();
        assertThat(underTest.findTheme("theme1"), is(theme1));
        assertThat(underTest.findTheme("theme2"), is(nullValue()));
    }

    @Test
    public void pushAndPopTheme() throws Throwable {
        underTest.clearThemes();

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
        underTest.clearAdminThemes();
        assertThat(underTest.getAdminTheme(), is(adminTheme1));
    }

    @Test
    public void findAdminTheme() throws Throwable {
        underTest.clearAdminThemes();
        assertThat(underTest.findAdminTheme("adminTheme1"), is(adminTheme1));
        assertThat(underTest.findAdminTheme("adminTheme2"), is(nullValue()));
    }

    @Test
    public void pushAndPopAdminTheme() throws Throwable {
        underTest.clearAdminThemes();

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
