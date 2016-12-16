package com.eden.orchid;

import com.eden.orchid.compiler.SiteResources;
import com.eden.orchid.discover.DiscoverAll;
import com.eden.orchid.options.SiteOptions;
import com.sun.javadoc.RootDoc;
import liqp.Template;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Orchid {
    public static int optionLength(String option) {
        return SiteOptions.optionLength(option);
    }

    public static boolean start(RootDoc root) {
        // Discover all resources, options, and documentation data so it can then be sent to the liquid renderer
        JSONObject obj = new JSONObject();
        obj.put("site", SiteOptions.startDiscovery(root));
        obj.put("res",  SiteResources.startDiscovery(root));
        obj.put("orchid", DiscoverAll.startDiscovery(root));

        // converts all data to JSON that can be loaded as a JS object on the rendered page. Send this to renderer with other data
        obj.put("docsJson", obj.toString(2));

        // Render index page
        Path file = Paths.get(SiteOptions.outputDir + "/index.html");
        try {
            Template template = Template.parse(JarUtils.getResource("assets/html/index.html"));
            Files.write(file, template.render(obj.toString(2)).getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Render intermediate package pages

        // Render classDoc pages

        // Render static pages

        // Render blog pages

        return true;
    }
}
