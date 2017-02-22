package com.eden.orchid.impl.render;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResources;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class OrchidRendererImpl extends OrchidRenderer {

    private OrchidContext context;

    @Inject
    public OrchidRendererImpl(OrchidContext context, Set<TemplateResolutionStrategy> strategies, OrchidResources resources) {
        super(strategies, resources);
        this.context = context;
    }

    protected boolean render(OrchidPage page, String extension, String content) {
        JSONObject templateVariables = new JSONObject(context.getRoot().toMap());
        templateVariables.put("page", page.getData());
        if (!EdenUtils.isEmpty(page.getType())) {
            templateVariables.put(page.getType(), page.getData());
        }

        content = "" + context.getTheme().compile(extension, content, templateVariables);

        String outputPath = page.getReference().getFullPath();
        String outputName = page.getReference().getFileName() + "." + page.getReference().getOutputExtension();

        outputPath = context.query("options.d").getElement().toString() + File.separator + outputPath.replaceAll("/", File.separator);

        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(outputPath + File.separator + outputName);
            Files.write(classesFile, content.getBytes());
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
