package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.options.Option;

@AutoRegister
public class ThemeOption implements Option {

    @Override
    public String getFlag() {
        return "theme";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        if(options.length == 2) {
            try {
                Class<? extends Theme> themeClass = (Class<? extends Theme>) Class.forName(options[1]);
                Orchid.setTheme(themeClass.newInstance());

                return new JSONElement(options[1]);
            }
            catch (ClassNotFoundException e) {
                Clog.e("The Theme class #{$1} could not be found on the classpath.", new Object[] {options[1]});
            }
            catch (ClassCastException e) {
                Clog.e("The Theme class #{$1} does not implement #{2}.", new Object[] {options[1], Theme.class.getName()});
            }
            catch (InstantiationException|IllegalAccessException e) {
                Clog.e("The Theme class #{$1} could not be created. Make sure it has a public no-arg constructor.", new Object[] {options[1]});
            }
        }
        else {
            Clog.e("'-theme' option should be of length 2: given #{$1}", new Object[] {options});
        }

        return null;
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 80;
    }
}
