package com.eden.orchid.compilers.impl;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.compilers.PageCompiler;
import liqp.Template;
import liqp.parser.Flavor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@AutoRegister
public class LiquidCompiler implements PageCompiler {
    @Override
    public String compile(String input, JSONObject json) {
        try {
            return Template.parse(input, Flavor.LIQUID).render(json.toString(2));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String compile(String input, JSONArray json) {
        try {
            return Template.parse(input, Flavor.LIQUID).render(json.toString(2));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public int priority() {
        return 100;
    }
}
