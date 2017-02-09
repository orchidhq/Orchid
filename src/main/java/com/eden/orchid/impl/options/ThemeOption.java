package com.eden.orchid.impl.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.api.options.OrchidOption;

import javax.inject.Singleton;

@Singleton
public class ThemeOption implements OrchidOption {

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

            Theme theme = getRegistrar().resolve(themeClass);
            if(theme != null) {
                Orchid.getContext().setTheme(theme);
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
    public int priority() {
        return 800;
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
