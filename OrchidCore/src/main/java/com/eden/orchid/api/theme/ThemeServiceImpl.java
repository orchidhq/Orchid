package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;

import java.util.Stack;

public final class ThemeServiceImpl implements ThemeService {

    private OrchidContext context;

    private Theme defaultTheme;
    private Stack<Theme> themeStack;

    public ThemeServiceImpl() {
        themeStack = new Stack<>();
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public Theme getDefaultTheme() {
        return defaultTheme;
    }

    @Override
    public void setDefaultTheme(Theme theme) {
        this.defaultTheme = theme;
    }

    @Override
    public Theme getTheme() {
        return (themeStack.size() > 0) ? themeStack.peek() : getDefaultTheme();
    }

    @Override
    public void pushTheme(Theme theme) {
        themeStack.push(theme);
    }

    @Override
    public void popTheme() {
        themeStack.pop();
    }

    @Override
    public void clearThemes() {
        defaultTheme.clearCache();
        themeStack.forEach(Theme::clearCache);
    }
}
