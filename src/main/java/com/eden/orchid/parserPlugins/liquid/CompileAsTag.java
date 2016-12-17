package com.eden.orchid.parserPlugins.liquid;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.compilers.SiteResources;
import liqp.TemplateContext;
import liqp.nodes.LNode;
import liqp.tags.Tag;
import org.json.JSONObject;

@AutoRegister
public class CompileAsTag extends Tag {

    public CompileAsTag() {
        super("compile-as");
    }

    @Override
    public Object render(TemplateContext context, LNode... nodes) {
        try {
            String source = super.asString(nodes[1].render(context));

            return SiteResources.compile(super.asString(nodes[0].render(context)), source, new JSONObject(context.getVariables()));
        }
        catch(Exception e) {
            return "";
        }
    }
}