package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Set;

public abstract class Theme extends DefaultResourceSource {

    private Provider<OrchidResources> resources;
    private Set<OrchidCompiler> compilers;
    private Set<OrchidPreCompiler> preCompilers;

    @Inject
    public Theme(Provider<OrchidResources> resources, Set<OrchidCompiler> compilers, Set<OrchidPreCompiler> preCompilers) {
        this.resources = resources;
        this.compilers = new ObservableTreeSet<>(compilers);
        this.preCompilers = new ObservableTreeSet<>(preCompilers);
    }

    public Class<? extends OrchidPreCompiler> getPrecompilerClass() {
        return FrontMatterPrecompiler.class;
    }

    public boolean shouldContinue() {
        return true;
    }

    /**
     * A callback fired on the selected theme when it is first set. By this time, Orchid has registered all compoenents
     * and parsed all Options, but has not yet started a OrchidTask.
     */
    public void onThemeSet() {

    }

    public void generateHomepage() {
        JSONObject frontPageData = new JSONObject(getContext().getRoot().toMap());
        OrchidResource readmeResource = resources.get().getProjectReadme();
        if (readmeResource != null) {
            frontPageData.put("readme", compile(readmeResource.getReference().getExtension(), readmeResource.getContent()));
        }

        OrchidResource licenseResource = resources.get().getProjectLicense();
        if (licenseResource != null) {
            frontPageData.put("license", compile(licenseResource.getReference().getExtension(), licenseResource.getContent()));
        }

        OrchidResource resource = new StringResource("index.twig", "");
        OrchidPage page = new OrchidPage(resource);

        page.setData(frontPageData);
        page.renderTemplate("templates/pages/frontPage.twig");
    }

    public String compile(String extension, String input, Object... data) {
        OrchidCompiler compiler = compilerFor(extension);

        if(compiler != null) {
            return compiler.compile(extension, input, data);
        }

        return input;
    }

    public OrchidCompiler compilerFor(String extension) {
        for (OrchidCompiler compiler : compilers) {
            if (acceptsExtension(extension, compiler.getSourceExtensions())) {
                return compiler;
            }
        }

        return null;
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
        OrchidCompiler compiler = compilerFor(extension);

        if(compiler != null) {
            return compiler.getOutputExtension();
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
