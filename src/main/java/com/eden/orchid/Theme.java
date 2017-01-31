package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.PreCompiler;
import com.eden.orchid.compilers.SiteCompilers;
import com.eden.orchid.impl.compilers.FrontMatterPrecompiler;
import com.eden.orchid.resources.OrchidPage;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.resources.ResourceSource;
import com.eden.orchid.impl.resources.StringResource;
import org.json.JSONObject;

import java.util.Map;

public abstract class Theme implements ResourceSource {

    public Class<? extends PreCompiler> getPrecompilerClass() {
        return FrontMatterPrecompiler.class;
    }

    public boolean shouldContinue() {
        return true;
    }

    @Override
    public int resourcePriority() {
        return 20;
    }

    public void generateHomepage() {
        JSONObject frontPageData = new JSONObject(Orchid.getRoot().toMap());
        OrchidResource readmeResource = Orchid.getResources().getProjectReadme();
        if (readmeResource != null) {
            frontPageData.put("readme", compile(readmeResource.getReference().getExtension(), readmeResource.getContent()));
        }

        OrchidResource licenseResource = Orchid.getResources().getProjectLicense();
        if (licenseResource != null) {
            frontPageData.put("license", compile(licenseResource.getReference().getExtension(), licenseResource.getContent()));
        }

        OrchidResource resource = new StringResource("index.twig", "");
        OrchidPage page = new OrchidPage(resource);
        page.setData(frontPageData);
        page.renderTemplate("templates/pages/frontPage.twig");
    }

    public String compile(String extension, String input, Object... data) {
        for (Map.Entry<Integer, Compiler> compiler : SiteCompilers.compilers.entrySet()) {
            if (acceptsExtension(extension, compiler.getValue().getSourceExtensions())) {
                return compiler.getValue().compile(extension, input, data);
            }
        }

        return input;
    }

    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        for (Map.Entry<Integer, PreCompiler> compiler : SiteCompilers.precompilers.entrySet()) {
            if (compiler.getValue().getClass().equals(getPrecompilerClass())) {
                return compiler.getValue().getEmbeddedData(input);
            }
        }

        return new EdenPair<>(input, null);
    }

    public String getOutputExtension(String extension) {
        for (Map.Entry<Integer, Compiler> compiler : SiteCompilers.compilers.entrySet()) {
            if (acceptsExtension(extension, compiler.getValue().getSourceExtensions())) {
                return compiler.getValue().getOutputExtension();
            }
        }

        return extension;
    }

    private boolean acceptsExtension(String sourceExt, String[] acceptedExts) {
        for (String ext : acceptedExts) {
            if (ext.equalsIgnoreCase(sourceExt)) {
                return true;
            }
        }

        return false;
    }
}
