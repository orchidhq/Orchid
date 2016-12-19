package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.options.SiteOption;
import com.eden.orchid.options.SiteOptions;
import com.eden.orchid.Theme;

@AutoRegister
public class ThemeOption extends SiteOption {

    @Override
    public String getFlag() {
        return "theme";
    }

    @Override
    public boolean parseOption(String[] options) {
        if(options.length == 2) {
            try {
                Class<? extends Theme> themeClass = (Class<? extends Theme>) Class.forName(options[1]);
                SiteOptions.siteOptions.put("theme", themeClass.newInstance());
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
            return true;
        }
        else {
            Clog.e("'-theme' option should be of length 2: given #{$1}", new Object[] {options});
            return false;
        }
    }

    @Override
    public void setDefault() {

    }
}
