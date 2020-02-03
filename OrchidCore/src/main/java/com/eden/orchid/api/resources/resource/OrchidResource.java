package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.annotation.Nonnull;
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

    protected String content;
    protected JSONElement embeddedData;

    public OrchidResource(OrchidReference reference) {
        if (reference == null) {
            throw new IllegalArgumentException("A resource must have a valid OrchidReference");
        } else {
            this.reference = reference;
        }
    }

    @NotNull
    public abstract InputStream getContentStream();

    public boolean shouldPrecompile() {
        JSONElement data = getEmbeddedData();
        if (EdenUtils.elementIsObject(data) && ((JSONObject) data.getElement()).has("precompile")) {
            return ((JSONObject) data.getElement()).getBoolean("precompile");
        }

        String rawContent = OrchidExtensionsKt.readToString(getContentStream());

        return reference.getContext().resolve(OrchidPrecompiler.class).shouldPrecompile(reference.getExtension(), rawContent);
    }

    public boolean shouldRender() {
        return true;
    }

    public String compileContent(Object data) {
        String compiledContent = getContent();
        if (!EdenUtils.isEmpty(compiledContent)) {
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

    public JSONElement getEmbeddedData() {
        loadContent();
        return this.embeddedData;
    }

// Freeable Resource Impl
//----------------------------------------------------------------------------------------------------------------------

    protected final void loadContent() {
        String rawContent = OrchidExtensionsKt.readToString(getContentStream());
        if(rawContent != null) {
            EdenPair<String, Map<String, Object>> parsedContent = reference.getContext().getEmbeddedData(reference.getExtension(), rawContent);
            this.content = parsedContent.first;
            this.embeddedData = new JSONElement(new JSONObject(parsedContent.second));
        }
        else {
            this.content = "";
            this.embeddedData = null;
        }
    }

    public void free() {
        content = null;
        embeddedData = null;
    }

}
