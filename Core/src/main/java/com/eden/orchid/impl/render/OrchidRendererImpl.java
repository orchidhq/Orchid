package com.eden.orchid.impl.render;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import org.apache.commons.io.FilenameUtils;
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
    public boolean renderTemplate(OrchidPage page, String template) {
        OrchidResource templateResource = resources.getResourceEntry(template);

        if (templateResource == null) {
            return false;
        }

        String extension = FilenameUtils.getExtension(template);
        String templateContent = templateResource.getContent();

        return renderInternal(page, extension, templateContent);
    }

    @Override
    public boolean renderString(OrchidPage page, String extension, String content) {
        return renderInternal(page, extension, content);
    }

    @Override
    public boolean renderRaw(OrchidPage page) {
        return renderInternal(page, page.getResource().getReference().getExtension(), page.getResource().getContent());
    }

    private boolean renderInternal(OrchidPage page, String extension, String templateContent) {
        JSONObject templateVariables = new JSONObject(context.getRoot().toMap());
        templateVariables.put("page", page.getData());
        if (!EdenUtils.isEmpty(page.getAlias())) {
            templateVariables.put(page.getAlias(), page.getData());
        }

        String content = "" + context.getTheme().compile(extension, templateContent, templateVariables);

        String outputPath = page.getReference().getFullPath();
        String outputName = page.getReference().getFileName() + "." + page.getReference().getOutputExtension();

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
