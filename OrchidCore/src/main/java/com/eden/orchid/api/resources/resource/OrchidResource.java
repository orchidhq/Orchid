package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.theme.pages.OrchidReference;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Resources provide the "intrinsic content" of a page.
 *
 * @orchidApi extensible
 * @since v1.0.0
 */
public abstract class OrchidResource {

    // TODO: remove this. Resources shouldn't _have_ a resource, but pages should _create_ a reference from APIs on the resource
    protected final OrchidReference reference;

    protected String rawContent;
    protected String content;
    protected JSONElement embeddedData;

    public OrchidResource(OrchidReference reference) {
        if (reference == null) {
            throw new IllegalArgumentException("A resource must have a valid OrchidReference");
        } else {
            this.reference = reference;
        }
    }

    public boolean shouldPrecompile() {
        JSONElement data = getEmbeddedData();
        if (EdenUtils.elementIsObject(data) && ((JSONObject) data.getElement()).has("precompile")) {
            return ((JSONObject) data.getElement()).getBoolean("precompile");
        }

        return reference.getContext().resolve(OrchidPrecompiler.class).shouldPrecompile(reference.getExtension(), getRawContent());
    }

    public boolean shouldRender() {
        return true;
    }

    public InputStream getContentStream() {
        return IOUtils.toInputStream(getRawContent(), StandardCharsets.UTF_8);
    }

    public String compileContent(Object data) {
        if (!EdenUtils.isEmpty(getContent())) {
            String compiledContent = getContent();

            if (shouldPrecompile()) {
                compiledContent = reference.getContext().compile(getPrecompilerExtension(), compiledContent, data);
            }

            return reference.getContext().compile(
                    getReference().getExtension(),
                    compiledContent,
                    data
            );
        } else {
            return "";
        }
    }

    public String getPrecompilerExtension() {
        JSONElement data = getEmbeddedData();
        if (EdenUtils.elementIsObject(data) && ((JSONObject) data.getElement()).has("precompileAs")) {
            return ((JSONObject) data.getElement()).getString("precompileAs");
        }

        return reference.getContext().getDefaultPrecompilerExtension();
    }

    public boolean canUpdate() {
        return false;
    }

    public boolean canDelete() {
        return false;
    }

    public void update(InputStream newContent) throws IOException {

    }

    public void delete() throws IOException {

    }

    @Override
    public String toString() {
        return "OrchidResource[" + this.getClass().getSimpleName() + "]{" +
                "reference=" + reference.getRelativePath() +
                '}';
    }

// Delombok
//----------------------------------------------------------------------------------------------------------------------

    public OrchidReference getReference() {
        return this.reference;
    }

    public String getContent() {
        loadContent();
        return this.content;
    }

    public String getRawContent() {
        loadContent();
        return this.rawContent;
    }

    public JSONElement getEmbeddedData() {
        loadContent();
        return this.embeddedData;
    }

// Freeable Resource Impl
//----------------------------------------------------------------------------------------------------------------------

    protected void loadContent() {
        if(rawContent != null) {
            EdenPair<String, Map<String, Object>> parsedContent = reference.getContext().getEmbeddedData(reference.getExtension(), rawContent);
            this.content = parsedContent.first;
            this.embeddedData = new JSONElement(new JSONObject(parsedContent.second));
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;
        }
    }

    public void free() {
        rawContent = null;
        content = null;
    }

}
