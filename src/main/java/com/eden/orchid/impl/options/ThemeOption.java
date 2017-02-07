package com.eden.orchid.impl.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.options.Option;
import com.eden.orchid.resources.ResourceSource;
import com.eden.orchid.utilities.AutoRegister;

import java.util.Map;

@AutoRegister
public class ThemeOption implements Option {

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
        for(Map.Entry<Integer, ResourceSource> resourceSourceEntry : Orchid.getResources().getResourceSources().entrySet()) {
            if (resourceSourceEntry.getValue() instanceof Theme) {
                Theme theme = (Theme) resourceSourceEntry.getValue();

                if (theme.getClass().getName().equals(options[1])) {
                    Orchid.setTheme(theme);
                    return new JSONElement(options[1]);
                }
            }
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
