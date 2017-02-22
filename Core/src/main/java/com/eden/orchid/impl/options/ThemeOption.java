package com.eden.orchid.impl.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.Theme;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OrchidOption;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ThemeOption extends OrchidOption {

    private Provider<OrchidContext> contextProvider;

    @Inject
    public ThemeOption(Provider<OrchidContext> contextProvider) {
        this.contextProvider = contextProvider;
        setPriority(800);
    }

    @Override
    public String getFlag() {
        return "theme";
    }

    @Override
    public String getDescription() {
        return "the fully-qualified classname of your selected Theme object";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        try {
            Class<? extends Theme> themeClass = (Class<? extends Theme>) Class.forName(options[1]);

            Theme theme = contextProvider.get().getInjector().getInstance(themeClass);
            if(theme != null) {
                contextProvider.get().setTheme(theme);
                return new JSONElement(options[1]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Clog.e("The Theme class #{$1} could not be found. Please make sure it has been registered properly.", new Object[] {options[1]});

        return null;
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int optionLength() {
        return 2;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
