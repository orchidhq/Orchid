package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.options.Option;

@AutoRegister
public class MaxIterationsOption implements Option {

    @Override
    public String getFlag() {
        return "maxIterations";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        if(options.length == 2) {
            try {
                return new JSONElement(Integer.parseInt(options[1]));
            }
            catch (NumberFormatException e) {
                Clog.e("'-maxIterations' option must be an integer, got #{$1}", new Object[] {options[1]});
            }
        }
        else {
            Clog.e("'-maxIterations' option should be of length 2: given #{$1}", new Object[] {options});
        }

        return null;
    }

    @Override
    public JSONElement getDefaultValue() {
        return new JSONElement(5);
    }

    @Override
    public int priority() {
        return 70;
    }
}
