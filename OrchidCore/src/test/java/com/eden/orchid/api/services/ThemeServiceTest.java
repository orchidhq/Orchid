package com.eden.orchid.api.services;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.ThemeService;
import com.eden.orchid.api.theme.ThemeServiceImpl;
import com.eden.orchid.api.theme.assets.GlobalAssetHolder;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public final class ThemeServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private ThemeService serviceDelegate;
    private ThemeServiceImpl underTest;

    private GlobalAssetHolder globalAssetHolder;

    private JSONObject theme2ContextOptions;
    private JSONObject adminTheme2ContextOptions;
    private JSONObject themeContextOptions;
    private JSONObject adminThemeContextOptions;

    private Theme theme1;
    private Set<Theme> themes;

    private AdminTheme adminTheme1;
    private Set<AdminTheme> adminThemes;

    @Before
    public void testSetup() {
        themes = new HashSet<>();
        theme1 = mock(Theme.class);
        when(theme1.getKey()).thenReturn("theme1");
        themes.add(theme1);

        adminTheme1 = mock(AdminTheme.class);
        when(adminTheme1.getKey()).thenReturn("adminTheme1");
        adminThemes = new HashSet<>();
        adminThemes.add(adminTheme1);

        globalAssetHolder = mock(GlobalAssetHolder.class);

        themeContextOptions = new JSONObject();
        theme2ContextOptions = new JSONObject();
        adminThemeContextOptions = new JSONObject();
        adminTheme2ContextOptions = new JSONObject();

        // test the service directly
        context = mock(OrchidContext.class);
        when(context.query("theme")).thenReturn(new JSONElement(themeContextOptions));
        when(context.query("theme2")).thenReturn(new JSONElement(theme2ContextOptions));
        when(context.query("adminTheme")).thenReturn(new JSONElement(adminThemeContextOptions));
        when(context.query("adminTheme2")).thenReturn(new JSONElement(adminTheme2ContextOptions));

        underTest = new ThemeServiceImpl(globalAssetHolder, () -> themes,  "theme1", () -> adminThemes, "adminTheme1");
        underTest.initialize(context);

        // test that the public implementation is identical to the real implementation
        serviceDelegate = new ThemeService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) underTest; }
        };
    }

    @Test
    public void getGlobalAssetHolder() throws Throwable {
        assertThat(underTest.getGlobalAssetHolder(), is(globalAssetHolder));

        assertThat(serviceDelegate.getGlobalAssetHolder(), is(globalAssetHolder));
    }

    @Test
    public void getDefaultTheme() throws Throwable {
        assertThat(underTest.getDefaultTheme(), is(theme1));

        assertThat(serviceDelegate.getDefaultTheme(), is(theme1));
    }

    @Test
    public void getTheme() throws Throwable {
        assertThat(underTest.getTheme(), is(theme1));

        assertThat(serviceDelegate.getTheme(), is(theme1));
    }

    @Test
    public void findTheme() throws Throwable {
        assertThat(underTest.findTheme("theme1"), is(theme1));
        assertThat(underTest.findTheme("theme2"), is(nullValue()));

        assertThat(serviceDelegate.findTheme("theme1"), is(theme1));
        assertThat(serviceDelegate.findTheme("theme2"), is(nullValue()));
    }

    @Test
    public void pushAndPopTheme() throws Throwable {
        Theme theme2 = mock(Theme.class);
        when(theme2 .getKey()).thenReturn("theme2");

        underTest.pushTheme(theme2);
        assertThat(underTest.getTheme(), is(theme2));
        underTest.popTheme();
        assertThat(underTest.getTheme(), is(theme1));

        underTest.pushTheme(theme2, new JSONObject());
        assertThat(underTest.getTheme(), is(theme2));
        underTest.clearThemes();
        assertThat(underTest.getTheme(), is(theme1));
    }

    @Test
    public void pushAndPopThemeDelegate() throws Throwable {
        Theme theme2 = mock(Theme.class);
        when(theme2 .getKey()).thenReturn("theme2");

        serviceDelegate.pushTheme(theme2);
        assertThat(serviceDelegate.getTheme(), is(theme2));
        serviceDelegate.popTheme();
        assertThat(serviceDelegate.getTheme(), is(theme1));

        serviceDelegate.pushTheme(theme2, new JSONObject());
        assertThat(serviceDelegate.getTheme(), is(theme2));
        serviceDelegate.clearThemes();
        assertThat(serviceDelegate.getTheme(), is(theme1));
    }

    @Test
    public void pushThemeInitialization() throws Throwable {
        // Test that if a options key is given for a theme, it will use that
        Theme theme2 = mock(Theme.class);
        when(theme2.getKey()).thenReturn("theme2");
        underTest.pushTheme(theme2);
        verify(theme2).extractOptions(context, theme2ContextOptions);
        underTest.popTheme();

        // Test that if not and "theme" is given, it will fallback to that
        Theme theme3 = mock(Theme.class);
        when(theme3.getKey()).thenReturn("theme3");
        underTest.pushTheme(theme3);
        verify(theme3).extractOptions(context, themeContextOptions);
        underTest.popTheme();

        // Test that if neither is given, it will fallback to full options object
        JSONObject optionsData = new JSONObject();
        when(context.query("theme")).thenReturn(null);
        when(context.getOptionsData()).thenReturn(optionsData);

        underTest.pushTheme(theme3);
        verify(theme3).extractOptions(context, optionsData);
        underTest.popTheme();

        // Test that if we pass it options directly when pushing the theme, it will use that
        JSONObject customOptionsData = new JSONObject();
        underTest.pushTheme(theme3, customOptionsData);
        verify(theme3).extractOptions(context, customOptionsData);
        underTest.popTheme();
    }

    @Test
    public void getDefaultAdminTheme() throws Throwable {
        assertThat(underTest.getDefaultAdminTheme(), is(adminTheme1));

        assertThat(serviceDelegate.getDefaultAdminTheme(), is(adminTheme1));
    }

    @Test
    public void getAdminTheme() throws Throwable {
        assertThat(underTest.getAdminTheme(), is(adminTheme1));

        assertThat(serviceDelegate.getAdminTheme(), is(adminTheme1));
    }

    @Test
    public void findAdminTheme() throws Throwable {
        assertThat(underTest.findAdminTheme("adminTheme1"), is(adminTheme1));
        assertThat(underTest.findAdminTheme("adminTheme2"), is(nullValue()));

        assertThat(serviceDelegate.findAdminTheme("adminTheme1"), is(adminTheme1));
        assertThat(serviceDelegate.findAdminTheme("adminTheme2"), is(nullValue()));
    }

    @Test
    public void pushAndPopAdminTheme() throws Throwable {
        AdminTheme adminTheme2 = mock(AdminTheme.class);
        when(adminTheme2.getKey()).thenReturn("adminTheme2");

        underTest.pushAdminTheme(adminTheme2);
        assertThat(underTest.getAdminTheme(), is(adminTheme2));
        underTest.popAdminTheme();
        assertThat(underTest.getAdminTheme(), is(adminTheme1));

        underTest.pushAdminTheme(adminTheme2, new JSONObject());
        assertThat(underTest.getAdminTheme(), is(adminTheme2));
        underTest.clearAdminThemes();
        assertThat(underTest.getAdminTheme(), is(adminTheme1));
    }

    @Test
    public void pushAndPopAdminThemeDelegate() throws Throwable {
        AdminTheme adminTheme2 = mock(AdminTheme.class);
        when(adminTheme2.getKey()).thenReturn("adminTheme2");

        serviceDelegate.pushAdminTheme(adminTheme2);
        assertThat(serviceDelegate.getAdminTheme(), is(adminTheme2));
        serviceDelegate.popAdminTheme();
        assertThat(serviceDelegate.getAdminTheme(), is(adminTheme1));

        serviceDelegate.pushAdminTheme(adminTheme2, new JSONObject());
        assertThat(serviceDelegate.getAdminTheme(), is(adminTheme2));
        serviceDelegate.clearAdminThemes();
        assertThat(serviceDelegate.getAdminTheme(), is(adminTheme1));
    }

    @Test
    public void pushAdminThemeInitialization() throws Throwable {
        // Test that if a options key is given for a theme, it will use that
        AdminTheme adminTheme2 = mock(AdminTheme.class);
        when(adminTheme2.getKey()).thenReturn("adminTheme2");
        underTest.pushAdminTheme(adminTheme2);
        verify(adminTheme2).extractOptions(context, adminTheme2ContextOptions);
        underTest.popAdminTheme();

        // Test that if not and "adminTheme" is given, it will fallback to that
        AdminTheme adminTheme3 = mock(AdminTheme.class);
        when(adminTheme3.getKey()).thenReturn("adminTheme3");
        underTest.pushAdminTheme(adminTheme3);
        verify(adminTheme3).extractOptions(context, adminThemeContextOptions);
        underTest.popAdminTheme();

        // Test that if neither is given, it will fallback to full options object
        JSONObject optionsData = new JSONObject();
        when(context.query("adminTheme")).thenReturn(null);
        when(context.getOptionsData()).thenReturn(optionsData);

        underTest.pushAdminTheme(adminTheme3);
        verify(adminTheme3).extractOptions(context, optionsData);
        underTest.popAdminTheme();

        // Test that if we pass it options directly when pushing the theme, it will use that
        JSONObject customOptionsData = new JSONObject();
        underTest.pushAdminTheme(adminTheme3, customOptionsData);
        verify(adminTheme3).extractOptions(context, customOptionsData);
        underTest.popAdminTheme();
    }
    

}