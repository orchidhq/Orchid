package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.impl.compilers.FrontMatterPrecompiler;
import com.eden.orchid.impl.resources.StringResource;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Set;

public abstract class Theme implements OrchidResourceSource {

    private int resourcePriority = 100;

    private OrchidResources resources;
    private Set<OrchidCompiler> compilers;
    private Set<OrchidPreCompiler> preCompilers;

    @Inject
    public Theme(OrchidResources resources, Set<OrchidCompiler> compilers, Set<OrchidPreCompiler> preCompilers) {
        this.resources = resources;
        this.compilers = compilers;
        this.preCompilers = preCompilers;
    }

    public Class<? extends OrchidPreCompiler> getPrecompilerClass() {
        return FrontMatterPrecompiler.class;
    }

    public boolean shouldContinue() {
        return true;
    }

    public void setResourcePriority(int resourcePriority) {
        this.resourcePriority = resourcePriority;
    }

    @Override
    public final int getResourcePriority() {
        return resourcePriority;
    }

    /**
     * A callback fired on the selected theme when it is first set. By this time, Orchid has registered all compoenents
     * and parsed all Options, but has not yet started a OrchidTask.
     */
    public void onThemeSet() {

    }

    public void generateHomepage() {
        JSONObject frontPageData = new JSONObject(Orchid.getContext().getRoot().toMap());
        OrchidResource readmeResource = resources.getProjectReadme();
        if (readmeResource != null) {
            frontPageData.put("readme", compile(readmeResource.getReference().getExtension(), readmeResource.getContent()));
        }

        OrchidResource licenseResource = resources.getProjectLicense();
        if (licenseResource != null) {
            frontPageData.put("license", compile(licenseResource.getReference().getExtension(), licenseResource.getContent()));
        }

        OrchidResource resource = new StringResource("index.twig", "");
        OrchidPage page = new OrchidPage(resource);

        page.setData(frontPageData);
        page.renderTemplate("templates/pages/frontPage.twig");
    }

    public String compile(String extension, String input, Object... data) {
        for (OrchidCompiler compiler : compilers) {
            if (acceptsExtension(extension, compiler.getSourceExtensions())) {
                return compiler.compile(extension, input, data);
            }
        }

        return input;
    }

    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        for (OrchidPreCompiler compiler : preCompilers) {
            if (compiler.getClass().equals(getPrecompilerClass())) {
                return compiler.getEmbeddedData(input);
            }
        }

        return new EdenPair<>(input, null);
    }

    public String getOutputExtension(String extension) {
        for (OrchidCompiler compiler : compilers) {
            if (acceptsExtension(extension, compiler.getSourceExtensions())) {
                return compiler.getOutputExtension();
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
