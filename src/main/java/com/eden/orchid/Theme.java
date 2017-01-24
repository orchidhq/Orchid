package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.PreCompiler;
import com.eden.orchid.compilers.SiteCompilers;
import com.eden.orchid.resources.OrchidPage;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.resources.OrchidResources;
import com.eden.orchid.resources.ResourceSource;
import com.eden.orchid.resources.impl.StringResource;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import java.util.Map;

public abstract class Theme implements ResourceSource {
    /**
     * Get the class of the precompiler required for this theme.
     *
     * @return  the class of the required precompiler
     */
    public abstract Class<? extends PreCompiler> getPrecompilerClass();

    /**
     * Get an array of classes defining additional compilers required to use this theme.
     *
     * @return  Get a list of compilers that are required to use this theme
     */
    public abstract Class<? extends Compiler>[] getRequiredCompilers();

    @Override
    public int resourcePriority() {
        return 20;
    }

    public String[] getMissingOptions() {
        return new String[] {};
    }

    public void generateHomepage(Object... data) {
        JSONObject frontPageData = new JSONObject(Orchid.getRoot().toMap());
        String readmeBody = OrchidResources.getProjectReadme();
        if(readmeBody != null) {
            frontPageData.put("readme", readmeBody);
        }

        String licenseBody = OrchidResources.getProjectLicense();
        if(licenseBody != null) {
            frontPageData.put("license", licenseBody);
        }

        OrchidResource resource = new StringResource("index.twig", "");
        OrchidPage page = new OrchidPage(resource);
        page.setData(frontPageData);
        page.renderTemplate("templates/pages/frontPage.twig");
    }

    public String compile(String extension, String input, Object... data) {
        for(Map.Entry<Integer, Compiler> compiler : SiteCompilers.compilers.entrySet()) {
            if(OrchidUtils.acceptsExtension(extension, compiler.getValue().getSourceExtensions())) {
                return compiler.getValue().compile(extension, input, data);
            }
        }

        return input;
    }

    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        for(Map.Entry<Integer, PreCompiler> compiler : SiteCompilers.precompilers.entrySet()) {
            if(compiler.getValue().getClass().equals(getPrecompilerClass())) {
                return compiler.getValue().getEmbeddedData(input);
            }
        }

        return new EdenPair<>(input, null);
    }

    public String getOutputExtension(String extension) {
        for(Map.Entry<Integer, Compiler> compiler : SiteCompilers.compilers.entrySet()) {
            if(OrchidUtils.acceptsExtension(extension, compiler.getValue().getSourceExtensions())) {
                return compiler.getValue().getOutputExtension();
            }
        }

        return extension;
    }
}
