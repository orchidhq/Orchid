package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.GlobalAssetHolder;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"}, dependsOnGroups = {"theme"})
public final class ThemeServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private ThemeService underTest;
    private ThemeServiceImpl service;

    private GlobalAssetHolder globalAssetHolder;

    private JSONObject theme2ContextOptions;
    private JSONObject adminTheme2ContextOptions;
    private JSONObject themeContextOptions;
    private JSONObject adminThemeContextOptions;

    private Theme theme1;
    private Set<Theme> themes;

    private AdminTheme adminTheme1;
    private Set<AdminTheme> adminThemes;

    @BeforeMethod
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


        // Mock Context
        context = mock(OrchidContext.class);
        when(context.query("theme")).thenReturn(new JSONElement(themeContextOptions));
        when(context.query("theme2")).thenReturn(new JSONElement(theme2ContextOptions));
        when(context.query("adminTheme")).thenReturn(new JSONElement(adminThemeContextOptions));
        when(context.query("adminTheme2")).thenReturn(new JSONElement(adminTheme2ContextOptions));

        // Create instance of Service Implementation
        service = new ThemeServiceImpl(globalAssetHolder, () -> themes,  "theme1", () -> adminThemes, "adminTheme1");
        service.initialize(context);

        // Create wrapper around the Implementation to verify it works in composition
        underTest = new ThemeService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void getGlobalAssetHolder() throws Throwable {
        assertThat(underTest.getGlobalAssetHolder(), is(globalAssetHolder));
    }

    @Test
    public void getDefaultTheme() throws Throwable {
        assertThat(underTest.getDefaultTheme(), is(theme1));
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

        underTest.pushTheme(theme2);
        assertThat(underTest.getTheme(), is(theme2));
        underTest.popTheme();
        assertThat(underTest.getTheme(), is(theme1));

        underTest.pushTheme(theme2, new JSONObject());
        assertThat(underTest.getTheme(), is(theme2));
        underTest.clearThemes();
        assertThat(underTest.getTheme(), is(theme1));
    }

    @Test(enabled = false)
    public void pushThemeInitialization() throws Throwable {
        // these tests need to be rewritten, as the theme options now get merged instead of overwritten. We should now
        // check for specific properties set in the theme after extracting options
    }

    @Test
    public void getDefaultAdminTheme() throws Throwable {
        assertThat(underTest.getDefaultAdminTheme(), is(adminTheme1));
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

        underTest.pushAdminTheme(adminTheme2);
        assertThat(underTest.getAdminTheme(), is(adminTheme2));
        underTest.popAdminTheme();
        assertThat(underTest.getAdminTheme(), is(adminTheme1));

        underTest.pushAdminTheme(adminTheme2, new JSONObject());
        assertThat(underTest.getAdminTheme(), is(adminTheme2));
        underTest.clearAdminThemes();
        assertThat(underTest.getAdminTheme(), is(adminTheme1));
    }

    @Test(enabled = false)
    public void pushAdminThemeInitialization() throws Throwable {
        // these tests need to be rewritten, as the theme options now get merged instead of overwritten. We should now
        // check for specific properties set in the theme after extracting options
    }

}