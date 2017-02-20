package com.eden.orchid.impl.render;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.OrchidReference;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OrchidRendererImpl implements OrchidRenderer {

    private OrchidContext context;
    private OrchidResources resources;

    @Inject
    public OrchidRendererImpl(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;
    }

    @Override
    public boolean render(String template, String extension, boolean templateReference, JSONObject pageData, String alias, OrchidReference reference) {
        String templateContent = "";
        if (templateReference) {
            OrchidResource templateResource = resources.getResourceEntry(template);

            if (templateResource == null) {
                templateResource = resources.getResourceEntry("templates/pages/index.twig");
            }
            if (templateResource == null) {
                templateResource = resources.getResourceEntry("templates/pages/index.html");
            }
            if (templateResource == null) {
                return false;
            }

            templateContent = templateResource.getContent();
        }
        else {
            templateContent = (EdenUtils.isEmpty(template)) ? "" : template;
        }

        JSONObject templateVariables = new JSONObject(context.getRoot().toMap());
        templateVariables.put("page", pageData);
        if (!EdenUtils.isEmpty(alias)) {
            templateVariables.put(alias, pageData);
        }

        String content = context.getTheme().compile(extension, templateContent, templateVariables);

        if (content == null) {
            Clog.v("#{$1} compiled to null", new Object[]{reference.toString()});
            content = "";
        }

        String outputPath = reference.getFullPath();
        String outputName = reference.getFileName() + "." + reference.getOutputExtension();

        outputPath = Orchid.getContext().query("options.d").getElement().toString() + File.separator + outputPath.replaceAll("/", File.separator);

        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(outputPath + File.separator + outputName);
            Files.write(classesFile, content.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
