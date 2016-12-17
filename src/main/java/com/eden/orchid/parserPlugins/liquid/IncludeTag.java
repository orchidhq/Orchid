package com.eden.orchid.parserPlugins.liquid;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidUtils;
import com.eden.orchid.compilers.SiteResources;
import liqp.TemplateContext;
import liqp.nodes.LNode;
import liqp.tags.Tag;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

@AutoRegister
public class IncludeTag extends Tag {

    public IncludeTag() {
        super("include");
    }

    @Override
    public Object render(TemplateContext context, LNode... nodes) {
        try {
            String includeResource = super.asString(nodes[0].render(context));
            String includedTemplate = OrchidUtils.getResourceFileContents("assets/includes/" + includeResource);

            context.put("root", Orchid.all);

            if(nodes.length > 1) {
                Object value = nodes[1].render(context);
                context.put(includeResource, value);
            }

            return SiteResources.compile(FilenameUtils.getExtension(includeResource), includedTemplate, new JSONObject(context.getVariables()));
        }
        catch(Exception e) {
            return "";
        }
    }
}